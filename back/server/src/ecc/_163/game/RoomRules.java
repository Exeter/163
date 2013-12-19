package ecc._163.game;

/**
 * stores information about the rules of 163 for specific rooms
 */
final public class RoomRules {
	
	/**
	 * number of players required before this room begins rounds of 163
	 */
	final public int m_numPlayers;
	
	/**
	 * time per round of 163 in this room
	 */
	final public int m_roundDuration;
	
	/**
	 * time per break between rounds of 163 in this room
	 */
	final public int m_breakDuration;
	
	/**
	 * the number of rounds per match of 163
	 */
	final public int m_numRounds;
	
	/**
	 * if the game will be rated
	 */
	final public boolean m_isRated;
	
	/**
	 * creates a rule object that defines the rules of 163 for a room.
	 * 
	 * @param numPlayers		the number of players required for the match to start
	 * @param roundDuration		the maximum amount of time given per round
	 * @param breakDuration		the duration of the break between rounds
	 * @param numRounds			the number of rounds per match
	 * @param rated				if the match is rated
	 */
	public RoomRules( int numPlayers , int roundDuration , int breakDuration , int numRounds , boolean rated ) {
		this.m_numPlayers = numPlayers;
		this.m_roundDuration = roundDuration;
		this.m_breakDuration = breakDuration;
		this.m_numRounds = numRounds;
		this.m_isRated = rated;
	}
	
	/**
	 * @return			the number of players required before the match can start
	 */
	final public int getNumPlayers() {
		return this.m_numPlayers;
	}
	
	/**
	 * @return			the maximum time allotted per round of 163
	 */
	final public int getRoundDuration() {
		return this.m_roundDuration;
	}
	
	/**
	 * @return			the break duration between successive rounds
	 */
	final public int getBreakDuration() {
		return this.m_breakDuration;
	}
	
	/**
	 * @return			the number of rounds of 163 per match
	 */
	final public int getNumRounds() {
		return this.m_numRounds;
	}
	
	/**
	 * @return			if the match is rated
	 */
	final public boolean getIsRated() {
		return this.m_isRated;
	}
}
