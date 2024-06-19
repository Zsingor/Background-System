package com.example.springtest.controller;


import com.example.springtest.entity.Message;
import com.example.springtest.entity.Result;
import com.example.springtest.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/getMessage")
    public Result getMessage(@RequestParam String senderId, @RequestParam String receiverId)
    {
        try {
            List<Message> data=messageService.getMessage(senderId, receiverId);
            return Result.success(data);
        }
        catch (Exception e)
        {
            return Result.error("获取消息列表失败");
        }

    }
}
