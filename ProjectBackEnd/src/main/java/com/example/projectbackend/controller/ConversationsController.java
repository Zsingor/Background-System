package com.example.projectbackend.controller;


import com.example.projectbackend.entity.Conversation;
import com.example.projectbackend.entity.Result;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.service.ConversationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/conversations")
public class ConversationsController {
    @Autowired
    private ConversationsService conversationsService;

    @PostMapping("/getUnreadCount")
    public Result getUnreadCount(@RequestBody User user)
    {
        try {
            Integer data=conversationsService.getUnreadCount(user.getId());
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取数据失败");
        }
    }

    @PostMapping("/updateUnreadCount")
    public Result updateUnreadCount(@RequestBody Conversation conversation)
    {
        try {
            conversationsService.updateUnreadCount(conversation);
            return Result.success();
        } catch (Exception e) {
            return Result.error("修改未读信息失败");
        }
    }
}
