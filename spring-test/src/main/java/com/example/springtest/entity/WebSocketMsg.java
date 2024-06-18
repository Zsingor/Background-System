package com.example.springtest.entity;


import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class WebSocketMsg {
    private String username;
    private Session session;
}
