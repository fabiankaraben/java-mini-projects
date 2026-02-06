package com.fabiankaraben.queryparser;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QueryParser {

    public Map<String, String> parse(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = "";
            if (keyValue.length > 1) {
                value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
            }
            result.put(key, value);
        }

        return result;
    }
}
