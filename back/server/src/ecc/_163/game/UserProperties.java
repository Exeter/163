package ecc._163.game;

public class UserProperties {

	/**
	 * the username the user uses to log on
	 */
	final private String m_username;
	
	/**
	 * the password the user provides to log on with his/her username
	 */
	final private String m_password;
	
	/**
	 * the display name all other users will see
	 */
	final private String m_displayName;
	
	public UserProperties( String username , String password , String displayName ) {
		this.m_username = username;
		this.m_password = password;
		this.m_displayName = displayName;
	}
	
	/**
	 * @return			the username of the user
	 */
	final public String getUsername() {
		return this.m_username;
	}
	
	/**
	 * @return			the password associated with the username of the user
	 */
	final public String getPassword() {
		return this.m_password;
	}
	
	/**
	 * @return			the display name of the user
	 */
	final public String getDisplayName() {
		return this.m_displayName;
	}
}
