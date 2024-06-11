package com.example.springtest.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class Roles {
    private Integer currentpage;
    private Integer pagesize;
    private Integer id;
    private String name;
    private String description;
    private String routesid;

    public List<String> getRoutesIdAsList() {
        if (routesid == null || routesid.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(routesid.split(","))
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    public void setRoutesIdFromList(List<String> routeIds) {
        this.routesid = routeIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
