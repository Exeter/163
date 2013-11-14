package calculate.structures;

import calculate.Calculate;


/**
 * template for a function. child classes should define an evaluate method that
 * takes inputs and produces an output
 */
abstract public class Function extends Token 
{
	/**
	 * priority of all functions are the same and greater than the priority
	 * of any operator 
	 */
	final public static int FUNCTION_PRIORITY = 1000000;
	
	final public static int DEGREE_MODE = 0;
	final public static int RADIAN_MODE = 1;
	final public static int GRADIAN_MODE = 2;
	/**
	 * how angles are measured - degrees, radians or gradians
	 */
	public static int MODE = DEGREE_MODE;
	
	/**
	 * number of arguments this function takes, e.g. 1 for cosine or 2 for permutation
	 */
	final private int m_numberOfArguments;
	
	public static int outputMode = Calculate.DEFAULT_OUTPUT_MODE;
	
	public Function(String functionName, int numberOfArguments)
	{
		super(functionName.trim());
		this.m_numberOfArguments = numberOfArguments;
	}
	
	/**
	 * determines if a given sequence of characters is the name of this function
	 * 
	 * @param stringToCheck			sequence of characters to check
	 * @return						if the function name matches the given input
	 */
	final public boolean matchesFunctionName(String stringToCheck)
	{
		return this.m_representation.equals(stringToCheck);
	}
	
	/**
	 * @return			number of arguments this function takes
	 */
	final public int getNumberOfArguments()
	{
		return this.m_numberOfArguments;
	}
	
	abstract public Expression evaluate(Expression[] functionInputs);
	
}
