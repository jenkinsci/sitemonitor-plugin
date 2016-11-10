package hudson.plugins.sitemonitor.mapper;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SuccessCodeListToCvStringTest {

    @Test
    public void apply_withOneItem() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);

        String result = SuccessCodeListToCvString.INSTANCE.apply(list);
        Assert.assertEquals("1", result);
    }

    @Test
    public void apply_withTwoItems() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);

        String result = SuccessCodeListToCvString.INSTANCE.apply(list);
        Assert.assertEquals("1,2", result);
    }

    @Test
    public void apply_withEmptyList() {
        List<Integer> list = new ArrayList<Integer>();
        String result = SuccessCodeListToCvString.INSTANCE.apply(list);
        Assert.assertEquals("", result);
    }

    @Test
    public void apply_avoidNullPointer() {
        String result = SuccessCodeListToCvString.INSTANCE.apply(null);
        Assert.assertEquals("", result);
    }

}