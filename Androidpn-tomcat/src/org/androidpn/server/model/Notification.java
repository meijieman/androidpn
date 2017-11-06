package org.androidpn.server.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 离线消息推送
 * @author xuyusong
 *
 */
@Entity
@Table(name = "apn_notification")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id ;
	
	@Column(name ="uuid",length=64, nullable = false, unique = true)
	private String uuid;
	
	@Column(name = "api_key",length = 64)
	private String apiKey;
	//一个用户可以拥有多条离线消息
	@Column(name = "user_name", length = 64)
	private String username;
	
	@Column(name = "title",nullable = false, length = 64)
	private String title;
	
	@Column(name = "message", nullable = false,length = 1000)
	private String message;
	
	@Column(name = "uri", length = 256)
	private String uri;

	@Column(name = "push_to")
	private String pushTo;

	@Column(name = "push_Type")
	private String pushType; // 推送类型 notification， payload

	@Column(name = "created_date", updatable = false)
	private Date createdDate = new Date();

	@Column(name = "valid_time")
	private Long validTime; // 有效时间，从创建时间计算，单位 s

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
