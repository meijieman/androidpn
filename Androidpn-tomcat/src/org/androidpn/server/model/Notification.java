package org.androidpn.server.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

/**
 * 通知（推送通知栏消息，穿透消息）
 *
 * @author xuyusong
 */
@Entity
@Table(name = "apn_notification")
public class Notification {

    public static final String STATE_EXPIRED = "expired";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "uuid", length = 64, nullable = false, unique = true)
    private String uuid;

    @Column(name = "api_key", length = 64)
    private String apiKey;

    @Column(name = "title", nullable = false, length = 64)
    private String title;

    @Column(name = "message", nullable = false, length = 1000)
    private String message; // 详情

    @Column(name = "uri", length = 256)
    private String uri;

    @Column(name = "push_to")
    private String pushTo; // 推送目标用户 all 全部，tag 标签，username 指定，group 分组

    @Column(name = "push_Type")
    private String pushType; // 推送类型 notification， payload

    @Column(name = "created_date", updatable = false)
    private Date createdDate = new Date();

    @Column(name = "valid_time")
    private Long validTime; // 有效时间，从创建时间计算，单位 s

    @Column(name = "state")
    private String state; // 状态 默认有效，否则过期（expired）

    // private String laterAction; // 点击通知栏的后续操作，启动应用，打开连接，下载应用

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPushTo() {
        return pushTo;
    }

    public void setPushTo(String pushTo) {
        this.pushTo = pushTo;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getValidTime() {
        return validTime;
    }

    public void setValidTime(Long validTime) {
        this.validTime = validTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
