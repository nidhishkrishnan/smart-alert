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
@Table(name = "employees", catalog = "smartbanking")
public class Employees implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String firstname;
	private String lastname;
	private String gender;
	private Date dob;
	private String empid;
	private String languages;
	private String areaExpert;
	private String addressLine1;
	private String addressLine2;
	private Integer pin;
	private Double mobile;
	private Double homePhone;
	private String email;
	private String status;
	private Date createdDate;
	private Date modifiedDate;

	public Employees() {
	}

	public Employees(String firstname, String lastname, String gender,
			Date dob, String empid, String languages, String areaExpert,
			String addressLine1, String addressLine2, Integer pin,
			Double mobile, Double homePhone, String email, String status,
			Date createdDate, Date modifiedDate) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.gender = gender;
		this.dob = dob;
		this.empid = empid;
		this.languages = languages;
		this.areaExpert = areaExpert;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.pin = pin;
		this.mobile = mobile;
		this.homePhone = homePhone;
		this.email = email;
		this.status = status;
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

	@Column(name = "firstname", length = 50)
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "lastname", length = 50)
	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Column(name = "gender", length = 50)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dob", length = 10)
	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Column(name = "empid", length = 50)
	public String getEmpid() {
		return this.empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	@Column(name = "languages", length = 65535)
	public String getLanguages() {
		return this.languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	@Column(name = "areaExpert", length = 65535)
	public String getAreaExpert() {
		return this.areaExpert;
	}

	public void setAreaExpert(String areaExpert) {
		this.areaExpert = areaExpert;
	}

	@Column(name = "addressLine1", length = 100)
	public String getAddressLine1() {
		return this.addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@Column(name = "addressLine2", length = 100)
	public String getAddressLine2() {
		return this.addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@Column(name = "pin")
	public Integer getPin() {
		return this.pin;
	}

	public void setPin(Integer pin) {
		this.pin = pin;
	}

	@Column(name = "mobile", precision = 22, scale = 0)
	public Double getMobile() {
		return this.mobile;
	}

	public void setMobile(Double mobile) {
		this.mobile = mobile;
	}

	@Column(name = "homePhone", precision = 22, scale = 0)
	public Double getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(Double homePhone) {
		this.homePhone = homePhone;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "status", length = 20)
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
