package com.example.springtest.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class Roles {
    private Integer currentpage;
    private Integer pagesize;
    private Integer id;
    private String name;
    private String description;
    private List<String> routesid;
}
