package ecc._163.game;

public class RoomProperties {

	/**
	 * the unique ID of the room
	 */
	final private int m_ID;
	
	/**
	 * a user-defined name for the room
	 */
	final private String m_name;
	
	/**
	 * the number of players connected to the room
	 */
	private int m_playersJoined = 0;
	
	/**
	 * creates a property object that describes a room's attributes
	 * 
	 * @param id			unique integer ID of the room
	 * @param name			user-defined name of the room
	 */
	public RoomProperties( int id , String name ) {
		this.m_ID = id;
		this.m_name = name;
	}
	
	/**
	 * @return			the unique ID of the room
	 */
	final public int getID() {
		return this.m_ID;
	}
	
	/**
	 * @return			the name of the room
	 */
	final public String getName() {
		return this.m_name;
	}
	
	/**
	 * @return			the number of players connected to the room
	 */
	final public int getNumPlayersJoined() {
		return this.m_playersJoined;
	}
	
	/**
	 * sets the number of players connected to the room
	 * 
	 * @param n			the number of players connected to the room
	 */
	final public void setNumPlayersJoined( int n ) {
		this.m_playersJoined = n;
	}
}
