package com.example.springtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    private Integer id;
    private String userId;
    private String contactId;
    private Integer unreadCount;
    private Integer messageId;
    private Date updateTime;
}
