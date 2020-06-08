package com.redhat.custom.storage.user;

/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */

public class UserEntity {
	private String id; // unique id of the user in the database
	private String loginName; // login user name
	private String email;
	private String phone;
	private String fullName;

	public UserEntity(String loginName, String phone, String email, String fullName) {

		this.id = loginName;
		this.loginName = loginName;
		this.phone = phone;
		this.email = email;
		this.fullName = fullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", loginName=" + loginName + ", email=" + email
		/* + ", password=" + password */ + ", phone=" + phone + ", userName=" + fullName + "]";
	}

}
