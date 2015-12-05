package hudson.plugins.sitemonitor.mapper;

import java.util.List;

import com.google.common.base.Function;

/**
 * Transform a list of integers to a string comma-separated
 * 
 * @author onuba
 *
 */
public enum SuccessCodeListToCvString implements Function<List<Integer>, String> {

    INSTANCE;

    public String apply(List<Integer> list) {

        StringBuffer sb = new StringBuffer();
        for (Integer successResponseCode : list) {
            sb.append(successResponseCode).append(",");
        }
        return sb.toString().replaceFirst(",$", "");
        
        
    }
}
