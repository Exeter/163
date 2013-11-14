package calculate.structures;

import calculate.Calculate;


abstract public class Operator extends Token 
{
	
	/**
	 * order in which this operator should be used. for example, plus has lower
	 * priority than multiply.
	 */
	final protected int m_priority;
	final protected boolean m_hasLeftAssociativity;
	
	public static int outputMode = Calculate.DEFAULT_OUTPUT_MODE;
	
	public Operator(String representation, int priority, boolean hasLeftAssociativity)
	{
	 	super(representation.trim());
	 	this.m_priority = priority;
	 	this.m_hasLeftAssociativity = hasLeftAssociativity;
	}
	
	/**
	 * @return			the priority of this operator. in other words, the order in which
	 * 					this operator should be used.
	 * @see				#m_priority
	 */
	final public int getPriority()
	{
		return this.m_priority;
	}
	
	/**
	 * @return			if the operator is left associative, in other words, if it must be
	 * 					evaluated from left to right. for example, 5^6^7 is 5^(6^7) - which
	 * 					is left associative
	 */
	final public boolean hasLeftAssociativity()
	{
		return this.m_hasLeftAssociativity;
	}
	
	/**
	 * evaluates an expression in the format [operand] [operator] [operand]
	 * 
	 * @param leftSideOperand			the operand on the left side of this operator
	 * @param rightSideOperand			the operand on the right side of this operator
	 * @return							the value of the expression [operand] [operator] [operand]
	 */
	abstract public Expression evaluate(Expression leftSideOperand, Expression rightSideOperand);
}
