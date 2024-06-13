package com.example.springtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logs {
    private String id;
    private String userid;
    private String username;
    private String roleid;
    private String module;
    private String operate;
    private String details;
    private String ip;
    private Date operatedate;
    private Date startDate;
    private Date endDate;
}
