package hudson.plugins.sitemonitor.mapper;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Transform a JSONObject into a list of integer, used in success code response
 * 
 * @author onuba
 *
 */
public enum JsonToSuccessResponseList implements Function<JSONObject, List<Integer>> {

    INSTANCE;

    public List<Integer> apply(JSONObject json) {

        final List<Integer> successResponseCodes = Lists.newArrayList();

        if (!StringUtils.isBlank(json.getString("successResponseCodes"))) {

            for (String responseCode : json.getString("successResponseCodes").split(",")) {
                successResponseCodes.add(Integer.parseInt(responseCode.trim()));
            }
        }
        
        return successResponseCodes;
    }

}
