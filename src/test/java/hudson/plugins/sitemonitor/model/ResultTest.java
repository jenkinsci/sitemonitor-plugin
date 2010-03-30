package hudson.plugins.sitemonitor.model;

import java.net.HttpURLConnection;

import junit.framework.TestCase;

public class ResultTest extends TestCase {

    private Result result;

    public void setUp() {
        result = new Result(new Site("http://hudson-ci.org"),
                HttpURLConnection.HTTP_OK, Status.DOWN, "some note");
    }

    public void testGetSiteShoudGiveExpectedValue() {
        assertEquals("http://hudson-ci.org", result.getSite().getUrl());
    }

    public void testGetResponseCodeShouldGiveExpectedValue() {
        assertEquals(new Integer(HttpURLConnection.HTTP_OK), result
                .getResponseCode());
    }

    public void testGetStatusShouldGiveExpectedValue() {
        assertEquals(Status.DOWN, result.getStatus());
    }
    
    public void testGetNoteShouldGiveExpectedValue() {
        assertEquals("some note", result.getNote());
    }
}
