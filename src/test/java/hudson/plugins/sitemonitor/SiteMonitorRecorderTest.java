package hudson.plugins.sitemonitor;

import hudson.plugins.sitemonitor.SiteMonitorRecorder;
import hudson.plugins.sitemonitor.model.Site;
import hudson.plugins.sitemonitor.model.Status;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    
    private class MockSMD extends SiteMonitorDescriptor {
    	public MockSMD(List<Integer> okCodes, Integer timeout) {
    		super(okCodes, timeout);
    	}
    }
    
    public void testLaunch() throws Exception {
        List<Site> sites = new ArrayList<Site>();
        Site site0 = new Site("http://hudson-ci.org");
		sites.add(site0);
        builder = new SiteMonitorRecorder(sites);
        
        List<Integer> okCodes = Arrays.asList(200);
        Integer timeout;
        SiteMonitorDescriptor descriptor;
        
        timeout = 1;
        descriptor = new MockSMD(okCodes, timeout);
        
        try {
        	Future<Status> f = builder.launchCheck(site0, descriptor);
        	Status s = f.get(1, TimeUnit.MICROSECONDS);
        } catch (Exception e) {
        	assertTrue("Expected exception", e instanceof TimeoutException);
        }
        
        timeout = 30;
        descriptor = new MockSMD(okCodes, timeout);

    	Future<Status> f = builder.launchCheck(site0, descriptor);
    	Status s = f.get(5, TimeUnit.MINUTES);
    	assertTrue("got result within 5 minutes", true);
    	
    }
    
    public void testLaunch_badUrl() throws Exception {
        List<Site> sites = new ArrayList<Site>();
        Site site0 = new Site("http://hudson-ci.org/bogusURL");
		sites.add(site0);
        builder = new SiteMonitorRecorder(sites);
        
        List<Integer> okCodes = Arrays.asList(200);
        Integer timeout;
        SiteMonitorDescriptor descriptor;
        
        timeout = 1;
        descriptor = new MockSMD(okCodes, timeout);
        
        try {
        	Future<Status> f = builder.launchCheck(site0, descriptor);
        	Status s = f.get(1, TimeUnit.MICROSECONDS);
        	assertFalse("got result from bad URL", true);
        } catch (Exception e) {
        	assertTrue("Expected exception", e instanceof TimeoutException);
        }
        
        timeout = 30;
        descriptor = new MockSMD(okCodes, timeout);

    	Future<Status> f = builder.launchCheck(site0, descriptor);
    	Status s = f.get(5, TimeUnit.MINUTES);
    	assertTrue("got result within 5 minutes", true);
    	
    }

    public void testLaunch_badHost() throws Exception {
        List<Site> sites = new ArrayList<Site>();
        Site site0 = new Site("http://no.such.host.hudson-ci.org/");
		sites.add(site0);
        builder = new SiteMonitorRecorder(sites);
        
        List<Integer> okCodes = Arrays.asList(200);
        Integer timeout;
        SiteMonitorDescriptor descriptor;
        
        timeout = 1;
        descriptor = new MockSMD(okCodes, timeout);
        
        try {
        	Future<Status> f = builder.launchCheck(site0, descriptor);
        	Status s = f.get(1, TimeUnit.MICROSECONDS);
        } catch (Exception e) {
        	assertTrue("Expected exception", e instanceof TimeoutException);
        }
        
        timeout = 30;
        descriptor = new MockSMD(okCodes, timeout);

        try {
        	Future<Status> f = builder.launchCheck(site0, descriptor);
        	Status s = f.get(5, TimeUnit.MINUTES);
        	assertFalse("got result from bad URL", true);
        } catch (Exception e) {
        	assertTrue("Expected exception", e instanceof ExecutionException);
        	assertTrue("Expected cause", e.getCause() instanceof UnknownHostException);
        }
    }

}
