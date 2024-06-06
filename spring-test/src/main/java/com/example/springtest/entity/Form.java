package com.example.springtest.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class Form {
    Integer currentpage;
    Integer pagesize;
    User queryData;
}
