package calculate.operators;

import calculate.structures.Expression;
import calculate.structures.Number;
import calculate.structures.Operator;

/**
 * a special operator ")"
 * @author mjchao
 *
 */
public class CLOSING_PARENTHESIS extends Operator
{

	final public static String REPRESENTATION = ")";
	final public static int PRIORITY = -1;
	final public static boolean HAS_LEFT_ASSOCIATIVITY = false;
	
	public CLOSING_PARENTHESIS()
	{
		super(REPRESENTATION, PRIORITY, HAS_LEFT_ASSOCIATIVITY);
	}

	@Override
	public Number evaluate(Expression leftSideOperand, Expression rightSideOperand) 
	{
		//ignore - this is a special operator - cannot call this method
		assert false;
		return null;
	}
}
