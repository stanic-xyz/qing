package cn.chenyunlong.qing.anime.interfaces.dto.request;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 将平安银行 Excel 流水转换为钱迹 CSV 模板的工具。
 * 支持按表头自适应识别常见字段：交易日期/时间、收入金额、支出金额、交易金额、收支方向、摘要、交易类型、对方户名等。
 * 输出钱迹模板 13 列：时间, 分类, 二级分类, 类型, 金额, 账户1, 账户2, 备注, 账单标记, 手续费, 优惠券, 标签, 账单图片。
 * 使用示例：
 * mvn -q -DskipTests exec:java -Dexec.args="-in \"e:\...\平安银行个人账户交易明细 JYLS251201048102.xlsx\" -out \"e:\...\Qianji_From_PAB.csv\" -acct \"平安银行\""
 */
public class BankToQianji {

    /**
     * 程序入口：解析参数，读取 Excel，生成 CSV
     */
    public static void main(String[] args) {
        Args a = parseArgs(args);
        if (a.input == null) {
            System.out.println("用法: -in <输入Excel路径> [-out <输出CSV路径>] [-acct <账户名称>] [-tag <标签>]");
            System.out.println("示例: -in \"e:\\workspace\\gitee\\stan\\bill\\my_bill\\平安银行个人账户交易明细 JYLS251201048102.xlsx\" -out \"e:\\workspace\\gitee\\stan\\bill\\my_bill\\Qianji_From_PAB.csv\" -acct \"平安银行\"");
            return;
        }
        if (a.output == null) {
            String base = Path.of(a.input).getParent().toString();
            a.output = base + File.separator + "Qianji_From_PAB.csv";
        }
        if (a.accountName == null) a.accountName = "平安银行";

        try (Workbook wb = loadWorkbook(Path.of(a.input))) {
            Sheet sheet = findDataSheet(wb);
            int headerRowIdx = findHeaderRow(sheet);
            if (headerRowIdx < 0) throw new IllegalStateException("未找到表头行（例如包含“交易日期/摘要”等关键列）");
            Row headerRow = sheet.getRow(headerRowIdx);
            Map<String, Integer> cols = mapHeaders(headerRow);

            List<String[]> outRows = new ArrayList<>();
            outRows.add(qianjiHeader());

            for (int r = headerRowIdx + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null || isRowBlank(row)) continue;

                Record rec = parseRecord(row, cols);
                if (rec == null) continue;

                OutputRow out = transformToQianji(rec, a.accountName, a.defaultTag);
                outRows.add(out.toCsvRow());
            }

            writeCsv(outRows, Path.of(a.output));
            System.out.println("已生成: " + a.output + " 记录数: " + (outRows.size() - 1));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * 参数封装
     */
    static class Args {
        String input;
        String output;
        String accountName;
        String defaultTag;
    }

    /**
     * 解析命令行参数
     */
    private static Args parseArgs(String[] args) {
        Args a = new Args();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-in" -> a.input = nextArg(args, ++i);
                case "-out" -> a.output = nextArg(args, ++i);
                case "-acct" -> a.accountName = nextArg(args, ++i);
                case "-tag" -> a.defaultTag = nextArg(args, ++i);
            }
        }
        return a;
    }

    /**
     * 读取下一个参数值
     */
    private static String nextArg(String[] args, int i) {
        return (i >= 0 && i < args.length) ? args[i] : null;
    }

    /**
     * 打开 XLSX 工作簿
     */
    private static Workbook loadWorkbook(Path excelPath) throws IOException {
        try (InputStream in = Files.newInputStream(excelPath)) {
            return new XSSFWorkbook(in);
        }
    }

    /**
     * 查找数据表：优先匹配名称包含“交易明细”，否则取第一个非空表
     */
    private static Sheet findDataSheet(Workbook wb) {
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet s = wb.getSheetAt(i);
            if (s != null && s.getSheetName() != null && s.getSheetName().contains("交易")) return s;
        }
        return wb.getSheetAt(0);
    }

    /**
     * 寻找表头行：扫描前 20 行，找包含常见关键列的行
     */
    private static int findHeaderRow(Sheet sheet) {
        Set<String> keys = new HashSet<>(Arrays.asList("交易日期", "交易时间", "记账日期", "摘要", "交易金额", "收入金额", "支出金额", "收支方向", "交易类型"));
        for (int r = 0; r <= Math.min(sheet.getLastRowNum(), 20); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            int hit = 0;
            for (Cell c : row) {
                String v = cellString(c);
                if (v != null && keys.contains(clean(v))) hit++;
            }
            if (hit >= 2) return r;
        }
        return -1;
    }

    /**
     * 将表头映射为列索引
     */
    private static Map<String, Integer> mapHeaders(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        for (Cell c : headerRow) {
            String k = clean(cellString(c));
            if (k == null || k.isEmpty()) continue;
            map.put(k, c.getColumnIndex());
        }
        return map;
    }

    /**
     * 判断一行是否空白
     */
    private static boolean isRowBlank(Row row) {
        for (Cell c : row) {
            if (cellString(c) != null && !cellString(c).isBlank()) return false;
            if (cellNumeric(c) != null) return false;
        }
        return true;
    }

    /**
     * 读取单元格字符串表现
     */
    private static String cellString(Cell c) {
        if (c == null) return null;
        return switch (c.getCellType()) {
            case STRING -> c.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(c) ? formatExcelDate(c.getDateCellValue()) : trimZero(c.getNumericCellValue());
            case BOOLEAN -> String.valueOf(c.getBooleanCellValue());
            case FORMULA -> c.getCellFormula();
            default -> null;
        };
    }

    /**
     * 读取数值（若为文本尝试解析）
     */
    private static Double cellNumeric(Cell c) {
        if (c == null) return null;
        try {
            return switch (c.getCellType()) {
                case NUMERIC -> DateUtil.isCellDateFormatted(c) ? null : c.getNumericCellValue();
                case STRING -> {
                    String s = c.getStringCellValue();
                    yield s == null || s.isBlank() ? null : Double.parseDouble(s.replace(",", "").trim());
                }
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 格式化 Excel 日期为 yyyy/M/d HH:mm
     */
    private static String formatExcelDate(Date d) {
        return new SimpleDateFormat("yyyy/M/d HH:mm").format(d);
    }

    /**
     * 去除字符串中的多余空白与全角问题
     */
    private static String clean(String s) {
        if (s == null) return null;
        return s.replace("\u00A0", " ").trim();
    }

    /**
     * 去掉数值末尾的 .0
     */
    private static String trimZero(double v) {
        String s = String.valueOf(v);
        if (s.endsWith(".0")) return s.substring(0, s.length() - 2);
        return s;
    }

    /**
     * 源记录结构
     */
    static class Record {
        String dateTime;     // yyyy/M/d HH:mm
        Double income;       // 收入金额（正）
        Double expense;      // 支出金额（正）
        Double amount;       // 交易金额（可能正负）
        String direction;    // 收入/支出
        String summary;      // 摘要
        String typeText;     // 交易类型
        String counterparty; // 对方户名/商户
        String channel;      // 渠道
    }

    /**
     * 从一行解析源记录
     */
    private static Record parseRecord(Row row, Map<String, Integer> idx) {
        Record r = new Record();

        r.summary = get(row, idx, List.of("摘要", "交易摘要", "交易说明"));
        r.typeText = get(row, idx, List.of("交易类型", "业务类型"));
        r.counterparty = get(row, idx, List.of("对方户名", "商户名称", "对方账户名称"));
        r.channel = get(row, idx, List.of("交易渠道", "渠道"));

        String dt = get(row, idx, List.of("交易时间", "交易日期", "记账日期"));
        r.dateTime = normalizeDateTime(dt);

        r.income = num(row, idx, List.of("收入金额", "贷方发生额", "贷方"));
        r.expense = num(row, idx, List.of("支出金额", "借方发生额", "借方"));
        r.amount = num(row, idx, List.of("交易金额", "金额"));
        r.direction = get(row, idx, List.of("收支方向", "方向"));

        // 若只有交易金额，方向未知，则根据正负判定
        if (r.amount != null && (r.income == null && r.expense == null)) {
            if (r.amount > 0) r.income = r.amount;
            else r.expense = Math.abs(r.amount);
        }
        // 若收入与支出都为空，则丢弃
        if (r.income == null && r.expense == null) return null;

        return r;
    }

    /**
     * 获取文本列值（按候选列名依次尝试）
     */
    private static String get(Row row, Map<String, Integer> idx, List<String> keys) {
        for (String k : keys) {
            Integer i = idx.get(k);
            if (i != null) {
                String v = cellString(row.getCell(i));
                if (v != null && !v.isBlank()) return clean(v);
            }
        }
        return null;
    }

    /**
     * 获取数值列值（按候选列名依次尝试）
     */
    private static Double num(Row row, Map<String, Integer> idx, List<String> keys) {
        for (String k : keys) {
            Integer i = idx.get(k);
            if (i != null) {
                Double v = cellNumeric(row.getCell(i));
                if (v != null && Math.abs(v) > 0.0000001) return Math.abs(v);
            }
        }
        return null;
    }

    /**
     * 规范化日期时间为钱迹格式 yyyy/M/d HH:mm
     */
    private static String normalizeDateTime(String raw) {
        if (raw == null || raw.isBlank()) {
            return DateTimeFormatter.ofPattern("yyyy/M/d HH:mm").format(LocalDateTime.now());
        }
        String s = raw.trim();
        // 常见格式尝试
        String[] patterns = {
                "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
                "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm",
                "yyyy-MM-dd", "yyyy/MM/dd"
        };
        for (String p : patterns) {
            try {
                SimpleDateFormat f = new SimpleDateFormat(p);
                Date d = f.parse(s);
                return formatExcelDate(d);
            } catch (ParseException ignored) {
            }
        }
        return s;
    }

    /**
     * 输出行结构（钱迹字段）
     */
    static class OutputRow {
        String time;       // 时间
        String category;   // 分类
        String subcat;     // 二级分类
        String type;       // 类型（支出/收入/转账）
        String amount;     // 金额（正数）
        String account1;   // 账户1
        String account2;   // 账户2
        String note;       // 备注
        String mark;       // 账单标记
        String fee;        // 手续费
        String coupon;     // 优惠券
        String tag;        // 标签
        String image;      // 账单图片

        /**
         * 转为 CSV 行
         */
        String[] toCsvRow() {
            return new String[]{time, category, subcat, type, amount, account1, account2, note, mark, fee, coupon, tag, image};
        }
    }

    /**
     * 转换为钱迹结构（含基本分类与类型推断）
     */
    private static OutputRow transformToQianji(Record rec, String accountName, String defaultTag) {
        OutputRow o = new OutputRow();
        o.time = rec.dateTime;
        o.account1 = accountName;
        o.account2 = "";
        o.fee = "";
        o.coupon = "";
        o.image = "";
        o.tag = defaultTag == null ? "" : defaultTag;

        boolean isTransfer = isTransfer(rec);
        if (isTransfer) {
            o.type = "转账";
            o.amount = String.valueOf(rec.income != null ? rec.income : rec.expense);
            o.mark = "不计收支";
            o.category = "";
            o.subcat = "";
        } else if (rec.expense != null && (rec.income == null || rec.expense >= rec.income)) {
            o.type = "支出";
            o.amount = String.valueOf(rec.expense);
            Category c = guessCategory(rec, false);
            o.category = c.category;
            o.subcat = c.subcat;
            o.mark = "";
        } else {
            o.type = "收入";
            o.amount = String.valueOf(rec.income);
            Category c = guessCategory(rec, true);
            o.category = c.category;
            o.subcat = c.subcat;
            o.mark = "";
        }

        StringBuilder note = new StringBuilder();
        if (rec.summary != null) note.append(rec.summary);
        if (rec.counterparty != null) {
            if (note.length() > 0) note.append(" | ");
            note.append("对方:").append(rec.counterparty);
        }
        if (rec.channel != null) {
            if (note.length() > 0) note.append(" | ");
            note.append("渠道:").append(rec.channel);
        }
        o.note = note.toString();
        return o;
    }

    /**
     * 简单转账识别规则
     */
    private static boolean isTransfer(Record r) {
        String t = (r.typeText == null ? "" : r.typeText) + " " + (r.summary == null ? "" : r.summary);
        return t.contains("转账") || t.contains("行内转") || t.contains("他行转") || t.contains("内部转") || t.contains("余额互转");
    }

    /**
     * 分类结构
     */
    static class Category {
        String category;
        String subcat;

        Category(String c, String s) {
            this.category = c;
            this.subcat = s;
        }
    }

    /**
     * 依据摘要/商户的关键词做轻量分类推断
     */
    private static Category guessCategory(Record r, boolean isIncome) {
        String t = ((r.summary == null ? "" : r.summary) + " " + (r.counterparty == null ? "" : r.counterparty)).toLowerCase();
        if (isIncome) {
            if (containsAny(t, "工资", "薪资", "payroll")) return new Category("工资", "");
            if (containsAny(t, "红包", "转入", "利息")) return new Category("礼金", "");
            return new Category("其他收入", "");
        } else {
            if (containsAny(t, "肯德基", "麦当劳", "餐饮", "午餐", "晚餐", "早餐", "外卖")) return new Category("三餐", "餐饮");
            if (containsAny(t, "奶茶", "茶百道", "咖啡")) return new Category("零食", "奶茶");
            if (containsAny(t, "淘宝", "京东", "拼多多", "天猫")) return new Category("网购", "");
            if (containsAny(t, "地铁", "公交", "打车", "滴滴")) return new Category("交通", "");
            if (containsAny(t, "水费", "电费", "燃气")) return new Category("居家", "生活缴费");
            return new Category("其他", "");
        }
    }

    /**
     * 包含任一关键词
     */
    private static boolean containsAny(String text, String... keys) {
        for (String k : keys) if (text.contains(k)) return true;
        return false;
    }

    /**
     * 钱迹模板表头
     */
    private static String[] qianjiHeader() {
        return new String[]{"时间", "分类", "二级分类", "类型", "金额", "账户1", "账户2", "备注", "账单标记", "手续费", "优惠券", "标签", "账单图片"};
    }

    /**
     * 写出 CSV（UTF-8 BOM，带基本转义）
     */
    private static void writeCsv(List<String[]> rows, Path out) throws IOException {
        try (OutputStream os = Files.newOutputStream(out);
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write('\uFEFF');
            for (String[] r : rows) {
                bw.write(joinCsv(r));
                bw.write("\r\n");
            }
        }
    }

    /**
     * 拼接一行 CSV，处理逗号/引号/换行转义
     */
    private static String joinCsv(String[] cols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            String v = cols[i] == null ? "" : cols[i];
            boolean needQuote = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
            if (needQuote) {
                v = v.replace("\"", "\"\"");
                sb.append("\"").append(v).append("\"");
            } else {
                sb.append(v);
            }
            if (i < cols.length - 1) sb.append(",");
        }
        return sb.toString();
    }
}
