package com.example.springtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Routes {
    String id;
    String name;
    String title;
    String path;
    Integer level;
    String status;
    String icon;
    String parentid;
    Integer position;
    String type;
    List<Routes> children;
}
