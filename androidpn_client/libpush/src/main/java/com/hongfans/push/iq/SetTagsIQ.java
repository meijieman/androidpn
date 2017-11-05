package com.hongfans.push.iq;

import org.jivesoftware.smack.packet.IQ;

/**
 * @desc: 设置一组标签
 * @author: Major
 * @since: 2017/11/5 14:29
 */
public class SetTagsIQ extends IQ{

    private String tags;

    @Override
    public String getChildElementXML(){
        StringBuilder buf = new StringBuilder();
        buf.append("<").append("settags").append(" xmlns=\"").append(
                "androidpn:iq:settags").append("\">");
        if(tags != null){
            buf.append("<tags>").append(tags).append("</tags>");
        }
        buf.append("</").append("settags").append("> ");
        return buf.toString();
    }

    public String getTags(){
        return tags;
    }

    public void setTags(String tags){
        this.tags = tags;
    }
}
