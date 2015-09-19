package com.codechef.smartalert.model;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_details", catalog = "smartbanking")
public class UserDetails implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String email;
	private String mobile;
	
	public UserDetails() {
	}

	public UserDetails(String email, String mobile) {
		this.email = email;
		this.mobile = mobile;
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

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "mobile", length = 50)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
