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

import hudson.util.FormValidation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * This class provides validation functions.
 * @author cliffano
 */
public class SiteMonitorValidator {

    /**
     * Validates a URL.
     * @param url
     *            the web site URL
     * @return false when URL is malformed, true otherwise
     */
    public final FormValidation validateUrl(final String url) {
        FormValidation validation = FormValidation.ok();
        if (StringUtils.isNotBlank(url)) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                try {
                    new URL(url);
                } catch (MalformedURLException mue) {
                    validation = FormValidation.error("URL is malformed");
                }
            } else {
                validation = FormValidation
                        .error("URL must start with http:// or https://");
            }
        }
        return validation;
    }

    /**
     * Validates HTTP connection timeout value.
     * @param timeout
     *            the time out value in seconds
     * @return true when timeout value is valid, false otherwise
     */
    public final FormValidation validateTimeout(final String timeout) {
        FormValidation validation = FormValidation.ok();
        if (StringUtils.isBlank(timeout)) {
            validation = FormValidation.error("Timeout value must be provided");
        } else if (!NumberUtils.isDigits(timeout)) {
            validation = FormValidation.error("Timeout value must be a number");
        }
        return validation;
    }

    /**
     * Validates a comma-separated HTTP response codes.
     * @param responseCodes
     *            the response codes
     * @return true if all response codes are valid, false otherwise
     */
    public final FormValidation validateResponseCodes(
            final String responseCodes) {
        FormValidation validation = FormValidation.ok();
        List<String> invalidResponseCodes = new ArrayList<String>();
        if (StringUtils.isNotBlank(responseCodes)) {
            for (String responseCode : responseCodes.split(",")) {
                if (!NumberUtils.isDigits(responseCode.trim())) {
                    invalidResponseCodes.add(responseCode);
                }
            }
        }
        if (!invalidResponseCodes.isEmpty()) {
            StringBuffer errorMessage = new StringBuffer(
                    "Invalid response code(s): ");
            for (String invalidResponseCode : invalidResponseCodes) {
                errorMessage.append(invalidResponseCode).append(" ");
            }
            validation = FormValidation.error(errorMessage.toString());
        }
        return validation;
    }
}
