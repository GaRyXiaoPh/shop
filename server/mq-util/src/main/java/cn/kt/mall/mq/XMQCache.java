package cn.kt.mall.mq;

import javax.jms.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class XMQCache {

    private XMQCache() {
    }

    private static final ConcurrentHashMap<String, Session> connSessionMap = new ConcurrentHashMap<>();

    public static Session getSessionById(String connId) {
        return connSessionMap.get(connId);
    }

    public static void putSession(String connId, Session session) {
        connSessionMap.putIfAbsent(connId, session);
    }

    public static void removeSession(String connId) {
        connSessionMap.remove(connId);
    }

    public static void cleanSession() {
        connSessionMap.clear();
    }

    // 临时变量
    public static final AtomicInteger SNET_COUNT = new AtomicInteger(0);
}