package calculate.structures;

/**
 * a part of a mathematical expression to be read by the computer
 *
 */
public class Token 
{

	/**
	 * identifies this token. for example, "+" identifies addition, and "sin" identifies
	 * sine.
	 */
	protected String m_representation;
	
	public Token(String representation)
	{
		this.m_representation = representation;
	}
	
	public String getRepresentation()
	{
		return this.m_representation;
	}
	
	/**
	 * 
	 * @param token1		one token
	 * @param token2		another token
	 * @return				if the two given tokens have the same representation. For example,
	 * 						"sin" and "sin" will return true, but "1" and "2" will not.
	 */
	final static public boolean areTokensIdentical(Token token1, Token token2)
	{
		return token1.getRepresentation().equals(token2.getRepresentation());
	}
}
