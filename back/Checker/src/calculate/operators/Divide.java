package calculate.operators;

import calculate.Calculate;
import calculate.structures.Expression;
import calculate.structures.Fraction;
import calculate.structures.Number;
import calculate.structures.Operator;

public class Divide extends Operator 
{

	final public static String DIVISION_SIGN = "/";
	final public static int PRIORITY = 2;
	final public static boolean HAS_LEFT_ASSOCIATIVITY = true;
	
	public Divide()
	{
		super(DIVISION_SIGN, PRIORITY, HAS_LEFT_ASSOCIATIVITY);
	}

	@Override
	public Expression evaluate(Expression leftSideOperand, Expression rightSideOperand) 
	{
		if (Operator.outputMode == Calculate.OUTPUT_EXACT_MODE)
		{
			//try to use operands as exact fractions, but use them as numbers if not possible
			//if still not possible to use operands as numbers, then use them as expressions
			if (leftSideOperand instanceof Number && rightSideOperand instanceof Number)
			{
				if (leftSideOperand instanceof Fraction && rightSideOperand instanceof Fraction)
				{
					return ( (Fraction) leftSideOperand ).divide( (Fraction) rightSideOperand);
				} else
				{
					return new Number( ( (Number) leftSideOperand ).divide( (Number) rightSideOperand).getValue());
				}
			} else
			{
				return leftSideOperand.divide(rightSideOperand);
			}
		} else
		{
			if (leftSideOperand instanceof Number && rightSideOperand instanceof Number)
			{
				return new Number( ((Number) leftSideOperand).divide( ((Number) rightSideOperand) ).getValue());
			} else
			{
				//TODO remove assert
				assert false;
				return null;
			}
		}
	}
}
