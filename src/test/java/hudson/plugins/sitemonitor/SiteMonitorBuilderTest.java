package hudson.plugins.sitemonitor;

import hudson.plugins.sitemonitor.SiteMonitorBuilder;
import hudson.plugins.sitemonitor.model.Site;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class SiteMonitorBuilderTest extends TestCase {

    private SiteMonitorBuilder builder;

    public void testGetSitesShouldGiveExpectedSites() {
        List<Site> sites = new ArrayList<Site>();
        sites.add(new Site("http://hudson-ci.org"));
        sites.add(new Site("http://hudson.dev.java.net"));
        builder = new SiteMonitorBuilder(sites);
        assertEquals(sites, builder.getSites());
    }
}
