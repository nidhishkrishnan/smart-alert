package com.codechef.smartalert.model;

import static javax.persistence.GenerationType.IDENTITY;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "users", catalog = "smartbanking")
public class Users implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private UserDetails userDetails;
	private String name;
	private String username;
	private String password;
	private String accountType;
	private String userType;
	private String status;
	private Date createdDate;
	private Date modifiedDate;
//	@JsonManagedReference
//	private Set<Appointments> appointmentses = new HashSet<Appointments>(0);

	public Users() {
	}

	public Users(UserDetails userDetails, String name, String username,
			String password, String accountType, String userType,
			String status, Date createdDate, Date modifiedDate) {
		this.userDetails = userDetails;
		this.name = name;
		this.username = username;
		this.password = password;
		this.accountType = accountType;
		this.userType = userType;
		this.status = status;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
//		this.appointmentses = appointmentses;
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

	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "user_details")
	public UserDetails getUserDetails() {
		return this.userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "username", length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "account_type", length = 50)
	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "user_type", length = 50)
	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
//	public Set<Appointments> getAppointmentses() {
//		return this.appointmentses;
//	}
//
//	public void setAppointmentses(Set<Appointments> appointmentses) {
//		this.appointmentses = appointmentses;
//	}


}
