package ecc._163.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import ecc._163.net.Communicator;
import ecc._163.net.MessageWriter;

/**
 * represents a room in which 163 may be played
 */
public class Room {

	/**
	 * the value 163
	 */
	final public static int ONE_SIXTY_THREE = 163;
	
	/**
	 * the number of cards given to users to make 163
	 */
	final public static int NUM_CARDS = 6;
	
	/**
	 * the properties of this room, such as the room's name and ID
	 */
	private RoomProperties m_properties;
	
	/**
	 * the rules for this room
	 */
	private RoomRules m_rules;
	
	/**
	 * the number of rounds of 163 left to be played in this room
	 */
	protected int m_roundsLeft = 0;
	
	/**
	 * cards being used in the current round of 163
	 */
	final private int[] m_cards = new int[ 6 ];
	
	/**
	 * the countdown timer for this room
	 */
	private CountdownTimer m_timer;
	
	/**
	 * the list of contestants participating in 163 in this room
	 */
	final private ArrayList< Contestant > m_contestants = new ArrayList< Contestant >();
	
	/**
	 * creates a room in which rounds of 163 may be played
	 * 
	 * @param properties	the properties of this room, such as name and id
	 * @param rules			the rules of 163 pertaining to this room
	 */
	public Room( RoomProperties properties , RoomRules rules ) {
		this.m_properties = properties;
		this.m_rules = rules;
	}
	
	/**
	 * @return			the properties of this room
	 */
	public RoomProperties getProperties() {
		return this.m_properties;
	}
	
	/**
	 * adds a contestant to this room
	 * 
	 * @param clientConnection			a connection to the client
	 * @param contestantDisplayName		the display name of the contestant
	 */
	public void addContestant( Communicator clientConnection , String contestantDisplayName ) {
		this.m_contestants.add( new Contestant( clientConnection , contestantDisplayName ) );
		this.m_properties.setNumPlayersJoined( this.m_contestants.size() );
	}
	
	/**
	 * removes a contestant from this room
	 * 
	 * @param c				the contestant to remove
	 */
	public void removeContestant( Contestant c ) {
		this.m_contestants.remove( c );
		this.m_properties.setNumPlayersJoined( this.m_contestants.size() );
	}
	
	/**
	 * starts the match of 163 for this room
	 */
	public void startMatch() {
		
		//TODO notify users match is starting
		for ( Contestant c : this.m_contestants ) {
			MessageWriter.writeMatchStarting( c.getCommunicator() );
		}
		this.m_roundsLeft = this.m_rules.getNumRounds();
		new BreakTimer( this.m_rules.getBreakDuration() ).start();
	}
	
	/**
	 * starts a new round of 163
	 */
	protected void startRound() {
		
		System.out.println( "Round has started" );
		//generate a problem
		int randomSeed = ( int )( Math.random() * 100000 );
		try {
			Process runGenerator = Runtime.getRuntime().exec( "./a.out " + ONE_SIXTY_THREE + " " + NUM_CARDS + " " + randomSeed );
			BufferedReader cardReader = new BufferedReader( new InputStreamReader( runGenerator.getInputStream() ) );
			
			//read in the cards generated by the generator
			for ( int i = 0 ; i < this.m_cards.length ; i++ ) {
				this.m_cards [ i ] = Integer.parseInt( cardReader.readLine() );
			}
			//close inputs
			cardReader.close();
		} catch ( IOException e ) {
			
			//TODO notify users of internal error
			for ( Contestant c : this.m_contestants ) {
				MessageWriter.writeInternalError( c.getCommunicator() );
			}
			return;
		}
		
		//TODO send the problem to all the players
		for ( Contestant c : this.m_contestants ) {
			MessageWriter.writeNewProblem( c.getCommunicator() , this.m_cards );
		}
		
		//start the timer
		this.m_timer = new CountdownTimer( this.m_rules.getRoundDuration() );
		this.m_timer.start();
		
		//DEBUG
		for ( int i = 0 ; i < this.m_cards.length ; i++ ) {
			System.out.print( this.m_cards[ i ] + " ");
		}
		System.out.println();
		
		//update number of rounds left
		this.m_roundsLeft--;
	}
	
	/**
	 * ends a round of 163
	 */
	protected void endRound() {
		
		//TODO notify users that round is over, no more solutions accepted
		for ( Contestant c : this.m_contestants ) {
			MessageWriter.writeRoundOver( c.getCommunicator() );
		}
		System.out.println( "Round is over" );
		this.m_timer.cancel();
		
		//pause briefly between rounds
		if ( this.m_roundsLeft > 0 ) {
			new BreakTimer( this.m_rules.getBreakDuration() ).start();
		} else {
			endMatch();
		}
	}
	
	/**
	 * ends a match of 163
	 */
	protected void endMatch() {
		System.out.println( "Match is over" );
		
		//TODO notify users that match is over
		for ( Contestant c : this.m_contestants ) {
			MessageWriter.writeMatchOver( c.getCommunicator() );
		}
		//TODO update user ratings
	}
	
	/**
	 * checks a solution to 163
	 */
	public void checkSolution( Contestant submitter , String solution ) {
		try {
			
			//determine the cards that must be used
			String cards = "";
			for ( int i = 0 ; i < this.m_cards.length ; i++ ) {
				cards += this.m_cards[ i ] + " ";
			}
			cards = cards.trim();
			
			//check the user's solution
			Process runChecker = Runtime.getRuntime().exec( "java -jar checker.jar " + ONE_SIXTY_THREE + " " + NUM_CARDS + " " + cards + " " + solution );
			runChecker.waitFor();
			
			//notify user of result
			if ( runChecker.exitValue() == 0 ) {
				
				//TODO notify all other users of valid solution
				for ( Contestant c : this.m_contestants ) {
					MessageWriter.writeAnswerAccepted( c.getCommunicator() , submitter.getDisplayName() );
				}
				System.out.println( "ok" );
				
				//update contestant score
				submitter.incrementScore();
				
				endRound();
			} else {
				
				//TODO notify users of failed solution
				for ( Contestant c : this.m_contestants ) {
					MessageWriter.writeAnswerRejected( c.getCommunicator() , c.getDisplayName() , solution );
				}
				System.out.println( "failed" );
			}
		} catch ( IOException e ) {
			
			//TODO notify user of internal error
			for ( Contestant c : this.m_contestants ) {
				MessageWriter.writeInternalError( c.getCommunicator() );
			}
		} catch ( InterruptedException e ) {
			
			//TODO notify user of internal error
			for ( Contestant c : this.m_contestants ) {
				MessageWriter.writeInternalError( c.getCommunicator() );
			}
		}
	}
	
	/**
	 * Timer that counts down to 0 by the second
	 */
	private class CountdownTimer extends Timer {
		
		/**
		 * seconds left before the timer times out
		 */
		protected int m_seconds;
		
		/**
		 * creates a timer that ticks off a specified number of seconds
		 * and performs an action upon timing out
		 * 
		 * @param seconds			number of seconds for the timer to count down
		 */
		public CountdownTimer( int seconds ) {
			super();
			this.m_seconds = seconds;
		}
		
		/**
		 * starts this timer
		 */
		public void start() {
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					//if there are no seconds left, then this timer
					//has timed out
					if ( CountdownTimer.this.m_seconds <= 0 ) {
						CountdownTimer.this.handleTimeout();
						CountdownTimer.this.cancel();
					}
					CountdownTimer.this.m_seconds --;
				}
			};
			super.schedule( task , 0 , 1000 );
		}
		
		/**
		 * @return			the number of seconds left before this timer times out
		 */
		//TODO Use this in determining rating
		@SuppressWarnings("unused")
		public int getSecondsLeft() {
			return this.m_seconds;
		}
		
		/**
		 * performs a task once the timer ticks to 0
		 */
		public void handleTimeout() {
			endRound();
		}
	}
	
	/**
	 * timer for keeping track of breaks between successive rounds
	 */
	private class BreakTimer extends CountdownTimer {

		/**
		 * creates a round pause timer that ticks down to 0 by the second
		 * between rounds of 163
		 * 
		 * @param seconds		number of seconds to tick
		 */
		public BreakTimer(int seconds) {
			super(seconds);
		}
		
		@Override
		public void handleTimeout() {
			startRound();
		}
	}
	
	/**
	 * stores information about the contestants in each room
	 */
	private static class Contestant {
		
		/**
		 * the connection to the client
		 */
		final private Communicator m_connection;
		
		/**
		 * display name of this contestant
		 */
		final private String m_displayName;
		
		/**
		 * rounds of 163 won
		 */
		private int m_score = 0;
		
		/**
		 * creates the Contestant object for a player
		 * 
		 * @param displayName		the display name of the player
		 */
		public Contestant( Communicator clientConnection , String displayName ) {
			this.m_connection = clientConnection;
			this.m_displayName = displayName;
		}
		
		/**
		 * @return			the connection between the server and this contestant's client
		 */
		public Communicator getCommunicator() {
			return this.m_connection;
		}
		
		/**
		 * @return			the display name of this contestant
		 */
		public String getDisplayName() {
			return this.m_displayName;
		}
		
		/**
		 * @return			the number of rounds of 163 this contestant has won
		 */
		//TODO use this in determining rating
		@SuppressWarnings("unused")
		public int getScore() {
			return this.m_score;
		}
		
		/**
		 * adds one to this contestant's score
		 */
		public void incrementScore() {
			this.m_score++;
		}
	}
	
	
	//DEBUG
	final public static void main( String[] args ) {
		Room r = new Room( new RoomProperties( 1 , "Mickey's Room" ) , new RoomRules( 1 , 10 , 3 , 4 , false ) );
		r.startMatch();
		Scanner s = new Scanner( System.in );
		while ( true ) {
			r.checkSolution( new Contestant( new Communicator() , "test" ) , s.nextLine() );
		}
	}//*/
}
