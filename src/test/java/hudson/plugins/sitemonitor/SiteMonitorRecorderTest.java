package hudson.plugins.sitemonitor;

import hudson.plugins.sitemonitor.SiteMonitorRecorder;
import hudson.plugins.sitemonitor.model.Site;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class SiteMonitorRecorderTest extends TestCase {

    private SiteMonitorRecorder builder;

    public void testGetSitesShouldGiveExpectedSites() {
        List<Site> sites = new ArrayList<Site>();
        sites.add(new Site("http://hudson-ci.org"));
        sites.add(new Site("http://hudson.dev.java.net"));
        builder = new SiteMonitorRecorder(sites);
        assertEquals(sites, builder.getSites());
    }
}
