package com.netty.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hzy
 * @date 2022-04-02
 */

public class SentBody implements Serializable {
    private static final long serialVersionUID = 1L;
    private String key;
    private final HashMap<String, String> data = new HashMap();
    private long timestamp;

    public SentBody() {
    }

    public String getKey() {
        return this.key;
    }

    public String get(String k) {
        return (String)this.data.get(k);
    }

    public Integer getInteger(String k) {
        return this.data.containsKey(k) ? Integer.parseInt((String)this.data.get(k)) : null;
    }

    public Long getLong(String k) {
        return this.data.containsKey(k) ? Long.parseLong((String)this.data.get(k)) : null;
    }

    public Double getDouble(String k) {
        return this.data.containsKey(k) ? Double.parseDouble((String)this.data.get(k)) : null;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void remove(String k) {
        this.data.remove(k);
    }

    public void put(String k, String v) {
        if (v != null && k != null) {
            this.data.put(k, v);
        }

    }

    public void putAll(Map<String, String> map) {
        this.data.putAll(map);
    }

    public Set<String> getKeySet() {
        return this.data.keySet();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SentBody]").append("\n");
        buffer.append("key:").append(this.key).append("\n");
        buffer.append("timestamp:").append(this.timestamp).append("\n");
        buffer.append("data:{");
        this.data.forEach((k, v) -> {
            buffer.append("\n").append(k).append(":").append(v);
        });
        buffer.append("\n}");
        return buffer.toString();
    }
}
