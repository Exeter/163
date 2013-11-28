package ecc._163.game;

import java.util.Scanner;

import ecc._163.net.Communicator;
import ecc._163.net.MessageWriter;
import ecc._163.net.MessagesReceived;

public class Server extends Thread {

	final public static int MAX_ROOMS = 3000;
	
	/**
	 * the list of rooms on this server
	 */
	final protected Room[] m_rooms = new Room[ MAX_ROOMS ];
	
	final public static int MAX_CLIENTS = 9000;
	final protected SubServer[] m_subservers = new SubServer[ MAX_CLIENTS ];
	
	/**
	 * creates a 163 server
	 */
	public Server() {
		start();
	}
	
	@Override
	public void run() {
		while ( !this.isInterrupted() ) {
			//TODO accept a client
		}
	}
	
	public void addClient( Communicator c ) {
		
		//find an untaken subserver id
		for ( int subserverId = 0 ; subserverId < MAX_CLIENTS ; subserverId++ ) {
			if ( this.m_subservers[ subserverId ] == null ) {
				
				//create the new subserver
				SubServer newSubServer = new SubServer( subserverId , c );
				this.m_subservers[ subserverId ] = newSubServer;
				
				//stop because we have assigned the new subserver
				break;
			}
		}
	}
	
	public void createRoom( String roomName , int numPlayers , int roundDuration , int breakDuration , int numRounds , boolean rated ) {
		
		//find a room Id that has not yet been taken
		for ( int roomId = 0 ; roomId < MAX_ROOMS ; roomId++ ) {
			if ( this.m_rooms[ roomId ] == null ) {
				
				//create the room
				RoomProperties properties = new RoomProperties( roomId , roomName );
				RoomRules rules = new RoomRules( numPlayers , roundDuration , breakDuration , numRounds , rated );
				Room newRoom = new Room( properties , rules );
				this.m_rooms[ roomId ] = newRoom;
				System.out.println( "successfully created room" );
				
				//stop because we have found an unused room Id
				break;
			}
		}
	}
	
	public void destroyRoom( int roomId ) {
		this.m_rooms[ roomId ] = null;
	}
	
	private class SubServer extends Thread {
		
		/**
		 * the unique id associated with this suberver
		 */
		final private int m_id;
		
		/**
		 * if this client has logged in. clients not logged in
		 * cannot perform any actions
		 */
		private boolean m_isLoggedIn = false;
		
		/**
		 * the properties of the user this subserver serves
		 */
		private UserProperties m_properties = null;
		
		/**
		 * the connection to the client
		 */
		final private Communicator m_connection;
		
		/**
		 * creates a SubServer object that processes an individual client's requests
		 * @param id
		 * @param connection
		 */
		public SubServer( int id , Communicator connection ) {
			this.m_id = id;
			this.m_connection = connection;
			start();
		}
		
		@Override
		public void run() {
			while ( !this.isInterrupted() ) {
				String messageFromClient = this.m_connection.read();
				try {
					process( messageFromClient );
				} catch ( Exception e ) {
					//ignore failed request
				}
			}
		}
		
		/**
		 * @return			the properties associated with the user this subserver serves
		 */
		public UserProperties getUserProperties() {
			return this.m_properties;
		}
		
		/**
		 * @return			the connection to the client
		 */
		public Communicator getConnection() {
			return this.m_connection;
		}
		
		/**
		 * processes a message sent by the client
		 * 
		 * @param message			the message sent by the client
		 */
		public void process( String message ) {
			Scanner s = new Scanner( message );
			s.useDelimiter( MessagesReceived.DELIMITER );
			String command = s.next();
			if ( this.m_isLoggedIn ) {
				if ( command.equals( MessagesReceived.CREATE_ROOM ) ) {
					String roomName = s.next();
					int numPlayers = s.nextInt();
					int roundDuration = s.nextInt();
					int breakDuration = s.nextInt();
					int numRounds = s.nextInt();
					boolean rated = s.nextBoolean();
					createRoom( roomName , numPlayers , roundDuration , breakDuration , numRounds , rated );
				} else if ( command.equals( MessagesReceived.JOIN_ROOM ) ) {
					int roomId = s.nextInt();
					joinRoom( roomId );
				}
			} else {
				if ( command.equals( MessagesReceived.LOGIN ) ) {
					String username = s.next();
					String password = s.next();
					login( username , password );
				}
			}
			
			if ( command.equals( MessagesReceived.DISCONNECT ) ) {
				close();
			}
		}
		
		/**
		 * processes the client's login request
		 * 
		 * @param username
		 * @param password
		 */
		public void login( String username , String password ) {
			UserProperties p = new UserProperties( username , password , username );
			this.m_properties = p;
			this.m_isLoggedIn = true;
			MessageWriter.writeLoginSuccess( this.getConnection() );
			System.out.println( "log in success" );
		}
		
		/**
		 * processes the client's request to create a room with the given properties
		 * 
		 * @param roomName
		 * @param numPlayers
		 * @param roundDuration
		 * @param breakDuration
		 * @param numRounds
		 * @param rated
		 */
		public void createRoom( String roomName , int numPlayers , int roundDuration , int breakDuration , int numRounds , boolean rated ) {
			Server.this.createRoom( roomName , numPlayers , roundDuration , breakDuration , numRounds , rated );
			MessageWriter.writeCreateRoomSuccess( this.getConnection() );
		}
		
		/**
		 * processes the client's request to join a room
		 * 
		 * @param roomId		the unique integer Id of the room the client wishes to join
		 */
		public void joinRoom( int roomId ) {
			
			//make sure the room exists
			if ( Server.this.m_rooms[ roomId ] == null ) {
				MessageWriter.writeNonexistingRoom( this.getConnection() );
			} else {
				Server.this.m_rooms[ roomId ].addContestant( this.getConnection() , this.getUserProperties().getDisplayName() );
				MessageWriter.writeJoinRoomSuccess( this.getConnection() );
				//TODO debugging right now
				System.out.println( "successfully joined room" );
			}
		}
		
		/**
		 * terminates this subserver and closes connections with this subserver's
		 * client
		 */
		public void close() {
			
			//stop this thread from running forever
			this.interrupt();
			
			//close connection because we are no longer
			//serving the client
			this.m_connection.close();
			
			//remove this subserver from the list of subservers
			//because this client connection has been closed and
			//will no longer be used
			Server.this.m_subservers[ this.m_id ] = null;
		}
	}
	
	//DEBUG
	final public static void main( String[] args ) {
		Server s = new Server();
		s.addClient( new Communicator() );
	}
}
