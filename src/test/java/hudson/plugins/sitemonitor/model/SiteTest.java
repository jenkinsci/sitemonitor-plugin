package hudson.plugins.sitemonitor.model;

import hudson.plugins.sitemonitor.model.Site;
import junit.framework.TestCase;

public class SiteTest extends TestCase {

    private Site site;

    public void testGetUrlShouldGiveExpectedUrlValue() {
        site = Site.builder("http://hudson-ci.org").build();
        assertEquals("http://hudson-ci.org", site.getUrl());
    }
}
