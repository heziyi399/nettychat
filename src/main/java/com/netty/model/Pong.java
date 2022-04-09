package com.netty.model;

import java.io.Serializable;

/**
 * @author hzy
 * @date 2022-04-02
 */
public class Pong implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "PONG";
    private static final Pong INSTANCE = new Pong();

    private Pong() {
    }

    public static Pong getInstance() {
        return INSTANCE;
    }

    public String toString() {
        return "PONG";
    }
}
