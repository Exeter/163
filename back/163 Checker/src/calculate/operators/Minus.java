package calculate.operators;

import calculate.Calculate;
import calculate.structures.Expression;
import calculate.structures.Fraction;
import calculate.structures.Number;
import calculate.structures.Operator;

public class Minus extends Operator 
{
	
	final public static String MINUS_SIGN = "-";
	final public static int PRIORITY = 1;
	final public static boolean HAS_LEFT_ASSOCIATIVITY = true; 
	
	public Minus()
	{
		super(MINUS_SIGN, PRIORITY, HAS_LEFT_ASSOCIATIVITY);
	}

	@Override
	public Expression evaluate(Expression leftSideOperand, Expression rightSideOperand) 
	{
		if (Operator.outputMode == Calculate.OUTPUT_EXACT_MODE)
		{
			//try to evaluate using operands as fractions first. if fails, try to use them
			//as numbers. if still fails, evaluate treating operands as expressions
			if (leftSideOperand instanceof Number && rightSideOperand instanceof Number)
			{
				if (leftSideOperand instanceof Fraction && rightSideOperand instanceof Fraction)
				{
					return ( (Fraction) leftSideOperand ).subtract( (Fraction) rightSideOperand );
				} else
				{
					return new Number( ((Number) leftSideOperand).subtract( (Number) rightSideOperand).getValue());
				}
			} else
			{
				return leftSideOperand.subtract(rightSideOperand);
			}
		} else
		{
			if (leftSideOperand instanceof Number && rightSideOperand instanceof Number)
			{
				return new Number( ((Number) leftSideOperand ).subtract( ((Number) rightSideOperand )).getValue());
			} else
			{
				//TODO remove assert after testing
				assert false;
				return null;
			}
		}
	}
}
