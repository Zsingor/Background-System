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
    Integer id;

    @JsonProperty(value = "senderId")
    String senderId;

    String receiverId;
    String content;
    Date createTime;
}
