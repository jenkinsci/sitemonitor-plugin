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

import hudson.Launcher;
import hudson.ProxyConfiguration;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.plugins.sitemonitor.model.Result;
import hudson.plugins.sitemonitor.model.Site;
import hudson.plugins.sitemonitor.model.Status;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;

/**
 * Performs the web site monitoring process.
 * @author cliffano
 */
public class SiteMonitorRecorder extends Recorder {

    /**
     * 1 sec = 1000 msecs .
     */
    private static final int MILLISECS_IN_SECS = 1000;

    /**
     * The list of web sites to monitor.
     */
    private List<Site> mSites;

    /**
     * Construct {@link SiteMonitorRecorder}.
     * @param sites
     *            the list of web sites to monitor
     */
    public SiteMonitorRecorder(final List<Site> sites) {
        mSites = sites;
    }

    /**
     * @return the list of web sites to monitor
     */
    public final List<Site> getSites() {
        return mSites;
    }

    // accepts any cert, based on http://stackoverflow.com/questions/1828775/httpclient-and-ssl
    private static class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private HttpURLConnection getConnection(String urlString)
            throws MalformedURLException, IOException, NoSuchAlgorithmException, KeyManagementException {
        if (urlString.startsWith("https://")) {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
            SSLContext.setDefault(ctx);

            HttpsURLConnection connection = (HttpsURLConnection) ProxyConfiguration.open(new URL(urlString));
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            return connection;
        }else if(urlString.contains("@")){
            URL passedURL = new URL(urlString);
			String creds = urlString.substring(urlString.indexOf("//")+2, urlString.indexOf("@"));
			String userName = creds.substring(0,creds.indexOf(":"));
			String passWord = creds.substring(creds.indexOf(":")+1,creds.length());
			String userPassword = userName + ":" + passWord;
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			HttpURLConnection connection = (HttpURLConnection) passedURL.openConnection();
			connection.setRequestProperty ("Authorization", "Basic " + encoding);
			return connection;
        }
        
        else {
            return (HttpURLConnection) ProxyConfiguration.open(new URL(urlString));
        }
    }

    /**
     * Performs the web site monitoring by checking the response code of the
     * site's URL.
     * @param build
     *            the build
     * @param launcher
     *            the launcher
     * @param listener
     *            the listener
     * @return true if all sites give success response codes, false otherwise
     * @throws InterruptedException
     *             when there's an interruption
     * @throws IOException
     *             when there's an IO error
     */
    @Override
    public final boolean perform(final AbstractBuild<?, ?> build,
            final Launcher launcher, final BuildListener listener)
            throws InterruptedException, IOException {
        List<Result> results = new ArrayList<Result>();
        SiteMonitorDescriptor descriptor = (SiteMonitorDescriptor)
                getDescriptor();

        boolean hasFailure = false;
        for (Site site : mSites) {

            Integer responseCode = null;
            Status status;
            String note = "";
            HttpURLConnection connection = null;

            try {
                connection = getConnection(site.getUrl());
                connection.setConnectTimeout(descriptor.getTimeout()
                        * MILLISECS_IN_SECS);
                responseCode = connection.getResponseCode();

                List<Integer> successResponseCodes = descriptor
                        .getSuccessResponseCodes();
                if (successResponseCodes.contains(responseCode)) {
                    status = Status.UP;
                } else {
                    status = Status.ERROR;
                }
            } catch (SocketTimeoutException ste) {
                listener.getLogger().println(ste + " - " + ste.getMessage());
                status = Status.DOWN;
            } catch (Exception e) {
                note = e + " - " + e.getMessage();
                listener.getLogger().println(note);
                status = Status.EXCEPTION;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            note = "[" + status + "] " + note;
            listener.getLogger().println(
                    Messages.SiteMonitor_Console_URL() + site.getUrl() + ", " +
                    Messages.SiteMonitor_Console_ResponseCode() + responseCode + ", " +
                    Messages.SiteMonitor_Console_Status() + status);

            if (!hasFailure && status != Status.UP) {
                hasFailure = true;
            }

            Result result = new Result(site, responseCode, status, note);
            results.add(result);
        }

        build.addAction(new SiteMonitorRootAction(results));
        hudson.model.Result result;
        if (hasFailure) {
            result = hudson.model.Result.FAILURE;
        } else {
            result = hudson.model.Result.SUCCESS;
        }
        build.setResult(result);
        
        // the return value is not used when this class implements a Recorder,
        // it's left here just in case this class switches to a Builder.
        // http://n4.nabble.com/how-can-a-Recorder-mark-build-as-failure-td1746654.html
        return !hasFailure;
    }
    
    /**
     * Gets the required monitor service.
     * @return the BuildStepMonitor
     */
    public final BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
}
