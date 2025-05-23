package com.example.projectbackend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class Roles {
    private String id;
    private String name;
    private String description;
    private List<String> routesid;
    private Date createTime;
    private String creatorId;
}
