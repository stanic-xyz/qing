/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.interfaces.validation;

import cn.chenyunlong.common.validator.ValidationResult;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 安全校验器
 *
 * <p>提供全面的输入参数安全校验，防范以下安全威胁：</p>
 * <ul>
 *   <li>XSS攻击：跨站脚本攻击</li>
 *   <li>SQL注入：SQL注入攻击</li>
 *   <li>路径遍历：目录遍历攻击</li>
 *   <li>命令注入：系统命令注入</li>
 *   <li>LDAP注入：LDAP注入攻击</li>
 *   <li>XML注入：XML外部实体攻击</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Slf4j
@Component
public class SecurityValidator {

    /**
     * XSS攻击模式
     */
    private static final List<Pattern> XSS_PATTERNS = Arrays.asList(
            Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("<iframe[^>]*>.*?</iframe>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("<object[^>]*>.*?</object>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("<embed[^>]*>.*?</embed>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("<form[^>]*>.*?</form>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onerror\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onclick\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmouseover\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onfocus\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onblur\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onchange\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onsubmit\\s*=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("expression\\s*\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("url\\s*\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("@import", Pattern.CASE_INSENSITIVE)
    );

    /**
     * SQL注入攻击模式
     */
    private static final List<Pattern> SQL_INJECTION_PATTERNS = Arrays.asList(
            Pattern.compile("('|(\\-\\-)|(;)|(\\|)|(\\*)|(%27)|(%2D%2D)|(%7C)|(%2A))", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(((')+|(\\-\\-)+|(;)+|(\\|)+|(\\*)+)+(\\s)*(union|select|insert|delete|update|create|drop|exec|execute|alter|grant|revoke)+(\\s)*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(union|select|insert|delete|update|create|drop|exec|execute|alter|grant|revoke)+(\\s)+", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(and|or)\\b\\s+\\d+\\s*=\\s*\\d+", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(and|or)\\b\\s+['\"]\\w+['\"]\\s*=\\s*['\"]\\w+['\"]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bunion\\b\\s+\\bselect\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bselect\\b\\s+.*\\bfrom\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\binsert\\b\\s+\\binto\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bdelete\\b\\s+\\bfrom\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bupdate\\b\\s+.*\\bset\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bdrop\\b\\s+\\btable\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\btruncate\\b\\s+\\btable\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bexec\\b\\s*\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bexecute\\b\\s*\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bsp_\\w+", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\bxp_\\w+", Pattern.CASE_INSENSITIVE)
    );

    /**
     * 路径遍历攻击模式
     */
    private static final List<Pattern> PATH_TRAVERSAL_PATTERNS = Arrays.asList(
            Pattern.compile("\\.\\.[\\\\/]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\.\\.%2f", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\.\\.%5c", Pattern.CASE_INSENSITIVE),
            Pattern.compile("%2e%2e[\\\\/]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("%2e%2e%2f", Pattern.CASE_INSENSITIVE),
            Pattern.compile("%2e%2e%5c", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\.\\.\\\\", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\.\\./", Pattern.CASE_INSENSITIVE)
    );

    /**
     * 命令注入攻击模式
     */
    private static final List<Pattern> COMMAND_INJECTION_PATTERNS = Arrays.asList(
            Pattern.compile("[;&|`]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\$\\(.*\\)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("`.*`", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\|\\s*(cat|ls|pwd|whoami|id|uname|ps|netstat|ifconfig|ping|wget|curl|nc|telnet|ssh|ftp)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("&&\\s*(cat|ls|pwd|whoami|id|uname|ps|netstat|ifconfig|ping|wget|curl|nc|telnet|ssh|ftp)", Pattern.CASE_INSENSITIVE),
            Pattern.compile(";\\s*(cat|ls|pwd|whoami|id|uname|ps|netstat|ifconfig|ping|wget|curl|nc|telnet|ssh|ftp)", Pattern.CASE_INSENSITIVE)
    );

    /**
     * LDAP注入攻击模式
     */
    private static final List<Pattern> LDAP_INJECTION_PATTERNS = Arrays.asList(
            Pattern.compile("\\*\\)|\\(\\*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\)\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\(\\|", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\|\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\(&", Pattern.CASE_INSENSITIVE),
            Pattern.compile("&\\(", Pattern.CASE_INSENSITIVE)
    );

    /**
     * XML注入攻击模式
     */
    private static final List<Pattern> XML_INJECTION_PATTERNS = Arrays.asList(
            Pattern.compile("<!\\[CDATA\\[", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<!DOCTYPE", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<!ENTITY", Pattern.CASE_INSENSITIVE),
            Pattern.compile("&\\w+;", Pattern.CASE_INSENSITIVE),
            Pattern.compile("&#\\d+;", Pattern.CASE_INSENSITIVE),
            Pattern.compile("&#x[0-9a-fA-F]+;", Pattern.CASE_INSENSITIVE)
    );

    /**
     * 危险文件扩展名
     */
    private static final List<String> DANGEROUS_FILE_EXTENSIONS = Arrays.asList(
            ".exe", ".bat", ".cmd", ".com", ".pif", ".scr", ".vbs", ".js", ".jar", ".jsp", ".php", ".asp", ".aspx"
    );

    /**
     * 校验输入是否包含XSS攻击
     *
     * @param input 输入内容
     * @return 校验结果
     */
    public ValidationResult validateXSS(String input) {
        if (StrUtil.isBlank(input)) {
            return ValidationResult.success();
        }

        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(input).find()) {
                log.warn("检测到XSS攻击尝试: {}", maskSensitiveContent(input));
                return ValidationResult.failure("输入内容包含潜在的XSS攻击代码", "XSS_ATTACK_DETECTED");
            }
        }

        return ValidationResult.success();
    }

    /**
     * 校验输入是否包含SQL注入攻击
     *
     * @param input 输入内容
     * @return 校验结果
     */
    public ValidationResult validateSQLInjection(String input) {
        if (StrUtil.isBlank(input)) {
            return ValidationResult.success();
        }

        for (Pattern pattern : SQL_INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                log.warn("检测到SQL注入攻击尝试: {}", maskSensitiveContent(input));
                return ValidationResult.failure("输入内容包含潜在的SQL注入代码", "SQL_INJECTION_DETECTED");
            }
        }

        return ValidationResult.success();
    }

    /**
     * 校验输入是否包含路径遍历攻击
     *
     * @param input 输入内容
     * @return 校验结果
     */
    public ValidationResult validatePathTraversal(String input) {
        if (StrUtil.isBlank(input)) {
            return ValidationResult.success();
        }

        for (Pattern pattern : PATH_TRAVERSAL_PATTERNS) {
            if (pattern.matcher(input).find()) {
                log.warn("检测到路径遍历攻击尝试: {}", maskSensitiveContent(input));
                return ValidationResult.failure("输入内容包含潜在的路径遍历攻击", "PATH_TRAVERSAL_DETECTED");
            }
        }

        return ValidationResult.success();
    }

    /**
     * 校验输入是否包含命令注入攻击
     *
     * @param input 输入内容
     * @return 校验结果
     */
    public ValidationResult validateCommandInjection(String input) {
        if (StrUtil.isBlank(input)) {
            return ValidationResult.success();
        }

        for (Pattern pattern : COMMAND_INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                log.warn("检测到命令注入攻击尝试: {}", maskSensitiveContent(input));
                return ValidationResult.failure("输入内容包含潜在的命令注入攻击", "COMMAND_INJECTION_DETECTED");
            }
        }

        return ValidationResult.success();
    }

    /**
     * 校验输入是否包含LDAP注入攻击
     *
     * @param input 输入内容
     * @return 校验结果
     */
    public ValidationResult validateLDAPInjection(String input) {
        if (StrUtil.isBlank(input)) {
            return ValidationResult.success();
        }

        for (Pattern pattern : LDAP_INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                log.warn("检测到LDAP注入攻击尝试: {}", maskSensitiveContent(input));
                return ValidationResult.failure("输入内容包含潜在的LDAP注入攻击", "LDAP_INJECTION_DETECTED");
            }
        }

        return ValidationResult.success();
    }

    /**
     * 校验输入是否包含XML注入攻击
     *
     * @param input 输入内容
     * @return 校验结果
     */
    public ValidationResult validateXMLInjection(String input) {
        if (StrUtil.isBlank(input)) {
            return ValidationResult.success();
        }

        for (Pattern pattern : XML_INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                log.warn("检测到XML注入攻击尝试: {}", maskSensitiveContent(input));
                return ValidationResult.failure("输入内容包含潜在的XML注入攻击", "XML_INJECTION_DETECTED");
            }
        }

        return ValidationResult.success();
    }

    /**
     * 校验文件名是否安全
     *
     * @param filename 文件名
     * @return 校验结果
     */
    public ValidationResult validateFilename(String filename) {
        if (StrUtil.isBlank(filename)) {
            return ValidationResult.failure("文件名不能为空", "FILENAME_EMPTY");
        }

        // 检查文件名长度
        if (filename.length() > 255) {
            return ValidationResult.failure("文件名长度不能超过255个字符", "FILENAME_TOO_LONG");
        }

        // 检查危险字符
        if (ReUtil.contains("[<>:\"|?*\\\\]", filename)) {
            return ValidationResult.failure("文件名包含非法字符", "FILENAME_ILLEGAL_CHARS");
        }

        // 检查路径遍历
        ValidationResult pathResult = validatePathTraversal(filename);
        if (!pathResult.isValid()) {
            return pathResult;
        }

        // 检查危险文件扩展名
        String lowerFilename = filename.toLowerCase();
        for (String ext : DANGEROUS_FILE_EXTENSIONS) {
            if (lowerFilename.endsWith(ext)) {
                log.warn("检测到危险文件扩展名: {}", filename);
                return ValidationResult.failure("不允许上传此类型的文件", "DANGEROUS_FILE_TYPE");
            }
        }

        return ValidationResult.success();
    }

    /**
     * 校验邮箱格式
     *
     * @param email 邮箱地址
     * @return 校验结果
     */
    public ValidationResult validateEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return ValidationResult.failure("邮箱地址不能为空", "EMAIL_EMPTY");
        }

        // 基本格式校验
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!ReUtil.isMatch(emailPattern, email)) {
            return ValidationResult.failure("邮箱格式不正确", "EMAIL_FORMAT_INVALID");
        }

        // 长度校验
        if (email.length() > 254) {
            return ValidationResult.failure("邮箱地址长度不能超过254个字符", "EMAIL_TOO_LONG");
        }

        // 安全校验
        ValidationResult xssResult = validateXSS(email);
        if (!xssResult.isValid()) {
            return xssResult;
        }

        return ValidationResult.success();
    }

    /**
     * 校验用户名格式
     *
     * @param username 用户名
     * @return 校验结果
     */
    public ValidationResult validateUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return ValidationResult.failure("用户名不能为空", "USERNAME_EMPTY");
        }

        // 长度校验
        if (username.length() < 3 || username.length() > 50) {
            return ValidationResult.failure("用户名长度必须在3-50个字符之间", "USERNAME_LENGTH_INVALID");
        }

        // 格式校验：只允许字母、数字、下划线、中划线
        String usernamePattern = "^[a-zA-Z0-9_-]+$";
        if (!ReUtil.isMatch(usernamePattern, username)) {
            return ValidationResult.failure("用户名只能包含字母、数字、下划线和中划线", "USERNAME_FORMAT_INVALID");
        }

        // 不能以数字开头
        if (Character.isDigit(username.charAt(0))) {
            return ValidationResult.failure("用户名不能以数字开头", "USERNAME_START_WITH_DIGIT");
        }

        // 安全校验
        ValidationResult sqlResult = validateSQLInjection(username);
        if (!sqlResult.isValid()) {
            return sqlResult;
        }

        ValidationResult xssResult = validateXSS(username);
        if (!xssResult.isValid()) {
            return xssResult;
        }

        return ValidationResult.success();
    }

    /**
     * 校验密码强度
     *
     * @param password 密码
     * @return 校验结果
     */
    public ValidationResult validatePassword(String password) {
        if (StrUtil.isBlank(password)) {
            return ValidationResult.failure("密码不能为空", "PASSWORD_EMPTY");
        }

        // 长度校验
        if (password.length() < 8 || password.length() > 128) {
            return ValidationResult.failure("密码长度必须在8-128个字符之间", "PASSWORD_LENGTH_INVALID");
        }

        // 复杂度校验
        boolean hasLower = ReUtil.contains("[a-z]", password);
        boolean hasUpper = ReUtil.contains("[A-Z]", password);
        boolean hasDigit = ReUtil.contains("\\d", password);
        boolean hasSpecial = ReUtil.contains("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]", password);

        int complexity = 0;
        if (hasLower) {
            complexity++;
        }
        if (hasUpper) {
            complexity++;
        }
        if (hasDigit) {
            complexity++;
        }
        if (hasSpecial) {
            complexity++;
        }

        if (complexity < 3) {
            return ValidationResult.failure("密码必须包含大写字母、小写字母、数字、特殊字符中的至少3种", "PASSWORD_COMPLEXITY_INSUFFICIENT");
        }

        // 常见弱密码检查
        String[] weakPasswords = {"password", "123456", "12345678", "qwerty", "abc123", "admin", "root"};
        String lowerPassword = password.toLowerCase();
        for (String weak : weakPasswords) {
            if (lowerPassword.contains(weak)) {
                return ValidationResult.failure("密码不能包含常见的弱密码模式", "PASSWORD_TOO_WEAK");
            }
        }

        return ValidationResult.success();
    }

    /**
     * 综合安全校验
     *
     * @param input 输入内容
     * @return 校验结果
     */
    public ValidationResult validateSecurity(String input) {
        if (StrUtil.isBlank(input)) {
            return ValidationResult.success();
        }

        // XSS校验
        ValidationResult xssResult = validateXSS(input);
        if (!xssResult.isValid()) {
            return xssResult;
        }

        // SQL注入校验
        ValidationResult sqlResult = validateSQLInjection(input);
        if (!sqlResult.isValid()) {
            return sqlResult;
        }

        // 路径遍历校验
        ValidationResult pathResult = validatePathTraversal(input);
        if (!pathResult.isValid()) {
            return pathResult;
        }

        // 命令注入校验
        ValidationResult cmdResult = validateCommandInjection(input);
        if (!cmdResult.isValid()) {
            return cmdResult;
        }

        // LDAP注入校验
        ValidationResult ldapResult = validateLDAPInjection(input);
        if (!ldapResult.isValid()) {
            return ldapResult;
        }

        // XML注入校验
        ValidationResult xmlResult = validateXMLInjection(input);
        if (!xmlResult.isValid()) {
            return xmlResult;
        }

        return ValidationResult.success();
    }

    /**
     * 掩码敏感内容（用于日志记录）
     *
     * @param content 敏感内容
     * @return 掩码后的内容
     */
    private String maskSensitiveContent(String content) {
        if (StrUtil.isBlank(content) || content.length() <= 10) {
            return "***";
        }
        return content.substring(0, 5) + "***" + content.substring(content.length() - 5);
    }
}
