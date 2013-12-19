package ecc._163.net;

public class MessageWriter {

	final public static String DELIMITER = "`";
	
	final public static void writeInternalError( Communicator c ) {
		c.write( "<INTERNAL_ERROR>" );
	}
	
	final public static void writeLoginSuccess( Communicator c ) {
		c.write( "<LOGIN_SUCCESS>" );
	}
	
	final public static void writeCreateRoomSuccess( Communicator c ) {
		c.write( "<CREATE_ROOM_SUCCESS>" );
	}
	
	final public static void writeNonexistingRoom( Communicator c ) {
		c.write( "<NONEXISTING_ROOM>" );
	}
	
	final public static void writeJoinRoomSuccess( Communicator c ) {
		c.write( "<JOIN_ROOM_SUCCESS>" );
	}
	
	final public static void writeMatchStarting( Communicator c ) {
		c.write( "<MATCH_STARTING>" );
	}
	
	final public static void writeNewProblem( Communicator c , int[] cards ) {
		String newProblemMessage = "<NEW_PROBLEM>";
		for ( int i = 0 ; i < cards.length ; i++ ) {
			newProblemMessage += DELIMITER + cards[ i ];
		}
		c.write( newProblemMessage );
	}
	
	/**
	 * notifies the client that someone else has answered correctly already
	 * 
	 * @param c
	 */
	final public static void writeAnswerAccepted( Communicator c , String displayNameOfSolver ) {
		c.write( "<ANSWER_ACCEPTED>" + DELIMITER + displayNameOfSolver);
	}
	
	/**
	 * notifies the client that someone answered incorrectly, and what that
	 * person's solution was
	 * 
	 * @param c
	 * @param displayName
	 * @param invalidSolution
	 */
	final public static void writeAnswerRejected( Communicator c , String displayName , String invalidSolution ) {
		c.write( "<ANSWER_REJECTED>" + DELIMITER + displayName + DELIMITER + invalidSolution );
	}
	
	final public static void writeRoundOver( Communicator c ) {
		c.write( "<ROUND_OVER>" );
	}
	
	final public static void writeMatchOver( Communicator c ) {
		c.write( "<MATCH_OVER>" );
	}
	
}
