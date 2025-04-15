package com.example.projectbackend.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListAndString {
    public static List<String> StringToList(String str) {
        if (str == null || str.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(str.split(","))
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    public static String ListToString(List<String> lis) {
        return lis.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
