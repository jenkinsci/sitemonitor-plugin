package hudson.plugins.sitemonitor;

import hudson.plugins.sitemonitor.model.Result;
import hudson.plugins.sitemonitor.model.Site;
import hudson.plugins.sitemonitor.model.Status;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class SiteMonitorRootActionTest extends TestCase {

    private SiteMonitorRootAction action;

    public void setUp() {
        List<Result> results = new ArrayList<Result>();
        Result result1 = new Result(new Site("http://hudson-ci.org"),
                HttpURLConnection.HTTP_OK, Status.UP, "some note 1");
        Result result2 = new Result(new Site("http://blah"),
                HttpURLConnection.HTTP_BAD_GATEWAY, Status.ERROR, "some note 2");
        results.add(result1);
        results.add(result2);
        action = new SiteMonitorRootAction(results);
    }

    public void testGetResultsShouldGiveExpectedResults() {
        List<Result> results = action.getResults();
        assertEquals("http://hudson-ci.org", results.get(0).getSite().getUrl());
        assertEquals(new Integer(HttpURLConnection.HTTP_OK), results.get(0)
                .getResponseCode());
        assertEquals(Status.UP, results.get(0).getStatus());
        assertEquals("some note 1", results.get(0).getNote());
        assertEquals("http://blah", results.get(1).getSite().getUrl());
        assertEquals(new Integer(HttpURLConnection.HTTP_BAD_GATEWAY), results
                .get(1).getResponseCode());
        assertEquals(Status.ERROR, results.get(1).getStatus());
        assertEquals("some note 2", results.get(1).getNote());
    }

    public void testGetDisplayNameShouldGiveExpectedValue() {
        assertEquals("Site Monitor", action.getDisplayName());
    }

    public void testGetIconFileNameShouldGiveExpectedValue() {
        assertEquals("/plugin/sitemonitor/images/icon.png", action
                .getIconFileName());
    }

    public void testGetUrlNameShouldGiveExpectedValue() {
        assertEquals("sitemonitor", action.getUrlName());
    }
}
