package hudson.plugins.sitemonitor.model;

import hudson.plugins.sitemonitor.model.Site;
import junit.framework.TestCase;

public class SiteTest extends TestCase {

    private Site site;

    public void testGetUrlShouldGiveExpectedUrlValue() {
        site = new Site("http://hudson-ci.org");
        assertEquals("http://hudson-ci.org", site.getUrl());
    }

    public void testGetRegularExpressionShouldGiveExpectedUrlValue() {
        site = new Site(null, "regex1", false);
        assertEquals("regex1", site.getRegularExpression());
        assertEquals("regex1", site.getRegularExpressionPattern().pattern());
    }
    
    public void testGetRegularExpressionFlagShouldGiveExpectedUrlValue() {
        site = new Site(null, null, false);
        assertEquals(false, site.isFailWhenRegexNotFound());
    }

}
