package calculate.operators;

import calculate.Calculate;
import calculate.structures.Expression;
import calculate.structures.Fraction;
import calculate.structures.Number;
import calculate.structures.Operator;

public class Multiply extends Operator 
{

	final public static String MULTIPLICATION_SIGN = "*";
	final public static int PRIORITY = 2;
	final public static boolean HAS_LEFT_ASSOCIATIVITY = true;
	
	public Multiply()
	{
		super(MULTIPLICATION_SIGN, PRIORITY, HAS_LEFT_ASSOCIATIVITY);
	}

	@Override
	public Expression evaluate(Expression leftSideOperand, Expression rightSideOperand) 
	{
		if (Operator.outputMode == Calculate.OUTPUT_EXACT_MODE)
		{
			//try to use operands as fractions. if they are not fractions, then try to
			//use them as numbers. if still can't use them as numbers, then use them as
			//expressions
			if (leftSideOperand instanceof Number && rightSideOperand instanceof Number)
			{
				if (leftSideOperand instanceof Fraction && rightSideOperand instanceof Fraction)
				{
					return ( (Fraction) leftSideOperand ).multiply( (Fraction) rightSideOperand);
				} else
				{
					return new Number( ((Number) leftSideOperand ).multiply( (Number) rightSideOperand).getValue());
				}
			} else
			{
				if (leftSideOperand instanceof Fraction)
				{
					return ((Fraction) leftSideOperand).multiply(rightSideOperand);
				} else
				{
					return leftSideOperand.multiply(rightSideOperand);
				}
			}
		} else
		{
			if (leftSideOperand instanceof Number && rightSideOperand instanceof Number)
			{
				return ((Number) leftSideOperand).multiply((Number) rightSideOperand);
			} else
			{
				//TODO remove assert after testing
				assert false;
				return null;
			}
		}
	}
}
