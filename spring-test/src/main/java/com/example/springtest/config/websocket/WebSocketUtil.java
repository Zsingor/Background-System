package com.example.springtest.config.websocket;

import com.example.springtest.entity.WebSocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
@ServerEndpoint(value = "/webSocket/{userid}")
public class WebSocketUtil {
    /**
     * 登录连接数
     */
    private static int loginCount = 0;
    /**
     * user 线程安全的
     */
    private static final Map<String, WebSocketMsg> userMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("userid") String userid, Session session, EndpointConfig config) {
        WebSocketMsg webSocketMsg = new WebSocketMsg();
        webSocketMsg.setUserid(userid);
        webSocketMsg.setSession(session);
        boolean containsKey = userMap.containsKey(userid);
        if (!containsKey) {
            // 添加登录用户数量
            addLoginCount();
            userMap.put(userid, webSocketMsg);
        }
        System.out.println("WebSocket 连接已经建立。");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("收到客户端消息：" + message);
        session.getBasicRemote().sendText("服务器收到消息：" + message);
    }

    @OnClose
    public void onClose(@PathParam("userid") String userid, Session session, CloseReason closeReason) {
        boolean containsKey = userMap.containsKey(userid);
        if (containsKey) {
            // 删除map中用户
            userMap.remove(userid);
            // 减少断开连接的用户
            reduceLoginCount();
        }
        System.out.println("WebSocket 连接已经关闭。");
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("WebSocket 连接出现错误：" + t.getMessage());
    }

    /**
     * @Description: 获取用户
     * @Param []
     * @Return: java.util.Map<java.lang.String, com.cn.webSocket.WebSocket>
     **/
    public synchronized Map<String, WebSocketMsg> getUsers() {
        return userMap;
    }

    /**
     * @Description: 发送指定用户信息
     * @Param [ username：用户,Message：信息]
     * @Return: void
     **/
    public void sendMessageTo(String userid, String content) throws IOException {
        for (WebSocketMsg item : userMap.values()) {
            if (item.getUserid().equals(userid)) {
                item.getSession().getAsyncRemote().sendText(content);
            }
        }
    }

    /**
     * @Description: 发给所有人
     * @Param [Message：信息]
     * @Return: void
     **/
    public void sendMessageAll(String content) throws IOException {
        for (WebSocketMsg item : userMap.values()) {
            item.getSession().getAsyncRemote().sendText(content);
        }
    }

    /**
     * @Description: 连接登录数增加
     * @Param []
     * @Return: void
     **/
    public static synchronized void addLoginCount() {
        loginCount++;
    }

    /**
     * @Description: 连接登录数减少
     * @Param []
     * @Return: void
     **/
    public static synchronized void reduceLoginCount() {
        loginCount--;
    }
}
