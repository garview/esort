package worm.esort.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;


/**
 * 主要用于多用户使用功能。
 * 需要记录用户的注册时间、最后登录时间
 * @author John
 *
 */
@Entity
@Table(name="user")
public class User {

	private Long id;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private Date registerDate;
	
	private Date lastLoginDate;
	
	public User(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}

	public User() {
		super();
		registerDate = new Date();
	}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	@NotBlank
	@Column(unique=true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@NotBlank
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", registerDate=" + registerDate + ", lastLoginDate=" + lastLoginDate + "]";
	}
	
	
}
