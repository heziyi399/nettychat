package com.netty.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hzy
 * @date 2022-04-02
 */
public class ReplyBody implements Serializable, Transportable {
    private static final long serialVersionUID = 1L;
    private String key;
    private String code;
    private String message;
    private final HashMap<String, String> data = new HashMap();
    private long timestamp = System.currentTimeMillis();

    public ReplyBody() {
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void put(String k, String v) {
        if (v != null && k != null) {
            this.data.put(k, v);
        }

    }

    public void putAll(Map<String, String> map) {
        this.data.putAll(map);
    }

    public String get(String k) {
        return (String)this.data.get(k);
    }

    public void remove(String k) {
        this.data.remove(k);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<String> getKeySet() {
        return this.data.keySet();
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCode(int code) {
        this.code = String.valueOf(code);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ReplyBody]").append("\n");
        buffer.append("key:").append(this.getKey()).append("\n");
        buffer.append("timestamp:").append(this.timestamp).append("\n");
        buffer.append("code:").append(this.code).append("\n");
        buffer.append("data:{");
        this.data.forEach((k, v) -> {
            buffer.append("\n").append(k).append(":").append(v);
        });
        buffer.append("\n}");
        return buffer.toString();
    }

    public byte[] getBody() {
        ReplyBodyProto.Model.Builder builder = ReplyBodyProto.Model.newBuilder();
        builder.setCode(this.code);
        if (this.message != null) {
            builder.setMessage(this.message);
        }

        if (!this.data.isEmpty()) {
            builder.putAllData(this.data);
        }

        builder.setKey(this.key);
        builder.setTimestamp(this.timestamp);
        return builder.build().toByteArray();
    }

    public byte getType() {
        return 4;
    }
}
