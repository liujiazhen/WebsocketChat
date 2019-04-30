package com.liujiazhen.chat;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright © 2019 LiuJiazhen
 * <p>
 * description:
 *
 * @author LiuJiazhen
 * @version v1.0.0
 * @date 2019/4/30 15:23
 * <p>
 * Modification History
 * Date     Author      Version     Description
 * ---------------------------------------------------------*
 * 2019/4/30      LJZ     v1.0.0      create
 */
public class SingleMap {
    private static volatile ConcurrentHashMap<String, WebSocket> singleton;

    private SingleMap() {
    }

    public static ConcurrentHashMap<String, WebSocket> getInstance() {
        if (singleton == null) {
            synchronized (SingleMap.class) {
                if (singleton == null) {
                    singleton = new ConcurrentHashMap<String, WebSocket>();
                }
            }
        }
        return singleton;
    }
}
