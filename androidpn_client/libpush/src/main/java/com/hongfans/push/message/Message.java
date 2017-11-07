package com.hongfans.push.message;

import java.io.Serializable;

/**
 * @desc: 消息传递的基础参数
 * @author: Major
 * @since: 2017/11/5 13:52
 */
public class Message implements Serializable{

    private String uuid; // 后台唯一键
    private String id;
    private String apiKey;
    private String clientId; // 客户端id，识别唯一客户端
    private String appId; // 应用id，需要先注册
    private String pkgName; // 应用包名，需要先注册

    public String getUuid(){
        return uuid;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getApiKey(){
        return apiKey;
    }

    public void setApiKey(String apiKey){
        this.apiKey = apiKey;
    }

    public String getClientId(){
        return clientId;
    }

    public void setClientId(String clientId){
        this.clientId = clientId;
    }

    public String getAppId(){
        return appId;
    }

    public void setAppId(String appId){
        this.appId = appId;
    }

    public String getPkgName(){
        return pkgName;
    }

    public void setPkgName(String pkgName){
        this.pkgName = pkgName;
    }

    @Override
    public String toString(){
        return "Message{" +
               "uuid='" + uuid + '\'' +
               "id='" + id + '\'' +
               ", apiKey='" + apiKey + '\'' +
               ", clientId='" + clientId + '\'' +
               ", appId='" + appId + '\'' +
               ", pkgName='" + pkgName + '\'' +
               '}';
    }
}
