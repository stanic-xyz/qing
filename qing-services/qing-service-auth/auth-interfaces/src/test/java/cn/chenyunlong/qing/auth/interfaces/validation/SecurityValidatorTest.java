package cn.chenyunlong.qing.auth.interfaces.validation;

import cn.chenyunlong.common.validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityValidatorTest {

    private SecurityValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SecurityValidator();
    }

    @Test
    @DisplayName("XSS校验：检测<script>标签")
    void testXssScriptTag() {
        ValidationResult result = validator.validateXSS("<script>alert('xss')</script>");
        assertFalse(result.isValid());
        assertEquals("XSS_ATTACK_DETECTED", result.getCode());
    }

    @Test
    @DisplayName("SQL注入校验：检测OR 1=1 注入")
    void testSqlInjectionOrTrue() {
        ValidationResult result = validator.validateSQLInjection("admin' OR '1'='1");
        assertFalse(result.isValid());
        assertEquals("SQL_INJECTION_DETECTED", result.getCode());
    }

    @Test
    @DisplayName("路径遍历校验：检测..\\../")
    void testPathTraversal() {
        ValidationResult result = validator.validatePathTraversal("../../etc/passwd");
        assertFalse(result.isValid());
        assertEquals("PATH_TRAVERSAL_DETECTED", result.getCode());
    }

    @Test
    @DisplayName("命令注入校验：检测; rm -rf")
    void testCommandInjection() {
        ValidationResult result = validator.validateCommandInjection("test; rm -rf / ");
        assertFalse(result.isValid());
        assertEquals("COMMAND_INJECTION_DETECTED", result.getCode());
    }

    @Test
    @DisplayName("LDAP注入校验：检测(|(uid=*))")
    void testLdapInjection() {
        ValidationResult result = validator.validateLDAPInjection("(|(uid=*))(sn=*))");
        assertFalse(result.isValid());
        assertEquals("LDAP_INJECTION_DETECTED", result.getCode());
    }

    @Test
    @DisplayName("XML注入校验：检测<!DOCTYPE xx [<!ENTITY ...]>")
    void testXmlInjection() {
        String payload = "<?xml version=\"1.0\"?><!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///etc/passwd\">]><foo>&xxe;</foo>";
        ValidationResult result = validator.validateXMLInjection(payload);
        assertFalse(result.isValid());
        assertEquals("XML_INJECTION_DETECTED", result.getCode());
    }

    @Test
    @DisplayName("综合安全校验：正常字符串允许通过")
    void testSecurityPass() {
        ValidationResult result = validator.validateSecurity("normal-safe-string_123");
        assertTrue(result.isValid());
        assertNull(result.getCode());
    }
}
