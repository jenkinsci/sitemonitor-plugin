package hudson.plugins.sitemonitor;

import hudson.plugins.sitemonitor.SiteMonitorValidator;
import hudson.util.FormValidation;
import junit.framework.TestCase;

public class SiteMonitorValidatorTest extends TestCase {

    private SiteMonitorValidator validator;

    public void setUp() {
        validator = new SiteMonitorValidator();
    }

    public void testValidateUrlWithValidUrlAndHttpProtocolShouldGiveOk() {
        FormValidation validation = validator
                .validateUrl("http://hudson-ci.org");
        assertEquals(FormValidation.Kind.OK, validation.kind);
    }
    
    public void testValidateUrlWithValidUrlAndHttpsProtocolShouldGiveOk() {
        FormValidation validation = validator
                .validateUrl("https://hudson-ci.org");
        assertEquals(FormValidation.Kind.OK, validation.kind);
    }

    public void testValidateUrlWithInvalidUrlShouldGiveError() {
        FormValidation validation = validator.validateUrl("://hudson-ci.org");
        assertEquals(FormValidation.Kind.ERROR, validation.kind);
    }

    public void testValidateUrlWithEmptyValueShouldGiveOk() {
        FormValidation validation = validator.validateUrl("");
        assertEquals(FormValidation.Kind.OK, validation.kind);
    }

    public void testValidateUrlWithInvalidProtocolShouldGiveError() {
        FormValidation validation = validator.validateUrl("ftp://hudson-ci.org");
        assertEquals(FormValidation.Kind.ERROR, validation.kind);
    }
    
    public void testValidateTimeoutWithNumericValueShouldGiveOk() {
        FormValidation validation = validator.validateTimeout("300");
        assertEquals(FormValidation.Kind.OK, validation.kind);
    }

    public void testValidateTimeoutWithNonNumericValueShouldGiveError() {
        FormValidation validation = validator.validateTimeout("abc^&@%!#");
        assertEquals(FormValidation.Kind.ERROR, validation.kind);
    }

    public void testValidateTimeoutWithEmptyValueShouldGiveError() {
        FormValidation validation = validator.validateTimeout("");
        assertEquals(FormValidation.Kind.ERROR, validation.kind);
    }

    public void testValidateResponseCodesWithValidResponseCodesShouldGiveOk() {
        FormValidation validation = validator.validateResponseCodes("200,304");
        assertEquals(FormValidation.Kind.OK, validation.kind);
    }

    public void testValidateResponseCodesWithValidResponseCodesHavingLeadingTrailingSpacesShouldGiveOk() {
        FormValidation validation = validator
                .validateResponseCodes("200 ,304, 500,400");
        assertEquals(FormValidation.Kind.OK, validation.kind);
    }

    public void testValidateResponseCodesWithEmptyValueShouldGiveOk() {
        FormValidation validation = validator.validateResponseCodes("");
        assertEquals(FormValidation.Kind.OK, validation.kind);
    }

    public void testValidateResponseCodesWithInvalidResponseCodesShouldGiveError() {
        FormValidation validation = validator.validateResponseCodes("b200,304");
        assertEquals(FormValidation.Kind.ERROR, validation.kind);
    }
}
