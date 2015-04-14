package com.example.jum.averagefoodprices;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;

/**
 * Created by James boyer
 */
//This is made so the graph can render the days of the month in the history activity
public class graphformat extends Format {
    String[] labels = null;
    public graphformat(String[] labels){
        this.labels = labels;
    }
    @Override
    public StringBuffer format(Object obj,StringBuffer strB,FieldPosition fp){

        Number num = (Number) obj;
        int i = num.intValue();
        strB.append(labels[i]);
        return strB;
    }
    @Override
    public Object parseObject(String str,ParsePosition pos){
        return Arrays.asList(labels).indexOf(str);
    }
}
