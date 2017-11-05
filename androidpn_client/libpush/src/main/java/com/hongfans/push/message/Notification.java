package com.hongfans.push.message;

/**
 * 通知
 * Created by MEI on 2017/11/4.
 */

public class Notification extends Message{

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String NOTIFICATION_API_KEY = "NOTIFICATION_API_KEY";
    public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";
    public static final String NOTIFICATION_URI = "NOTIFICATION_URI";

    private String title;
    private String message;
    private String uri;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getUri(){
        return uri;
    }

    public void setUri(String uri){
        this.uri = uri;
    }

    @Override
    public String toString(){
        return "Notification{" +
               ", title='" + title + '\'' +
               ", message='" + message + '\'' +
               ", uri='" + uri + '\'' +
               '}';
    }
}
