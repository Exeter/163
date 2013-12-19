package ecc._163.net;

public class Communicator {

	/**
	 * allows for communication with the client
	 */
	public Communicator() {
		
	}
	
	/**
	 * writes a message to the client
	 * 
	 * @param message			the message to send to the client
	 */
	public void write( String message ) {
		//TODO in debugging right now
		System.out.println( "Wrote to client: " + message );
	}
	
	private int i = -1;
	/**
	 * reads a message from the client
	 * 
	 * @return			a message from the client
	 */
	public String read() {
		//TODO For Debugging right now
		this.i++;
		switch( this.i ) {
			case 0:
				return "<LOGIN>`mjchao`12345`";
			case 1:
				return "<CREATE_ROOM>`Mickey's Room`1`60`10`4`false";
			case 2:
				return "<JOIN_ROOM>`0";
			default:
				return "ignore";
		}
	}
	
	/**
	 * closes this connection to the client
	 */
	public void close() {
		//TODO
	}
}
