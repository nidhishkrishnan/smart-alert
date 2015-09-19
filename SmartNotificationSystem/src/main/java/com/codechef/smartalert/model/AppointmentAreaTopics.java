package com.codechef.smartalert.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name = "appointment_area_topics", catalog = "smartbanking")
public class AppointmentAreaTopics implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	@JsonBackReference
	private AppointmentArea appointmentArea;
	private String topicName;
	private Date createdDate;
	private Date modifiedDate;

	public AppointmentAreaTopics() {
	}

	public AppointmentAreaTopics(AppointmentArea appointmentArea,
			String topicName, Date createdDate, Date modifiedDate) {
		this.appointmentArea = appointmentArea;
		this.topicName = topicName;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id")
	public AppointmentArea getAppointmentArea() {
		return this.appointmentArea;
	}

	public void setAppointmentArea(AppointmentArea appointmentArea) {
		this.appointmentArea = appointmentArea;
	}

	@Column(name = "topic_name", length = 50)
	public String getTopicName() {
		return this.topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
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
