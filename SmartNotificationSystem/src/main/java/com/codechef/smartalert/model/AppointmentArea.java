package com.codechef.smartalert.model;

import static javax.persistence.GenerationType.IDENTITY;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;	
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.annotate.JsonManagedReference;

@Entity
@Table(name = "appointment_area", catalog = "smartbanking")
public class AppointmentArea implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer areaId;
	private String name;
	private String description;
	private Date createdDate;
	private Date modifiedDate;
	@JsonManagedReference
	private Set<Appointments> appointments = new HashSet<Appointments>(0);
	@JsonManagedReference
	private Set<AppointmentAreaTopics> appointmentAreaTopics = new HashSet<AppointmentAreaTopics>(
			0);

	public AppointmentArea() {
	}

	public AppointmentArea(String name, String description, Date createdDate,
			Date modifiedDate, Set<Appointments> appointments,
			Set<AppointmentAreaTopics> appointmentAreaTopics) {
		this.name = name;
		this.description = description;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.appointments = appointments;
		this.appointmentAreaTopics = appointmentAreaTopics;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "area_id", unique = true, nullable = false)
	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	@Column(name = "name", length = 70)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "appointmentArea")
	public Set<Appointments> getAppointments() {
		return this.appointments;
	}

	public void setAppointments(Set<Appointments> appointments) {
		this.appointments = appointments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "appointmentArea")
	public Set<AppointmentAreaTopics> getAppointmentAreaTopics() {
		return this.appointmentAreaTopics;
	}

	public void setAppointmentAreaTopics(
			Set<AppointmentAreaTopics> appointmentAreaTopics) {
		this.appointmentAreaTopics = appointmentAreaTopics;
	}

}
