package util;


public class Text 
{

	final public static String NEWLINE = "\n";
	final public static class CALCULATIONS
	{
		final public static String MISSING_CLOSE_PARENTHESIS = "Missing \")\"";
		final public static String MISSING_OPEN_PARENTHESIS = "Missing \"(\"";
		final public static String SYNTAX_ERROR = "Syntax Error";
		
		final public static String getInvalidNumberMessage(String number)
		{
			return "The input \"" + number + "\" is not recognized value or function.";
		}
	}
}
