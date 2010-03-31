/**
 * Copyright (c) 2009 Cliffano Subagio
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
package hudson.plugins.sitemonitor;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.plugins.sitemonitor.model.Site;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Handles the global and job configuration management.
 * @author cliffano
 */
@Extension
public class SiteMonitorDescriptor extends BuildStepDescriptor<Publisher> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(SiteMonitorDescriptor.class.getName());

    /**
     * Default timeout (in seconds), used when global config timeout setting is
     * not set to any value.
     */
    private static final Integer DEFAULT_TIMEOUT_IN_SECS = 30;

    /**
     * The form validator.
     */
    private SiteMonitorValidator mValidator;

    /**
     * The response codes used to indicate that the web site is up.
     */
    private List<Integer> mSuccessResponseCodes;

    /**
     * The HTTP connection timeout value (in seconds).
     */
    private Integer mTimeout;

    /**
     * Constructs {@link SiteMonitorDescriptor}.
     */
    public SiteMonitorDescriptor() {
        super(SiteMonitorRecorder.class);
        load();
        mValidator = new SiteMonitorValidator();
    }

    /**
     * @return the plugin's display name, used in the job's build drop down list
     */
    @Override
    public final String getDisplayName() {
        return "Monitor Site";
    }

    /**
     * Checks whether this descriptor is applicable.
     * @param clazz
     *            the class
     * @return true
     */
    @Override
    public final boolean isApplicable(
            final Class<? extends AbstractProject> clazz) {
        return true;
    }

    /**
     * @return the success response codes
     */
    public final List<Integer> getSuccessResponseCodes() {
        if (mSuccessResponseCodes == null) {
            mSuccessResponseCodes = new ArrayList<Integer>();
            mSuccessResponseCodes.add(HttpURLConnection.HTTP_OK);
        }
        return mSuccessResponseCodes;
    }

    /**
     * @return the success response codes in comma-separated value format
     */
    public final String getSuccessResponseCodesCsv() {
        StringBuffer sb = new StringBuffer();
        for (Integer successResponseCode : getSuccessResponseCodes()) {
            sb.append(successResponseCode).append(",");
        }
        return sb.toString().replaceFirst(",$", "");
    }

    /**
     * @return the timeout value in seconds
     */
    public final Integer getTimeout() {
        if (mTimeout == null) {
            mTimeout = DEFAULT_TIMEOUT_IN_SECS;
        }
        return mTimeout;
    }

    /**
     * Handles SiteMonitor configuration for each job.
     * @param request
     *            the stapler request
     * @param json
     *            the JSON data containing job configuration values
     * @return the builder with specified sites to be monitored
     */
    @Override
    public final Publisher newInstance(final StaplerRequest request,
            final JSONObject json) {
        LOGGER.fine("json: " + json);

        List<Site> sites = new ArrayList<Site>();

        Object sitesObject = json.get("sites");
        if (sitesObject instanceof JSONObject) {
            for (Object siteObject : json.getJSONObject("sites").values()) {
                String url = String.valueOf(siteObject);
                sites.add(new Site(url));
            }
        } else if (sitesObject instanceof JSONArray) {
            for (Object siteObject : (JSONArray) sitesObject) {
                if (siteObject instanceof JSONObject) {
                    String url = ((JSONObject) siteObject).getString("url");
                    sites.add(new Site(url));
                }
            }
        } else {
            LOGGER.warning("Unable to parse 'sites' object in JSON data. "
                    + "It's neither JSONObject nor JSONArray");
        }
        return new SiteMonitorRecorder(sites);
    }

    /**
     * Handles SiteMonitor global configuration per Hudson instance.
     * @param request
     *            the stapler request
     * @param json
     *            the JSON data containing job configuration values
     * @return true (after configuration is saved)
     */
    @Override
    public final boolean configure(final StaplerRequest request,
            final JSONObject json) {
        LOGGER.fine("json: " + json);

        if (!StringUtils.isBlank(json.getString("successResponseCodes"))) {
            mSuccessResponseCodes.clear();
            for (String responseCode : json.getString("successResponseCodes")
                    .split(",")) {
                mSuccessResponseCodes
                        .add(Integer.parseInt(responseCode.trim()));
            }
        }
        mTimeout = json.getInt("timeout");
        save();
        return true;
    }

    /**
     * @param value
     *            the value to validate
     * @return true if value is a valid URL, false otherwise
     */
    public final FormValidation doCheckUrl(@QueryParameter final String value) {
        return mValidator.validateUrl(value);
    }

    /**
     * @param value
     *            the value to validate
     * @return true if value is a valid comma-separated response codes, false
     *         otherwise
     */
    public final FormValidation doCheckResponseCodes(
            @QueryParameter final String value) {
        return mValidator.validateResponseCodes(value);
    }

    /**
     * @param value
     *            the value to validate
     * @return true if value is a valid timeout, false otherwise
     */
    public final FormValidation doCheckTimeout(
            @QueryParameter final String value) {
        return mValidator.validateTimeout(value);
    }
}
