package org.androidpn.server.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

/**
 * 推送详情
 */
@Entity
@Table(name = "apn_push_detail")
public class PushDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "uuid", length = 64, nullable = false)
    private String uuid; // Notification uuid

    @Transient
    private String title;

    @Column(name = "username", nullable = false, length = 64)
    private String username;  // User username

    @Transient
    private String alias; // 设备最近绑定的 alias，一台设备一般会绑定过多个 alias

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "delivered_date")
    private Date deliveredDate; // 发送时间，一般和创建时间相同，离线消息发送时间稍晚

    @Column(name = "receipt_date")
    private Date receiptDate; // 到达客户端后立即返回一个回执 IQ

    @Column(name = "click_date")
    private Date clickDate; // 点击时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Date getClickDate() {
        return clickDate;
    }

    public void setClickDate(Date clickDate) {
        this.clickDate = clickDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
