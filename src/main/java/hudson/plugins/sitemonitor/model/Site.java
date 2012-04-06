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
package hudson.plugins.sitemonitor.model;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * This class keeps the details of the web site to be monitored.
 * @author cliffano
 */
public class Site {

    /**
     * The web site URL.
     */
    private String mUrl;

    /**
     * The regular expression to check.
     */
    private String mRegularExpression;
    
    /**
     * The pattern for the expression to check.
     */
    transient private Pattern mRegularExpressionPattern;

    /**
     * The flag for checking the regex.
     */
    private boolean mFailWhenRegexNotFound = true;

    /**
     * Constructs a Site with specified details.
     * @param url
     *            the web site URL
     * @param regularExpression
     *            the regular expression to check
     * @param failWhenRegexNotFound
     *            the flag for checking the regex
     */
    public Site(final String url, final String regularExpression, final boolean failWhenRegexNotFound) {
        mUrl = url;
        mRegularExpression = regularExpression;
        mFailWhenRegexNotFound = failWhenRegexNotFound;
        setRegexPattern();
    }

    /**
     * Constructs a Site with specified details.
     * @param url
     *            the web site URL
     */
    public Site(final String url) {
        mUrl = url;
    }

    /**
     * Sets the regex pattern based on the regex.
     */
    private void setRegexPattern() {
        if (!StringUtils.isEmpty(mRegularExpression)) {
            mRegularExpressionPattern = Pattern.compile(mRegularExpression);
        } else {
            mRegularExpressionPattern = null;
        }
    }

    /**
     * Part of the serialization strategy.
     * @return this
     */
    Object readResolve() {
        setRegexPattern();
        return this;
    }
    
    /**
     * @return the web site URL
     */
    public final String getUrl() {
        return mUrl;
    }

    /**
     * @return the regular expression to check
     */
    public final String getRegularExpression() {
        return mRegularExpression;
    }

    /**
     * @return the Pattern for the regular expression to check
     */
    public final Pattern getRegularExpressionPattern() {
        return mRegularExpressionPattern;
    }

    /**
     * @return the flag for checking the regex
     */
    public final boolean isFailWhenRegexNotFound() {
        return mFailWhenRegexNotFound;
    }
}
