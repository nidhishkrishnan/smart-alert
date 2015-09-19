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

@Entity
@Table(name = "holidays", catalog = "smartbanking")
public class Holidays implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date date;
	private String times;
	private String reason;
	private Date createdDate;
	private Date modifiedDate;

	public Holidays() {
	}

	public Holidays(Date date, String times, String reason, Date createdDate,
			Date modifiedDate) {
		this.date = date;
		this.times = times;
		this.reason = reason;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "date", length = 10)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "times", length = 80)
	public String getTimes() {
		return this.times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	@Column(name = "reason", length = 65535)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", length = 19)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
