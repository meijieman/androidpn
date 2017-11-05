package com.hongfans.push.iq;

import org.jivesoftware.smack.packet.IQ;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/11/5 14:57
 */
public class PayloadIQ extends IQ{

    private String message;

    @Override
    public String getChildElementXML(){
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("payload").append(" xmlns=\"").append(
                "androidpn:iq:payload").append("\">");
        if(message != null){
            buf.append("<message>").append(message).append("</message>");
        }
        buf.append("</").append("payload").append("> ");
        return buf.toString();
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
