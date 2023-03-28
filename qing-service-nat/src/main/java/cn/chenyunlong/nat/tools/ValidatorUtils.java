/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.nat.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 验证类工具集
 * </p>
 */
@Slf4j
public final class ValidatorUtils {

    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};
    //星座的中文
    @SuppressWarnings("unused")
    public static final String[] constellationNames = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    //星座的英文
    public static final String[] constellationNames_en = {"aquarius", "pisces", "aries", "taurus", "gemini", "cancer", "leo", "virgo", "libra", "scorpio", "sagittarius", "capricorn"};

    /**
     * 判断是否是手机号码
     *
     * @param mobile 电话号码
     */
    public static boolean isMobileNO(String mobile) {
        if (mobile == null) {
            return false;
        }
        return mobile.matches("(^1[0-9]{10}$)|(^([5689])\\d{7}$)");
    }

    /**
     * 邮箱格式判断
     *
     * @param email 电子邮件地址
     */
    public static boolean isEmail(String email) {
        String str = "^[0-9a-zA-Z_][-_.0-9a-zA-Z]{0,63}@([0-9a-zA-Z][0-9a-zA-Z-]*\\.)+[a-zA-Z]{2,4}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断是否是userId
     * <p>
     * userId必须是6~9位的纯数字字符串
     *
     * @param string userId字符串
     * @return boolean 是否是userId
     */
    public static boolean isUserId(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("^[1-9]\\d{6,9}");
    }

    /**
     * 用户账号格式判断
     *
     * @param userAccount 用户账号
     */
    public static boolean isUserAccount(String userAccount) {
        String str = "^[a-zA-Z][a-zA-Z0-9_]{5,15}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(userAccount);
        return m.matches();
    }

    /**
     * 用户密码格式判断
     *
     * @param userPassword 用户密码
     * @return boolean
     */
    public static boolean isUserPwd(String userPassword) {
        if (userPassword == null) {
            return false;
        }
        return userPassword.matches("^[\\w-]{6,16}$");
    }

    /**
     * 护照格式判断
     *
     * @param passport 护照编号
     */
    public static boolean isPassport(String passport) {
        passport = passport.replaceAll(" ", "");
        String str = "^([a-zA-Z]{5,17})|([a-zA-Z0-9]{5,17})$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(passport);
        return matcher.matches();
    }

    /**
     * 邮编格式判断
     *
     * @param zipCode 邮政编码
     */
    public static boolean isZipCode(String zipCode) {
        String str = "^\\d{6}$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(zipCode);
        return matcher.matches();
    }

    /**
     * 组织机构代码
     *
     * @param organizeCode 组织机构代码
     */
    public static boolean isOrganizeCode(String organizeCode) {
        String str = "^([0-9A-Z]){8}-?[0-9|X]$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(organizeCode);
        return matcher.matches();
    }

    /**
     * 座机电话
     *
     * @param officePhone 座机号码
     */
    public static boolean isOfficePhone(String officePhone) {
        String str = "(^(\\d{11})$|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d)|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d))$)";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(officePhone);
        return matcher.matches();
    }

    /**
     * 注册资本是
     *
     * @param registeredCapital 注册资本
     * @return boolean
     */
    public static boolean isRegisteredCapital(String registeredCapital) {
        String compile = "^(([1-9]\\d{0,11})|0)(\\.\\d{1,2})?$";
        Pattern pattern = Pattern.compile(compile);
        Matcher matcher = pattern.matcher(registeredCapital);
        return matcher.matches();
    }

    /**
     * 验证字符串是否是BigDecimal
     *
     * @param numberStr 数字的字符串
     * @return boolean
     */
    public static boolean isBigDecimal(String numberStr) {
        Matcher match;
        if (isNumeric(numberStr)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            match = pattern.matcher(numberStr.trim());
        } else {
            Pattern pattern;
            if (!numberStr.trim().contains(".")) {
                pattern = Pattern.compile("^[+]?[0-9]*");
            } else {
                pattern = Pattern.compile("^[+]?[0-9]+(\\.\\d{1,100})");
            }
            match = pattern.matcher(numberStr.trim());
        }
        return match.matches();
    }

    /**
     * 银行账号校验
     *
     * @param bankAccount 银行账户校验
     */
    public static boolean isBankAccountNo(String bankAccount) {
        if (!isNumeric(bankAccount)) {
            return false;
        }
        return bankAccount.length() == 15 || bankAccount.length() == 16 || bankAccount.length() == 19 || bankAccount.length() == 23;

    }

    /**
     * 检测18位身份证号是否合法
     *
     * @param idNumber 身份证号
     */
    public static Boolean isIdNumber(String idNumber) {
        String str = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(idNumber);
        return m.matches();
    }

    /**
     * 经办人身份证验证
     * 功能：身份证的有效验证
     *
     * @param IDStr    身份证号
     * @param birthday 生日
     * @param sex      性别
     * @return 有效：返回"" 无效：返回String信息
     * @throws NumberFormatException 数字格式异常
     * @throws ParseException        解析异常
     */
    public static String IDCardValidate(String IDStr, Date birthday, String sex)
            throws NumberFormatException, ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        // 性别位
        int gender = 0;
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
            gender = Integer.parseInt(IDStr.substring(16, 17)) % 2 == 1 ? 1 : 2;
        } else {
            gender = Integer.parseInt(IDStr.substring(14, 15)) % 2 == 1 ? 1 : 2;
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (!isNumeric(Ai)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // 性别判断
        if (!StringUtils.isEmpty(sex) && !String.valueOf(gender).equals(sex)) {
            errorInfo = "身份证号码与性别不匹配!";
            return errorInfo;
        }
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }

        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        if (birthday != null && !(strYear + "-" + strMonth + "-" + strDay).equals(s.format(birthday))) {
            errorInfo = "身份证号码与出生日期不匹配!";
            return errorInfo;
        }

        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable<String, String> h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int totalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            totalmulAiWi = totalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = totalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (!Ai.equals(IDStr.toLowerCase())) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    public static Hashtable<String, String> GetAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param string 字符串
     * @return boolean
     */
    public static boolean isNumeric(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(string);
        return isNum.matches();
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate 日期字符串
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile(
                "^((\\d{2}(([02468][048])|([13579][26]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3])):([0-5]?[0-9])((\\s)|(:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

    /**
     * 是否是ipv4地址
     *
     * @param str 字符串
     */
    public static boolean isIPv4Address(String str) {
        if (str != null) {
            int dot1 = str.indexOf('.');
            if (dot1 <= 0) {
                return true;
            }
            int temp;
            try {
                temp = Integer.parseInt(str.substring(0, dot1++));
                if (temp < 0 || temp > 255) {
                    return true;
                }
            } catch (Exception ex) {
                return true;
            }

            int dot2 = str.indexOf('.', dot1);
            if (dot2 <= 0) {
                return true;
            }
            try {
                temp = Integer.parseInt(str.substring(dot1, dot2++));
                if (temp < 0 || temp > 255) {
                    return true;
                }
            } catch (Exception ex) {
                return true;
            }

            int dot3 = str.indexOf('.', dot2);
            if (dot3 <= 0) {
                return true;
            }
            try {
                temp = Integer.parseInt(str.substring(dot2, dot3++));
                if (temp < 0 || temp > 255) {
                    return true;
                }
            } catch (Exception ex) {
                return true;
            }
            try {
                temp = Integer.parseInt(str.substring(dot3));
                if (temp < 0 || temp > 255) {
                    return true;
                }
            } catch (Exception ex) {
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * 得到星座
     * 根据出生日期获取星座
     *
     * @param date   日期
     * @param locale 语言环境
     * @return {@link String}
     */
    public static String getConstellation(LocalDate date, Locale locale) {
        if (date == null) {
            return "";
        }
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        if (day < constellationEdgeDay[month - 1]) {
            if (month == 1) {
                month = Month.DECEMBER.getValue();
            } else {
                month = month - 1;
            }
        }
        //使用了java的ResourceBundle
        ResourceBundle constellation = ResourceBundle.getBundle("constellation", locale);
        return constellation.getString(constellationNames_en[month - 1]);
        // default to return 魔羯
    }
}
