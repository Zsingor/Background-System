package com.example.springtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class User
{
    private Integer currentpage;
    private Integer pagesize;
    private Integer id;
    private String name;
    private String password;
    private Integer roleid;
    private String description;
    private String status;

    private String sex;
    private String address;
    private String createdate;
    private String startdate;
    private String enddate;
}
