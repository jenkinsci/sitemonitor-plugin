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

import hudson.model.RootAction;
import hudson.plugins.sitemonitor.model.Result;

import java.util.List;

/**
 * This class keeps the site monitoring results to be used in the report page.
 * Display name, icon file name, and URL name are used to construct the root
 * menu on the left side of a build page.
 * @author cliffano
 */
public class SiteMonitorRootAction implements RootAction {

    /**
     * Site monitoring results.
     */
    private List<Result> mResults;

    /**
     * Constructs {@link SiteMonitorRootAction}.
     * @param results
     *            site monitoring results
     */
    public SiteMonitorRootAction(final List<Result> results) {
        mResults = results;
    }

    /**
     * @return the text of site monitor link on the left menu on build page.
     */
    public final String getDisplayName() {
        return "Site Monitor";
    }

    /**
     * @return the icon of site monitor link on the left menu on build page.
     */
    public final String getIconFileName() {
        return "/plugin/sitemonitor/images/icon.png";
    }

    /**
     * @return the URL of site monitor link on the left menu on build page.
     */
    public final String getUrlName() {
        return "sitemonitor";
    }

    /**
     * @return site monitoring results
     */
    public final List<Result> getResults() {
        return mResults;
    }
}
