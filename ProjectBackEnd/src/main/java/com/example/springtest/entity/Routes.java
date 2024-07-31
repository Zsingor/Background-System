package com.example.springtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Routes {
    private String id;
    private String name;
    private String title;
    private String path;
    private Integer level;
    private String status;
    private String icon;
    private String parentid;
    private Integer position;
    private String type;
    private Date createTime;
    private String creatorId;

    private List<Routes> children;
}
