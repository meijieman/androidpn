package com.hongfans.push;

import java.io.Serializable;

/**
 * 消息传递对象
 * Created by MEI on 2017/11/4.
 */

public class Notification implements Serializable{

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String NOTIFICATION_API_KEY = "NOTIFICATION_API_KEY";
    public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";
    public static final String NOTIFICATION_URI = "NOTIFICATION_URI";

    private String id;
    private String apiKey;
    private String title;
    private String message;
    private String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
