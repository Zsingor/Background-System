package com.example.springtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class User
{
    private String id;
    private String name;
    private String password;
    private String email;
    private String description;
    private String status;
    private List<String> rolesid;
}
