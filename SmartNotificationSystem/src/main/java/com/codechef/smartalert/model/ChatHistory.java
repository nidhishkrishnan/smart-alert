package com.codechef.smartalert.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "chat_history", catalog = "smartbanking")
public class ChatHistory implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date timestamp;
	private String userId;
	private String agentId;
	private Integer appointmentId;
	private String message;
	private Integer messageType;
	private Integer incoming;
	private String name;
	private String fileName;
	private String mimeType;

	public ChatHistory() {
	}

	public ChatHistory(String userId, String agentId, Integer appointmentId,
			String message, Integer messageType, Integer incoming, String name,
			String fileName, String mimeType) {
		this.userId = userId;
		this.agentId = agentId;
		this.appointmentId = appointmentId;
		this.message = message;
		this.messageType = messageType;
		this.incoming = incoming;
		this.name = name;
		this.fileName = fileName;
		this.mimeType = mimeType;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Version
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp", length = 19)
	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name = "userId", length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "agentId", length = 50)
	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	@Column(name = "appointmentId")
	public Integer getAppointmentId() {
		return this.appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	@Column(name = "message", length = 65535)
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "messageType")
	public Integer getMessageType() {
		return this.messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	@Column(name = "incoming")
	public Integer getIncoming() {
		return this.incoming;
	}

	public void setIncoming(Integer incoming) {
		this.incoming = incoming;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "fileName", length = 50)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "mimeType", length = 50)
	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
