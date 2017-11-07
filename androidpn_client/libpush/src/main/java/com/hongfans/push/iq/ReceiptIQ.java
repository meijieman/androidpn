package com.hongfans.push.iq;

import org.jivesoftware.smack.packet.IQ;

/**
 * 消息回执
 */
public class ReceiptIQ extends IQ{

    private String uuid;

    @Override
    public String getChildElementXML(){
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("receipt").append(" xmlns=\"").append(
                "androidpn:iq:receipt").append("\">");
        if(uuid != null){
            buf.append("<uuid>").append(uuid).append("</uuid>");
        }
        buf.append("</").append("receipt").append("> ");
        return buf.toString();
    }

    public String getUuid(){
        return uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

}
