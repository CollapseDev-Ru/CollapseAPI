package ru.collapsedev.collapseapi.util;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ParametersUtil {

    public Map<String, String> parseParams(String line) {
        String[] args = line.split(" ");
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("--")) {
                params.put(args[i].substring(2), args[i + 1]);
            }
        }
        return params;
    }
}
