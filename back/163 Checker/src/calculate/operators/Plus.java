package calculate.operators;

import calculate.Calculate;
import calculate.structures.Expression;
import calculate.structures.Fraction;
import calculate.structures.Number;
import calculate.structures.Operator;

public class Plus extends Operator 
{

	final public static String PLUS_SIGN = "+";
	final public static int PRIORITY = 1;
	final public static boolean HAS_LEFT_ASSOCIATIVITY = true;
	
	public Plus()
	{
		super(PLUS_SIGN, PRIORITY, HAS_LEFT_ASSOCIATIVITY);
	}
	
	@Override
	final public Expression evaluate(Expression leftSideOperand, Expression rightSideOperand)
	{
		if (Operator.outputMode == Calculate.OUTPUT_EXACT_MODE)
		{
			//try to use the operands as numbers, but treat them as expressions
			//if they are not numbers
			if (leftSideOperand instanceof Number && rightSideOperand instanceof Number)
			{
				//try to use the operands as fractions, but use them as numbers
				//if they are not fractions
				if (leftSideOperand instanceof Fraction && rightSideOperand instanceof Fraction)
				{
					return ((Fraction) leftSideOperand).add((Fraction) rightSideOperand);
				} else
				{
					return new Number( ((Number) leftSideOperand ).add( ((Number) rightSideOperand) ).getValue());
				}
			} else
			{
				return leftSideOperand.add(rightSideOperand);
			}
		} else
		{
			if (leftSideOperand instanceof Number && rightSideOperand instanceof Number)
			{
				return new Number( ((Number) leftSideOperand).add( ((Number) rightSideOperand) ).getValue());
			} else
			{
				//TODO remove this assert after testing
				//should not reach this block.
				//we do not use expressions if we're not in exact mode
				assert false;
				return null;
			}
		}
	}
}
