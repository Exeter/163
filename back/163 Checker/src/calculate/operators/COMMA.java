package calculate.operators;

import calculate.structures.Expression;
import calculate.structures.Operator;

public class COMMA extends Operator 
{

	final public static String REPRESENTATION = ",";
	final public static int PRIORITY = -1;
	final public static boolean HAS_LEFT_ASSOCIATIVITY = false;
	
	public COMMA()
	{
		super(REPRESENTATION, PRIORITY, HAS_LEFT_ASSOCIATIVITY);
	}

	@Override
	public Expression evaluate(Expression leftSideOperand, Expression rightSideOperand) 
	{
		//TODO remove assert after testing
		assert false;
		return null;
	}
}
