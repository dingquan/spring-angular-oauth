package com.example.testapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class User extends BaseModel implements Serializable {

	private static final long serialVersionUID = 8993227507732079242L;
//	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	@Column(unique = true)
	private String email;
	
	@Column
	private String username;
	
	@Column
	private String displayName;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column
	private String password;
	
	@Column
	private String firstName;
	
	@Column
	private String lastName;
	
	@Column
	private String profileImage;
	
	@Column(unique = true)
	private String wechatId;
	
	@Column
	private String wechatName;
	
	@Column
	private String phone;
	
	@Column(name = "stripe_customer_id")
	@JsonIgnore
	private String stripeCustomerId;
	
	@Column(name = "reset_pwd_token")
	private String resetPwdToken;
	
	@Column(name = "reset_pwd_token_expires")
	private Date resetPwdTokenExpires;
	
	@Column
	@JsonProperty(access = Access.READ_ONLY)
	private Long points;
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null) {
			email = email.toLowerCase();
		}
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password == null || password.isEmpty()) {
			return; // don't set empty password
		}
//		this.password = PASSWORD_ENCODER.encode(password);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getWechatName() {
		return wechatName;
	}

	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStripeCustomerId() {
		return stripeCustomerId;
	}

	public void setStripeCustomerId(String stripeCustomerId) {
		this.stripeCustomerId = stripeCustomerId;
	}

	public String getResetPwdToken() {
		return resetPwdToken;
	}

	public void setResetPwdToken(String resetPwdToken) {
		this.resetPwdToken = resetPwdToken;
	}

	public Date getResetPwdTokenExpires() {
		return resetPwdTokenExpires;
	}

	public void setResetPwdTokenExpires(Date resetPwdTokenExpires) {
		this.resetPwdTokenExpires = resetPwdTokenExpires;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}
	

	@JsonIgnore
	@Transient
	// convenient getter which has a null test
	public Long getPointsBalance() {
		return points != null ? points : 0;
	}

	public User publicClone() {
		User u = new User();
		u.id = this.id;
		u.displayName = this.displayName;
		u.firstName = this.firstName;
		u.lastName = this.lastName;
		u.profileImage = this.profileImage;

		return u;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
