package com.codechef.smartalert.model;

import static javax.persistence.GenerationType.IDENTITY;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "times", catalog = "smartbanking")
public class Times implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String timeValue;
	private Integer selected;
	private Date createdDate;
	private Date modifiedDate;

	public Times() {
	}

	public Times(String timeValue, Integer selected, Date createdDate, Date modifiedDate) {
		this.timeValue = timeValue;
		this.selected = selected;
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

	@Column(name = "time_value", length = 50)
	public String getTimeValue() {
		return this.timeValue;
	}

	public void setTimeValue(String timeValue) {
		this.timeValue = timeValue;
	}

	@Column(name = "selected")
	public Integer getSelected() {
		return this.selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
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

