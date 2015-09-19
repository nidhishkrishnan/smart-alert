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
	@Table(name = "appointments", catalog = "smartbanking")
	public class Appointments implements java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private Integer id;
		@JsonBackReference
		private AppointmentArea appointmentArea;
		@JsonBackReference
		private Users users;
		private Date date;
		private Integer bankAgentId;
		private Integer timeId;
		private String topics;
		private Integer language;
		private Integer translationRequired;
		private String status;
		private Date createdDate;
		private Date modifiedDate;
		private String agentStatus;

		public Appointments() {
		}

		public Appointments(AppointmentArea appointmentArea, Users users,
				Date date, Integer bankAgentId, Integer timeId, String topics,
				Integer language, Integer translationRequired, String status,
				Date createdDate, Date modifiedDate, String agentStatus) {
			this.appointmentArea = appointmentArea;
			this.users = users;
			this.date = date;
			this.bankAgentId = bankAgentId;
			this.timeId = timeId;
			this.topics = topics;
			this.language = language;
			this.translationRequired = translationRequired;
			this.status = status;
			this.agentStatus = agentStatus;
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
		
		@Column(name = "agent_status", length = 50)
		public String getAgentStatus() {
			return this.agentStatus;
		}

		public void setAgentStatus(String agentStatus) {
			this.agentStatus = agentStatus;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "emp_id")
		public Users getUsers() {
			return this.users;
		}

		public void setUsers(Users users) {
			this.users = users;
		}

		@Temporal(TemporalType.DATE)
		@Column(name = "date", length = 10)
		public Date getDate() {
			return this.date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		@Column(name = "bank_agent_id")
		public Integer getBankAgentId() {
			return this.bankAgentId;
		}

		public void setBankAgentId(Integer bankAgentId) {
			this.bankAgentId = bankAgentId;
		}

		@Column(name = "time_id")
		public Integer getTimeId() {
			return this.timeId;
		}

		public void setTimeId(Integer timeId) {
			this.timeId = timeId;
		}

		@Column(name = "topics", length = 65535)
		public String getTopics() {
			return this.topics;
		}

		public void setTopics(String topics) {
			this.topics = topics;
		}

		@Column(name = "language")
		public Integer getLanguage() {
			return this.language;
		}

		public void setLanguage(Integer language) {
			this.language = language;
		}

		@Column(name = "translation_required")
		public Integer getTranslationRequired() {
			return this.translationRequired;
		}

		public void setTranslationRequired(Integer translationRequired) {
			this.translationRequired = translationRequired;
		}

		@Column(name = "status", length = 50)
		public String getStatus() {
			return this.status;
		}

		public void setStatus(String status) {
			this.status = status;
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
