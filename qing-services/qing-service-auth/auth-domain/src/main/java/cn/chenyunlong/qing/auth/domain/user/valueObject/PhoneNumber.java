package cn.chenyunlong.qing.auth.domain.user.valueObject;

import lombok.NonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 电话号码值对象
 * 遵循值对象模式：不可变、无副作用、基于值的相等性
 * 支持多种格式验证和标准化存储
 *
 * @param value       存储格式：国际格式（E.164标准）
 * @param countryCode 区域代码（如 +86）
 * @param localNumber 本地号码（不含国家代码）
 * @param normalized  标准化格式（统一为 E.164 格式）
 */
public record PhoneNumber(String value, String countryCode, String localNumber, String normalized) implements Serializable, Comparable<PhoneNumber> {

    // 常用正则表达式模式
    private static final Pattern CHINESE_PHONE_PATTERN =
            Pattern.compile("^(\\+?0?86\\-?)?1[3-9]\\d{9}$");

    private static final Pattern CHINESE_LANDLINE_PATTERN =
            Pattern.compile("^0\\d{9,12}$");

    private static final Pattern E164_PATTERN =
            Pattern.compile("^\\+[1-9]\\d{1,3}\\d{1,14}$");

    private static final Pattern INTERNATIONAL_PATTERN =
            Pattern.compile("^\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$");

    /**
     * 私有构造器，强制通过工厂方法创建
     */
    public PhoneNumber(String value, String countryCode, String localNumber, String normalized) {
        this.value = Objects.requireNonNull(value, "Phone number value cannot be null");
        this.countryCode = Objects.requireNonNull(countryCode, "Country code cannot be null");
        this.localNumber = Objects.requireNonNull(localNumber, "Local number cannot be null");
        this.normalized = Objects.requireNonNull(normalized, "Normalized value cannot be null");
    }

    /**
     * 主要工厂方法：从任意格式的电话号码创建
     */
    public static PhoneNumber of(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        String cleaned = cleanPhoneNumber(phoneNumber);

        // 验证并解析
        ParsedPhoneNumber parsed = parseAndValidate(cleaned);

        return new PhoneNumber(
                phoneNumber.trim(),           // 原始值（去除首尾空格）
                parsed.countryCode(),         // 解析出的国家代码
                parsed.localNumber(),         // 本地号码
                parsed.normalized()           // 标准化格式
        );
    }

    /**
     * 从标准E.164格式创建（例如：+8613800138000）
     */
    public static PhoneNumber fromE164(String e164Number) {
        if (e164Number == null) {
            return null;
        }

        if (!E164_PATTERN.matcher(e164Number).matches()) {
            throw new IllegalArgumentException("Invalid E.164 format: " + e164Number);
        }

        String countryCode = extractCountryCode(e164Number);
        String localNumber = e164Number.substring(countryCode.length());

        return new PhoneNumber(
                e164Number,
                countryCode,
                localNumber,
                e164Number
        );
    }

    /**
     * 从中国手机号创建（支持多种格式）
     */
    public static PhoneNumber fromChinese(String chineseNumber) {
        if (!CHINESE_PHONE_PATTERN.matcher(chineseNumber).matches()) {
            throw new IllegalArgumentException("Invalid Chinese phone number: " + chineseNumber);
        }

        String cleaned = cleanPhoneNumber(chineseNumber);

        // 标准化为中国手机号格式
        if (!cleaned.startsWith("+86")) {
            if (cleaned.startsWith("86")) {
                cleaned = "+" + cleaned;
            } else if (cleaned.startsWith("0")) {
                cleaned = "+86" + cleaned.substring(1);
            } else {
                cleaned = "+86" + cleaned;
            }
        }

        return PhoneNumber.fromE164(cleaned);
    }

    /**
     * 从国家代码和本地号码创建
     */
    public static PhoneNumber fromParts(String countryCode, String localNumber) {
        validateCountryCode(countryCode);
        validateLocalNumber(localNumber, countryCode);

        String normalized = normalize(countryCode, localNumber);

        return new PhoneNumber(
                normalized,
                countryCode,
                localNumber,
                normalized
        );
    }

    /**
     * 创建国际长途号码（例如：+1-800-555-0199）
     */
    public static PhoneNumber international(String countryCode, String areaCode, String subscriberNumber) {
        validateCountryCode(countryCode);

        // 美国/加拿大：国家代码 + 3位区号 + 7位号码
        if ("+1".equals(countryCode)) {
            if (!areaCode.matches("\\d{3}")) {
                throw new IllegalArgumentException("Invalid area code for North America: " + areaCode);
            }
            if (!subscriberNumber.matches("\\d{7}")) {
                throw new IllegalArgumentException("Invalid subscriber number: " + subscriberNumber);
            }
            return PhoneNumber.fromParts(countryCode, areaCode + subscriberNumber);
        }

        // 其他国家的处理逻辑...
        String localNumber = areaCode + subscriberNumber;
        return PhoneNumber.fromParts(countryCode, localNumber);
    }

    /**
     * 解析并验证电话号码
     */
    private static ParsedPhoneNumber parseAndValidate(String cleanedPhoneNumber) {
        // 1. 验证是否为有效电话号码
        if (!isValidPhoneNumber(cleanedPhoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format: " + cleanedPhoneNumber);
        }

        // 2. 提取国家代码
        String countryCode = extractCountryCode(cleanedPhoneNumber);

        // 修正后的本地号码提取逻辑
        String localNumber = getLocalNumber(cleanedPhoneNumber, countryCode);

        // 4. 验证本地号码长度（根据国家代码）
        validateLocalNumber(localNumber, countryCode);

        // 5. 标准化
        String normalized = normalize(countryCode, localNumber);

        return new ParsedPhoneNumber(countryCode, localNumber, normalized);
    }

    private static String getLocalNumber(String cleanedPhoneNumber, String countryCode) {
        String localNumber;

        // 情况1：如果清理后的字符串以国家代码开头，则去掉国家代码
        if (cleanedPhoneNumber.startsWith(countryCode)) {
            localNumber = cleanedPhoneNumber.substring(countryCode.length());
        }
        // 情况2：如果国家代码是"+86"，但字符串以"0"开头（中国本地固定电话）
        else if (countryCode.equals("+86") && cleanedPhoneNumber.startsWith("0")) {
            localNumber = cleanedPhoneNumber; // 包含区号的本地号码
        }
        // 情况3：其他情况，直接使用清理后的字符串作为本地号码
        else {
            localNumber = cleanedPhoneNumber;
        }
        return localNumber;
    }

    /**
     * 验证电话号码格式
     */
    private static boolean isValidPhoneNumber(String phoneNumber) {
        // 检查是否符合国际格式
        if (INTERNATIONAL_PATTERN.matcher(phoneNumber).matches()) {
            return true;
        }

        // 检查是否符合中国手机号格式
        return CHINESE_PHONE_PATTERN.matcher(phoneNumber).matches();

        // 可以添加更多国家/地区的验证逻辑
    }

    /**
     * 提取国家代码
     */
    private static String extractCountryCode(String phoneNumber) {
        if (phoneNumber.startsWith("+")) {
            // E.164格式：+国家代码...
            // 国家代码长度：1-3位数字
            String numberPart = phoneNumber.substring(1);

            // 常见国家代码规则
            if (numberPart.startsWith("1")) {  // 北美
                return "+1";
            } else if (numberPart.startsWith("86")) {  // 中国
                return "+86";
            } else if (numberPart.startsWith("44")) {  // 英国
                return "+44";
            } else if (numberPart.startsWith("81")) {  // 日本
                return "+81";
            } else if (numberPart.startsWith("82")) {  // 韩国
                return "+82";
            } else if (numberPart.startsWith("91")) {  // 印度
                return "+91";
            } else if (numberPart.startsWith("49")) {  // 德国
                return "+49";
            } else if (numberPart.startsWith("33")) {  // 法国
                return "+33";
            } else if (numberPart.matches("^[2-9]\\d{0,2}")) {
                // 其他国家：首字符2-9，长度1-3位
                String code = "+";
                for (int i = 0; i < Math.min(numberPart.length(), 3); i++) {
                    char c = numberPart.charAt(i);
                    if (c >= '0' && c <= '9') {
                        code += c;
                        // 检查是否可能的国家代码结束
                        if (isValidCountryCode(code)) {
                            return code;
                        }
                    } else {
                        break;
                    }
                }
            }
        } else if (phoneNumber.startsWith("00")) {
            // 国际长途前缀：00 + 国家代码...
            String withoutPrefix = phoneNumber.substring(2);
            return "+" + extractCountryCode("+" + withoutPrefix).substring(1);
        } else if (phoneNumber.startsWith("0") && phoneNumber.length() > 10) {
            // 中国国内长途：0 + 区号 + 本地号码
            // 这里默认处理中国固定电话
            return "+86";
        }

        // 默认处理（可能是本地号码）
        return "+86";  // 默认中国
    }

    /**
     * 验证国家代码
     */
    private static void validateCountryCode(String countryCode) {
        if (countryCode == null || !countryCode.startsWith("+")) {
            throw new IllegalArgumentException("Country code must start with '+': " + countryCode);
        }

        String digits = countryCode.substring(1);
        if (digits.isEmpty() || !digits.matches("\\d{1,3}")) {
            throw new IllegalArgumentException("Invalid country code: " + countryCode);
        }

        // 验证是否为已知的国家代码（可选）
        if (!isValidCountryCode(countryCode)) {
            throw new IllegalArgumentException("Unsupported country code: " + countryCode);
        }
    }

    /**
     * 验证本地号码
     */
    private static void validateLocalNumber(String localNumber, String countryCode) {
        if (localNumber == null || localNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Local number cannot be null or empty");
        }

        // 移除所有非数字字符后验证
        String digitsOnly = localNumber.replaceAll("\\D", "");

        if (digitsOnly.isEmpty()) {
            throw new IllegalArgumentException("Local number must contain digits: " + localNumber);
        }

        // 根据国家代码验证长度
        int minLength = getMinLengthForCountry(countryCode);
        int maxLength = getMaxLengthForCountry(countryCode);

        if (digitsOnly.length() < minLength || digitsOnly.length() > maxLength) {
            throw new IllegalArgumentException(
                    String.format("Local number length must be between %d and %d for country %s: %s",
                            minLength, maxLength, countryCode, localNumber)
            );
        }

        // 中国手机号特殊验证
        if ("+86".equals(countryCode) && digitsOnly.startsWith("1")) {
            if (!digitsOnly.matches("1[3-9]\\d{9}")) {
                throw new IllegalArgumentException("Invalid Chinese mobile number: " + localNumber);
            }
        }
    }

    /**
     * 清理电话号码（移除空格、括号、连字符等）
     */
    private static String cleanPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        // 移除所有空白字符
        String cleaned = phoneNumber.trim()
                .replaceAll("\\s+", "")
                .replaceAll("[()-]", "")
                .replaceAll("\\.", "");

        // 处理国际长途前缀
        if (cleaned.startsWith("00")) {
            cleaned = "+" + cleaned.substring(2);
        }

        // 处理中国手机号常见格式
        if (cleaned.startsWith("0086")) {
            cleaned = "+" + cleaned.substring(2);
        }

        // 处理中国手机号常见格式
        if (cleaned.startsWith("86")) {
            cleaned = "+" + cleaned;
        }

        return cleaned;
    }

    /**
     * 标准化电话号码
     */
    private static String normalize(String countryCode, String localNumber) {
        // 移除本地号码中的非数字字符
        String digitsOnly = localNumber.replaceAll("\\D", "");

        return countryCode + digitsOnly;
    }

    /**
     * 检查是否为有效的国家代码
     */
    private static boolean isValidCountryCode(String countryCode) {
        // 这里可以维护一个国家代码白名单
        // 为了简化，只验证格式，实际项目可以维护完整的国家代码列表

        String[] validCountryCodes = {
                "+1",   // 北美
                "+7",   // 俄罗斯
                "+20",  // 埃及
                "+27",  // 南非
                "+30",  // 希腊
                "+31",  // 荷兰
                "+32",  // 比利时
                "+33",  // 法国
                "+34",  // 西班牙
                "+36",  // 匈牙利
                "+39",  // 意大利
                "+40",  // 罗马尼亚
                "+41",  // 瑞士
                "+43",  // 奥地利
                "+44",  // 英国
                "+45",  // 丹麦
                "+46",  // 瑞典
                "+47",  // 挪威
                "+48",  // 波兰
                "+49",  // 德国
                "+51",  // 秘鲁
                "+52",  // 墨西哥
                "+53",  // 古巴
                "+54",  // 阿根廷
                "+55",  // 巴西
                "+56",  // 智利
                "+57",  // 哥伦比亚
                "+58",  // 委内瑞拉
                "+60",  // 马来西亚
                "+61",  // 澳大利亚
                "+62",  // 印度尼西亚
                "+63",  // 菲律宾
                "+64",  // 新西兰
                "+65",  // 新加坡
                "+66",  // 泰国
                "+81",  // 日本
                "+82",  // 韩国
                "+84",  // 越南
                "+86",  // 中国
                "+90",  // 土耳其
                "+91",  // 印度
                "+92",  // 巴基斯坦
                "+93",  // 阿富汗
                "+94",  // 斯里兰卡
                "+95",  // 缅甸
                "+98",  // 伊朗
                "+212", // 摩洛哥
                "+213", // 阿尔及利亚
                "+216", // 突尼斯
                "+218", // 利比亚
                "+220", // 冈比亚
                "+221", // 塞内加尔
                "+222", // 毛里塔尼亚
                "+223", // 马里
                "+224", // 几内亚
                "+225", // 科特迪瓦
                "+226", // 布基纳法索
                "+227", // 尼日尔
                "+228", // 多哥
                "+229", // 贝宁
                "+230", // 毛里求斯
                "+231", // 利比里亚
                "+232", // 塞拉利昂
                "+233", // 加纳
                "+234", // 尼日利亚
                "+235", // 乍得
                "+236", // 中非共和国
                "+237", // 喀麦隆
                "+238", // 佛得角
                "+239", // 圣多美和普林西比
                "+240", // 赤道几内亚
                "+241", // 加蓬
                "+242", // 刚果共和国
                "+243", // 刚果民主共和国
                "+244", // 安哥拉
                "+245", // 几内亚比绍
                "+246", // 迪戈加西亚
                "+247", // 阿森松岛
                "+248", // 塞舌尔
                "+249", // 苏丹
                "+250", // 卢旺达
                "+251", // 埃塞俄比亚
                "+252", // 索马里
                "+253", // 吉布提
                "+254", // 肯尼亚
                "+255", // 坦桑尼亚
                "+256", // 乌干达
                "+257", // 布隆迪
                "+258", // 莫桑比克
                "+260", // 赞比亚
                "+261", // 马达加斯加
                "+262", // 留尼汪
                "+263", // 津巴布韦
                "+264", // 纳米比亚
                "+265", // 马拉维
                "+266", // 莱索托
                "+267", // 博茨瓦纳
                "+268", // 斯威士兰
                "+269", // 科摩罗
                "+290", // 圣赫勒拿
                "+291", // 厄立特里亚
                "+297", // 阿鲁巴
                "+298", // 法罗群岛
                "+299", // 格陵兰
                "+350", // 直布罗陀
                "+351", // 葡萄牙
                "+352", // 卢森堡
                "+353", // 爱尔兰
                "+354", // 冰岛
                "+355", // 阿尔巴尼亚
                "+356", // 马耳他
                "+357", // 塞浦路斯
                "+358", // 芬兰
                "+359", // 保加利亚
                "+370", // 立陶宛
                "+371", // 拉脱维亚
                "+372", // 爱沙尼亚
                "+373", // 摩尔多瓦
                "+374", // 亚美尼亚
                "+375", // 白俄罗斯
                "+376", // 安道尔
                "+377", // 摩纳哥
                "+378", // 圣马力诺
                "+379", // 梵蒂冈
                "+380", // 乌克兰
                "+381", // 塞尔维亚
                "+382", // 黑山
                "+383", // 科索沃
                "+385", // 克罗地亚
                "+386", // 斯洛文尼亚
                "+387", // 波斯尼亚和黑塞哥维那
                "+389", // 北马其顿
                "+420", // 捷克
                "+421", // 斯洛伐克
                "+423", // 列支敦士登
                "+500", // 福克兰群岛
                "+501", // 伯利兹
                "+502", // 危地马拉
                "+503", // 萨尔瓦多
                "+504", // 洪都拉斯
                "+505", // 尼加拉瓜
                "+506", // 哥斯达黎加
                "+507", // 巴拿马
                "+508", // 圣皮埃尔和密克隆
                "+509", // 海地
                "+590", // 瓜德罗普
                "+591", // 玻利维亚
                "+592", // 圭亚那
                "+593", // 厄瓜多尔
                "+594", // 法属圭亚那
                "+595", // 巴拉圭
                "+596", // 马提尼克
                "+597", // 苏里南
                "+598", // 乌拉圭
                "+599", // 荷属加勒比
                "+670", // 东帝汶
                "+672", // 诺福克岛
                "+673", // 文莱
                "+674", // 瑙鲁
                "+675", // 巴布亚新几内亚
                "+676", // 汤加
                "+677", // 所罗门群岛
                "+678", // 瓦努阿图
                "+679", // 斐济
                "+680", // 帕劳
                "+681", // 瓦利斯和富图纳
                "+682", // 库克群岛
                "+683", // 纽埃
                "+685", // 萨摩亚
                "+686", // 基里巴斯
                "+687", // 新喀里多尼亚
                "+688", // 图瓦卢
                "+689", // 法属波利尼西亚
                "+690", // 托克劳
                "+691", // 密克罗尼西亚联邦
                "+692", // 马绍尔群岛
                "+850", // 朝鲜
                "+852", // 香港
                "+853", // 澳门
                "+855", // 柬埔寨
                "+856", // 老挝
                "+880", // 孟加拉国
                "+886", // 台湾
                "+960", // 马尔代夫
                "+961", // 黎巴嫩
                "+962", // 约旦
                "+963", // 叙利亚
                "+964", // 伊拉克
                "+965", // 科威特
                "+966", // 沙特阿拉伯
                "+967", // 也门
                "+968", // 阿曼
                "+970", // 巴勒斯坦
                "+971", // 阿拉伯联合酋长国
                "+972", // 以色列
                "+973", // 巴林
                "+974", // 卡塔尔
                "+975", // 不丹
                "+976", // 蒙古
                "+977", // 尼泊尔
                "+992", // 塔吉克斯坦
                "+993", // 土库曼斯坦
                "+994", // 阿塞拜疆
                "+995", // 格鲁吉亚
                "+996", // 吉尔吉斯斯坦
                "+998"  // 乌兹别克斯坦
        };

        for (String validCode : validCountryCodes) {
            if (validCode.equals(countryCode)) {
                return true;
            }
        }

        // 如果不在白名单中，检查格式是否有效
        String digits = countryCode.substring(1);
        return digits.matches("[1-9]\\d{0,2}");
    }

    /**
     * 获取国家的最小号码长度
     */
    private static int getMinLengthForCountry(String countryCode) {
        return switch (countryCode) {
            case "+1" -> 10;  // 北美：3位区号 + 7位号码
            case "+44" -> 10; // 英国：10位（不含国家代码）
            case "+86" -> 11; // 中国：11位手机号
            case "+81" -> 10; // 日本：10位
            case "+82" -> 9;  // 韩国：9-10位
            case "+91" -> 10; // 印度：10位
            default -> 4;     // 默认最小长度
        };
    }

    /**
     * 获取国家的最大号码长度
     */
    private static int getMaxLengthForCountry(String countryCode) {
        return switch (countryCode) {
            case "+1" -> 10;  // 北美固定长度
            case "+44" -> 10; // 英国固定长度
            case "+86" -> 12; // 中国：11位手机号 + 部分座机有区号
            case "+81" -> 11; // 日本：10-11位
            case "+82" -> 10; // 韩国：9-10位
            case "+91" -> 12; // 印度：10-12位
            default -> 15;    // ITU规定最大15位
        };
    }

    // ==================== 业务方法 ====================

    /**
     * 获取标准E.164格式
     */
    public String toE164() {
        return normalized;
    }

    /**
     * 获取国际显示格式（带空格分隔）
     */
    public String toInternationalFormat() {
        if ("+86".equals(countryCode) && localNumber.matches("1[3-9]\\d{9}")) {
            // 中国手机号：+86 138 0013 8000
            return String.format("%s %s %s %s",
                    countryCode,
                    localNumber.substring(0, 3),
                    localNumber.substring(3, 7),
                    localNumber.substring(7)
            );
        } else if ("+1".equals(countryCode)) {
            // 北美格式：+1 (800) 555-0199
            String areaCode = localNumber.substring(0, 3);
            String prefix = localNumber.substring(3, 6);
            String lineNumber = localNumber.substring(6);
            return String.format("%s (%s) %s-%s", countryCode, areaCode, prefix, lineNumber);
        } else {
            // 通用格式：国家代码 + 空格 + 本地号码
            return countryCode + " " + localNumber;
        }
    }

    /**
     * 获取本地显示格式（不带国家代码）
     */
    public String toLocalFormat() {
        if ("+86".equals(countryCode)) {
            if (localNumber.matches("1[3-9]\\d{9}")) {
                // 中国手机号：138-0013-8000
                return String.format("%s-%s-%s",
                        localNumber.substring(0, 3),
                        localNumber.substring(3, 7),
                        localNumber.substring(7)
                );
            } else {
                // 中国座机：(010) 1234-5678
                if (localNumber.length() >= 10) {
                    String areaCode = localNumber.substring(0, 3);
                    String prefix = localNumber.substring(3, 7);
                    String lineNumber = localNumber.substring(7);
                    return String.format("(%s) %s-%s", areaCode, prefix, lineNumber);
                }
            }
        } else if ("+1".equals(countryCode)) {
            // 北美格式：(800) 555-0199
            String areaCode = localNumber.substring(0, 3);
            String prefix = localNumber.substring(3, 6);
            String lineNumber = localNumber.substring(6);
            return String.format("(%s) %s-%s", areaCode, prefix, lineNumber);
        }

        // 默认返回本地号码
        return localNumber;
    }

    /**
     * 获取简写格式（隐藏中间四位）
     */
    public String toMaskedFormat() {
        if (localNumber.length() >= 7) {
            int maskStart = localNumber.length() - 8;
            int maskEnd = localNumber.length() - 4;

            if (maskStart < 0) {
                maskStart = 0;
            }

            StringBuilder masked = new StringBuilder();
            if (maskStart > 0) {
                masked.append(localNumber, 0, maskStart);
            }

            for (int i = maskStart; i < maskEnd && i < localNumber.length(); i++) {
                masked.append("*");
            }

            if (maskEnd < localNumber.length()) {
                masked.append(localNumber.substring(maskEnd));
            }

            return countryCode + " " + masked;
        }

        return countryCode + " " + localNumber;
    }

    /**
     * 判断是否是中国手机号
     */
    public boolean isChineseMobile() {
        return "+86".equals(countryCode) && localNumber.matches("1[3-9]\\d{9}");
    }

    /**
     * 判断是否是北美号码
     */
    public boolean isNorthAmerican() {
        return "+1".equals(countryCode);
    }

    /**
     * 获取运营商信息（仅支持中国手机号）
     */
    public String getOperator() {
        if (!isChineseMobile()) {
            return "Unknown";
        }

        String prefix = localNumber.substring(0, 3);

        // 中国移动
        if (prefix.matches("134[0-8]|135|136|137|138|139|150|151|152|157|158|159|172|178|182|183|184|187|188|198")) {
            return "China Mobile";
        }
        // 中国联通
        else if (prefix.matches("130|131|132|155|156|166|175|176|185|186")) {
            return "China Unicom";
        }
        // 中国电信
        else if (prefix.matches("133|153|173|177|180|181|189|191|199")) {
            return "China Telecom";
        }
        // 虚拟运营商
        else if (prefix.matches("170[0-9]")) {
            return "Virtual Operator";
        }

        return "Other";
    }

    /**
     * 获取号码类型
     */
    public NumberType getNumberType() {
        if (isChineseMobile()) {
            return NumberType.MOBILE;
        }

        // 根据国家代码和本地号码长度判断
        if ("+1".equals(countryCode)) {
            return localNumber.length() == 10 ? NumberType.LANDLINE : NumberType.MOBILE;
        }

        // 通用判断规则
        if (localNumber.startsWith("0") || localNumber.startsWith("1")) {
            return NumberType.MOBILE;
        }

        return NumberType.LANDLINE;
    }

    /**
     * 比较两个电话号码（按标准化格式）
     */
    @Override
    public int compareTo(@NonNull PhoneNumber other) {
        return this.normalized.compareTo(other.normalized);
    }

    /**
     * 转换为字符串（默认返回标准化格式）
     */
    @Override
    public String toString() {
        return normalized;
    }

    // ==================== 内部记录类 ====================

    /**
     * 解析后的电话号码记录
     */
    private record ParsedPhoneNumber(
            String countryCode,
            String localNumber,
            String normalized
    ) {}

    // ==================== 枚举类型 ====================

    /**
     * 电话号码类型枚举
     */
    public enum NumberType {
        MOBILE,     // 手机
        LANDLINE,   // 座机
        TOLL_FREE,  // 免费电话
        PREMIUM,    // 付费电话
        VOIP,       // 网络电话
        UNKNOWN     // 未知
    }

    public boolean equals(PhoneNumber phoneNumber) {
        return phoneNumber.normalized.equals(normalized);
    }
}
