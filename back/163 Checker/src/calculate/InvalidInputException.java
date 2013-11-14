package calculate;

public class InvalidInputException extends Exception 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final private String m_errorMessage;
	
	public InvalidInputException(String errorMessage)
	{
		this.m_errorMessage = errorMessage;
	}
	
	final public String getErrorMessage()
	{
		return this.m_errorMessage;
	}
}
