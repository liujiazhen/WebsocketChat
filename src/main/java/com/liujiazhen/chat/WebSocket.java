package com.liujiazhen.chat;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright © 2019 LiuJiazhen
 * <p>
 * description:
 *
 * @author LiuJiazhen
 * @version v1.0.0
 * @date 2019/4/30 13:59
 * <p>
 * Modification History
 * Date     Author      Version     Description
 * ---------------------------------------------------------*
 * 2019/4/30      LJZ     v1.0.0      create
 */
@ServerEndpoint("/websocket/{info}")
public class WebSocket {
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}

        WebSocket webSocket = (WebSocket) o;

        return session != null ? session.equals(webSocket.session) : webSocket.session == null;

    }

    @Override
    public int hashCode() {
        return session != null ? session.hashCode() : 0;
    }

    private static ConcurrentHashMap<String, WebSocket> roomList = SingleMap.getInstance();

    private volatile String name;

    private static volatile int onlineCount = 0;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    @OnOpen
    public void onOpen(@PathParam("info") String parm, Session session) {
        this.session = session;
        this.name = parm;
        roomList.put(name, this);
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        for (Map.Entry<String, WebSocket> entry : roomList.entrySet()) {
            try {
                entry.getValue().sendMessage(name + " 加入狗子聊天室！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        String name = this.name;
        roomList.remove(name);
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //群发消息
        for (Map.Entry<String, WebSocket> entry : roomList.entrySet()) {
            try {
                entry.getValue().sendMessage(this.name + "：" + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }
}