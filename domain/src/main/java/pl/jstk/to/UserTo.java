package pl.jstk.to;

public class UserTo {
	private Long id;
	private String userName;
	private String password;

	public UserTo() {
	}

	public UserTo(Long id, String user, String password) {
		this.id = id;
		this.userName = user;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String user) {
		this.userName = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
