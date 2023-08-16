package com.example.plugin;

import java.util.List;
import java.util.Map;

public class FeatureContent {

    private static Map<String, List<Object>> content;

    public static Map<String, List<Object>> getContent() {
        return content;
    }

    public static void setContent(Map<String, List<Object>> content) {
        FeatureContent.content = content;
    }



}
