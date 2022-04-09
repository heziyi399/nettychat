package com.netty.model;

/**
 * @author hzy
 * @date 2022-04-02
 */
public interface Transportable {
    byte[] getBody();

    byte getType();
}
