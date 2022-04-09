package com.netty.model;

import lombok.Data;

/**
 * @author hzy
 * @date 2022-04-02
 */
@Data
public class MessageBody {
    private static final long serialVersionUID = 1L;
    private long id;
    private String action;
    private String title;
    private String content;
    private String sender;
    private String receiver;
    private String format;
    private String extra;
    private long timestamp = System.currentTimeMillis();

    public MessageBody() {
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("#Message#").append("\n");
        buffer.append("id:").append(this.id).append("\n");
        buffer.append("action:").append(this.action).append("\n");
        buffer.append("title:").append(this.title).append("\n");
        buffer.append("content:").append(this.content).append("\n");
        buffer.append("extra:").append(this.extra).append("\n");
        buffer.append("sender:").append(this.sender).append("\n");
        buffer.append("receiver:").append(this.receiver).append("\n");
        buffer.append("format:").append(this.format).append("\n");
        buffer.append("timestamp:").append(this.timestamp);
        return buffer.toString();
    }

    public boolean isNotEmpty(String txt) {
        return txt != null && txt.trim().length() != 0;
    }

    public MessageBody clone() {
        MessageBody message = new MessageBody();
        message.id = this.id;
        message.action = this.action;
        message.title = this.title;
        message.content = this.content;
        message.sender = this.sender;
        message.receiver = this.receiver;
        message.extra = this.extra;
        message.format = this.format;
        message.timestamp = this.timestamp;
        return message;
    }

    public byte[] getBody() {
        MessageProto.Model.Builder builder = MessageProto.Model.newBuilder();
        builder.setId(this.id);
        builder.setAction(this.action);
        builder.setSender(this.sender);
        builder.setTimestamp(this.timestamp);
        if (this.receiver != null) {
            builder.setReceiver(this.receiver);
        }

        if (this.content != null) {
            builder.setContent(this.content);
        }

        if (this.title != null) {
            builder.setTitle(this.title);
        }

        if (this.extra != null) {
            builder.setExtra(this.extra);
        }

        if (this.format != null) {
            builder.setFormat(this.format);
        }

        return builder.build().toByteArray();
    }

    public byte getType() {
        return 2;
    }
}
