/**
 * Copyright (c) 2009 Cliffano Subagio, Copyright (c) 2015 Francisco Hernandez Suarez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.sitemonitor.model;

import hudson.plugins.sitemonitor.mapper.SuccessCodeListToCvString;

import java.util.List;

/**
 * This class keeps the details of the web site to be monitored.
 * @author cliffano
 * @author onuba 
 */
public class Site {

    /**
     * The web site URL.
     */
    private String mUrl;
    
    /**
     * Timeout for this site, if there is no any timeout the process will relay 
     * in global configutrations.
     */
    private Integer timeout;
    
    /**
     * Success Response codes for this site, if there is no any success code the process will relay 
     * in global configutrations.
     */
    private List<Integer> successResponseCodes;

    /**
     * Constructs a Site with specified details.
     * @param url
     *            the web site URL
     * @param timeout
     *            the timeout for this site, if there is not timeout the process will use 
     *            the global confguration
     * @param url
     *            the list of success codes for this site, if there is not success list the 
     *            process will use the global confguration
     */
    private Site(SiteBuilder builder) {
        this.mUrl = builder.url;
        this.timeout = builder.timeout;
        this.successResponseCodes = builder.successResponseCodes;
    }

    /**
     * @return the web site URL
     */
    public final String getUrl() {
        return mUrl;
    }

	/**
	 * @return the timeout
	 */
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * @return the successResponseCodes
	 */
	public List<Integer> getSuccessResponseCodes() {
		return successResponseCodes;
	}
	
	/**
     * @return the success response codes in comma-separated value format, used by jelly
     */
    public final String getSuccessResponseCodesCsv() {
        
        return SuccessCodeListToCvString.INSTANCE.apply(successResponseCodes);
    }
	
	public static SiteBuilder builder(String url) {
	    return new SiteBuilder(url);
	}
	
	/**
	 * Builder class for Site class
	 * 
	 * @author onuba
	 *
	 */
	public static class SiteBuilder {
	    
	    private final String url;
	    
	    private Integer timeout;
	    
	    private List<Integer> successResponseCodes;
	    
	    private SiteBuilder(String url) {
	        this.url = url;
	    }
	    
	    public SiteBuilder timeout(int timeout) {
	        this.timeout = timeout;
	        
	        return this;
	    }
	    
	    public SiteBuilder successResponseCodes(List<Integer> successResponseCodes) {
            this.successResponseCodes = successResponseCodes;
            
            return this;
        }
	    
	    public Site build() {
	        return new Site(this);
	    }
	}
}
