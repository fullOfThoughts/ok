//package com.bmd.core.service;
//
//
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.*;
//
////@Component
////@ServerEndpoint(value = "/api/chat")
//public class chatService {
//    //线程安全的静态变量，表示在线连接数
//    private static volatile int onlineCount = 0;
//
//    //若要实现服务端与指定客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
//    public static Map<String, Object> webSocketMap = new HashMap<>();
//
//    //与某个客户端的连接会话，通过它实现定向推送(只推送给某个用户)
//    private Session session;
//
//
//    /**
//     * 建立连接成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session) {
//        this.session = session;
//        webSocketMap.put(session.getId(), session);    // 添加到map中
//        addOnlineCount();    // 添加在线人数
//        System.out.println("新人加入，当前在线人数为：" + getOnlineCount());
//    }
//
//    /**
//     * 关闭连接调用的方法
//     */
//    @OnClose
//    public void onClose(Session closeSession)  {
//        webSocketMap.remove(closeSession.getId());
//        try {
//            closeSession.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        webSocketSet.remove(session);
//        subOnlineCount();
//        System.out.println("有人离开，当前在线人数为：" + getOnlineCount());
//    }
//
//    /**
//     * 收到客户端小心调用的方法
//     */
//    @OnMessage
//    public void onMessage(String message, Session mysession) throws Exception {
//        sendAllMessage(message);
//    }
//
//    @OnError
//    public void onError(Session session, Throwable error){
//
//
//    }
//
//    public void sendAllMessage(String message)  {
//        Set<String> strings = webSocketMap.keySet();
//        List<Session> list = new ArrayList<>();
//        strings.forEach(key->{
//            Session o = (Session) webSocketMap.get(key);
//            try {
//                o.getBasicRemote().sendText(message);
//            } catch (Exception e) {
//                list.add(o);
//            }
//        });
//        for (Session session1 : list) {
//            onClose(session1);
//        }
//    }
//
//    // 获取在线人数
//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
//
//    // 添加在线人+1
//    public static synchronized void addOnlineCount() {
//        onlineCount++;
//    }
//
//    // 减少在线人-1
//    public static synchronized void subOnlineCount() {
//        onlineCount--;
//    }
//}
