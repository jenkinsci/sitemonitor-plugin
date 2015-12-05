package hudson.plugins.sitemonitor.mapper;

import hudson.plugins.sitemonitor.model.Site;
import hudson.plugins.sitemonitor.model.Site.SiteBuilder;
import net.sf.json.JSONObject;

import com.google.common.base.Function;

/**
 * Transform a json site information into Site object
 * @author fhernandez
 *
 */
public enum JsonToSiteMapper implements Function<JSONObject, Site>{

    INSTANCE;

    public Site apply(JSONObject json) {
        
        final SiteBuilder siteBuilder = Site.builder(((JSONObject) json).getString("url")).
                timeout(((JSONObject) json).getInt("timeout")).
                successResponseCodes(JsonToSuccessResponseList.INSTANCE.apply((JSONObject) json));
        
        return siteBuilder.build();
    }
    
}
