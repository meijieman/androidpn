package com.hongfans.push.message;

/**
 * @desc: 穿透消息
 * @author: Major
 * @since: 2017/11/5 13:59
 */
public class Payload extends Message{

    private String message;

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    @Override
    public String toString(){
        return super.toString() + "Payload{" +
               "message='" + message + '\'' +
               '}';
    }
}
