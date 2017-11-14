package com.hongfans.push.message;

/**
 * 通知
 * Created by MEI on 2017/11/4.
 */

public class Notification extends Message{

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
        return super.toString() + "Notification{" +
               ", title='" + title + '\'' +
               ", message='" + message + '\'' +
               ", uri='" + uri + '\'' +
               '}';
    }
}
