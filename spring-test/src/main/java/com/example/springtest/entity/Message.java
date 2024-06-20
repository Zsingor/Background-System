package com.example.springtest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Integer id;

    @JsonProperty(value = "senderId")
    private String senderId;
    private String senderName;
    private String receiverId;
    private String content;
    private Date createTime;
}
