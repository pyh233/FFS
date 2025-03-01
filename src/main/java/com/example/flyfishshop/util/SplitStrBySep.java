package com.example.flyfishshop.util;

import java.util.ArrayList;
import java.util.List;

public class SplitStrBySep {
    public static List<String> split(String str, String sep) {
        List<String> list = new ArrayList<String>();
        str = str.trim();
        sep = sep.trim();
        if (str.length() == 0 || sep.length() == 0) {
            return list;
        }
        return List.of(str.split(sep));
    }
}
