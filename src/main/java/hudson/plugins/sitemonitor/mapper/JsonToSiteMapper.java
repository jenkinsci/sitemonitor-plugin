/**
 * Copyright (c) 2015 Francisco Hernandez Suarez
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
package hudson.plugins.sitemonitor.mapper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.google.common.base.Function;

import hudson.plugins.sitemonitor.model.Site;
import hudson.plugins.sitemonitor.model.Site.SiteBuilder;
import net.sf.json.JSONObject;

/**
 * Transform a json site information into Site object
 * @author onuba
 *
 */
public enum JsonToSiteMapper implements Function<JSONObject, Site>{

    INSTANCE;

    public Site apply(JSONObject json) {
        
        if (json != null) {
            String url = json.getString("url");
            
            if (!url.startsWith("http://") && !url.startsWith("https://") && !url.contains("${")) {
                url = "http://" + url;
            }
            
            final SiteBuilder siteBuilder = Site.builder(url);
            
            if (!StringUtils.isBlank(json.getString("timeout")) && NumberUtils.isDigits(json.getString("timeout"))) {
                siteBuilder.timeout(((JSONObject) json).getInt("timeout"));
            }
            
            siteBuilder.successResponseCodes(JsonToSuccessResponseList.INSTANCE.apply((JSONObject) json));
            
            siteBuilder.admitInsecureSslCerts(json.containsKey("admitInsecureSslCerts") && json.getBoolean("admitInsecureSslCerts"));
            
            return siteBuilder.build();
        }
        
        return null;
        
    }
    
}
