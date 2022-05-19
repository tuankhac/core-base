package com.vmo.core.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ConverterSqlUtils {
    public static String convertListStringToStringQuery(List<String> strs) {
        List<String> newStrs = new ArrayList<>();
        for (String str : strs) {
            newStrs.add("'" + str + "'");
        }
        String content = newStrs.toString();
        content = "(" + content.substring(1);
        content = content.substring(0, content.length() - 1) + ")";
        return content;
    }

    public static String convertListStringToFloatQuery(List<Float> floats) {
        String content = floats.toString();
        content = "(" + content.substring(1);
        content = content.substring(0, content.length() - 1) + ")";
        return content;
    }

    public static String convertListStringToLongQuery(List<Long> longs) {
        String content = longs.toString();
        content = "(" + content.substring(1);
        content = content.substring(0, content.length() - 1) + ")";
        return content;
    }

    public static String convertStringToStringQuery(String value) {
        if (value != null && value.contains("'")) {
            value = value.replace("'", "''");
        }
        return "'" + value + "'";
    }

    public static String convertStringToStringQueryLike(String value) {
        return "'%" + value + "%'";
    }

    public static String convertNumberToStringQuery(long value) {
        return "'" + value + "'";
    }

    public static String convertNumberToStringQuery(double value) {
        return "'" + value + "'";
    }
}
