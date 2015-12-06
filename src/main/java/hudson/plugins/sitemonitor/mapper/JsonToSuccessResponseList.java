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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

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
                if (NumberUtils.isDigits(responseCode)) {
                    successResponseCodes.add(Integer.parseInt(responseCode.trim()));
                }
            }
        }
        
        return successResponseCodes;
    }

}
