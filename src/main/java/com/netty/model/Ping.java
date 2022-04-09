package com.netty.model;

import java.io.Serializable;

/**
 * @author hzy
 * @date 2022-04-02
 */
public class Ping implements Serializable, Transportable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "PING";
    private static final String DATA = "PING";
    private static final Ping INSTANCE = new Ping();

    private Ping() {
    }

    public static Ping getInstance() {
        return INSTANCE;
    }

    public String toString() {
        return "PING";
    }

    public byte[] getBody() {
        return "PING".getBytes();
    }

    public byte getType() {
        return 1;
    }
}
