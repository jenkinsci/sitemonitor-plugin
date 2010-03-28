package hudson.plugins.sitemonitor.model;

import hudson.plugins.sitemonitor.model.Site;
import junit.framework.TestCase;

public class SiteTest extends TestCase {

    private Site site;

    public void testGetUrlShouldGiveExpectedUrlValue() {
        site = new Site("http://hudson-ci.org");
        assertEquals("http://hudson-ci.org", site.getUrl());
    }
}
