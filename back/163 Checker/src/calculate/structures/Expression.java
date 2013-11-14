package calculate.structures;

import java.math.BigDecimal;

import calculate.Calculate;
import calculate.operators.CLOSING_PARENTHESIS;
import calculate.operators.OPENING_PARENTHESIS;

/**
 * A mathematical expression with or without a known value
 */
public class Expression extends Token 
{

	public static int floatingPoint = 12;
	/**
	 * part of the expression with a known value
	 */
	private Number m_numericalPart = null;
	/**
	 * the operator on the numerical part and unknown part
	 */
	private Operator m_operator = null;
	/**
	 * part of the expression without a known value
	 */
	private String m_unevaluatedPart = null;
	
	/**
	 * Constructor. Used when everything has an unknown exact value, for example
	 * log(2) or e^5.
	 * 
	 * @param representation			textual representation of the expression
	 */
	public Expression(String representation) 
	{
		super(representation);
		this.m_unevaluatedPart = representation;
	}
	
	/**
	 * Constructor. Used when a number is created and it doesn't have an indeterminable
	 * expression in it.
	 * 
	 * @param numericalPart			a number
	 */
	public Expression(BigDecimal numericalPart)
	{
		super(numericalPart.stripTrailingZeros().toString());
		this.m_numericalPart = new Number(numericalPart.stripTrailingZeros().toString());
	}
	
	/**
	 * Constructor. User when combining a fractional, exact expression with a known
	 * exact value.
	 * 
	 * @param fractionalPart			a fractional expression
	 * @param operator					an operator
	 * @param unknownPart				the indeterminable part of the expression
	 */
	public Expression(Fraction fractionalPart, Operator operator, Expression unknownPart)
	{
		super(OPENING_PARENTHESIS.REPRESENTATION + fractionalPart.getExactFractionalValue() + CLOSING_PARENTHESIS.REPRESENTATION
				+ operator.getRepresentation()
				+ OPENING_PARENTHESIS.REPRESENTATION + unknownPart.getRepresentation() + CLOSING_PARENTHESIS.REPRESENTATION);
		if (unknownPart.getNumericalPart() != null)
		{
			this.m_numericalPart = (Fraction) operator.evaluate(fractionalPart, unknownPart.getNumericalPart());
			this.m_unevaluatedPart = unknownPart.getUnevaluatedPart();
		}
	}

	/**
	 * Constructor. Used when combining a numerical expression with a known exact value
	 * with an expression with an unknown exact value. for example, combining
	 * 25/3 + log(3).
	 * 
	 * @param numericalPart				a numerical expression with known exact value
	 * @param operator					an operator that operates on the numerical and unknown part
	 * @param unknownPart				an expression with an indeterminable exact value
	 */
	public Expression(Number numericalPart, Operator operator, Expression unknownPart)
	{
		super(OPENING_PARENTHESIS.REPRESENTATION + numericalPart.getRepresentation() + CLOSING_PARENTHESIS.REPRESENTATION 
				+ operator.getRepresentation() 
				+ OPENING_PARENTHESIS.REPRESENTATION + unknownPart.getRepresentation() + CLOSING_PARENTHESIS.REPRESENTATION);
		if (unknownPart.getNumericalPart() != null)
		{
			this.m_numericalPart = numericalPart.add(unknownPart.getNumericalPart());
			this.m_unevaluatedPart = unknownPart.getRepresentation();
		}
	}
	
	/**
	 * Constructor. Used when combining two expressions with unknown exact values.
	 * for example, combining log(2) with log(10).
	 * 
	 * @param leftOperandExpression
	 * @param operator
	 * @param rightOperandExpression
	 */
	public Expression(String leftOperandExpression, Operator operator, String rightOperandExpression)
	{
		super(OPENING_PARENTHESIS.REPRESENTATION + leftOperandExpression + CLOSING_PARENTHESIS.REPRESENTATION  
				+ operator.getRepresentation() 
				+ OPENING_PARENTHESIS.REPRESENTATION + rightOperandExpression + CLOSING_PARENTHESIS.REPRESENTATION);
	}
	
	public Number getNumericalPart()
	{
		return this.m_numericalPart;
	}
	
	public Operator getOperator()
	{
		return this.m_operator;
	}
	
	public String getUnevaluatedPart()
	{
		return this.m_unevaluatedPart;
	}
	
	public Expression add(Expression augend)
	{
		//load this expression's data
		Number thisNumericalPart = this.m_numericalPart;
		String thisExpressionPart = this.m_unevaluatedPart;
		//load the other expression's data
		Number otherNumericalPart = augend.getNumericalPart();
		String otherExpressionPart = augend.getUnevaluatedPart();
		//add them together
		Number addedNumericalPart = null;
		//if one numerical part is nonexistent, then the result will be the
		//value of the other numerical part
		if (thisNumericalPart == null && otherNumericalPart != null)
		{
			addedNumericalPart = otherNumericalPart;
		} else if (thisNumericalPart != null && otherNumericalPart == null)
		{
			addedNumericalPart = thisNumericalPart;
		} else if (thisNumericalPart != null && otherNumericalPart != null)
		{
			addedNumericalPart = thisNumericalPart.add(otherNumericalPart);
		}
		//if one expression part is nonexistent, then the result will be the value of
		//the other expression part
		Expression addedExpressionPart = null;
		if (thisExpressionPart == null && otherExpressionPart != null)
		{
			addedExpressionPart = new Expression(otherExpressionPart);
		} else if (otherExpressionPart == null && thisExpressionPart != null)
		{
			addedExpressionPart = new Expression(thisExpressionPart);
		} else if (thisExpressionPart != null && otherExpressionPart != null)
		{
			addedExpressionPart = new Expression(thisExpressionPart, Calculate.PLUS, otherExpressionPart);
		}
		//if only one of the two parts exist, return the one with the existing part
		//if both parts exist, add them together
		//if neither part exists, then there's an error
		if (addedNumericalPart == null)
		{
			return addedExpressionPart;
		} else if (addedExpressionPart == null)
		{
			return addedNumericalPart;
		} else
		{
			return new Expression(addedNumericalPart, Calculate.PLUS, addedExpressionPart);
		}
	}
	
	public Expression subtract(Expression subtrahend)
	{
		//load this expression's data
		Number thisNumericalPart = this.m_numericalPart;
		String thisUnevaluatedPart = this.m_unevaluatedPart;
		//load the subtrahend's data
		Number subtrahendNumericalPart = subtrahend.getNumericalPart();
		String subtrahendUnevaluatedPart = subtrahend.getUnevaluatedPart();
		//combine the numerical parts
		Number subtractedNumericalPart = null;
		if (thisNumericalPart == null)
		{
			subtractedNumericalPart = subtrahendNumericalPart;
		} else if (subtrahendNumericalPart == null)
		{
			subtractedNumericalPart = thisNumericalPart;
		} else
		{
			subtractedNumericalPart = thisNumericalPart.subtract(subtrahendNumericalPart);
		}
		//combine the unevaluated parts
		//once again, we assume that at least one of the two parts must exit
		Expression subtractedUnevaluatedPart = null;
		if (thisUnevaluatedPart == null && subtrahendUnevaluatedPart != null)
		{
			subtractedUnevaluatedPart = new Expression(subtrahendUnevaluatedPart);
		} else if (subtrahendUnevaluatedPart == null && thisUnevaluatedPart != null)
		{
			subtractedUnevaluatedPart = new Expression(thisUnevaluatedPart);
		} else if (thisUnevaluatedPart != null && subtrahendUnevaluatedPart != null)
		{
			subtractedUnevaluatedPart = new Expression(thisUnevaluatedPart, Calculate.MINUS, subtrahendUnevaluatedPart);
		}
		//subtract the numerical and unevaluated parts
		if (subtractedNumericalPart == null)
		{
			return subtractedUnevaluatedPart;
		} else if (subtractedUnevaluatedPart == null)
		{
			return subtractedNumericalPart;
		} else
		{
			return new Expression(subtractedNumericalPart, Calculate.MINUS, subtractedUnevaluatedPart);
		}
	}
	
	public Expression multiply(Expression multiplicand)
	{
		//load this expression's data
		Number thisNumericalPart = this.m_numericalPart;
		String thisUnevaluatedPart = this.m_unevaluatedPart;
		//load the multiplicand's data
		Number multiplicandNumericalPart = multiplicand.getNumericalPart();
		String multiplicandUnevaluatedPart = multiplicand.getUnevaluatedPart();
		//add the numerical parts together
		Number multipliedNumericalPart = null;
		if (thisNumericalPart == null && multiplicandNumericalPart != null)
		{
			multipliedNumericalPart = multiplicandNumericalPart;
		} else if (thisNumericalPart != null && multiplicandNumericalPart == null)
		{
			multipliedNumericalPart = thisNumericalPart;
		} else if (thisNumericalPart != null && multiplicandNumericalPart != null)
		{
			multipliedNumericalPart = thisNumericalPart.multiply(multiplicandNumericalPart);
		}
		//add the unevaluated parts together
		Expression multipliedUnevaluatedPart = null;
		if (thisUnevaluatedPart == null && multiplicandUnevaluatedPart != null)
		{
			multipliedUnevaluatedPart = new Expression(multiplicandUnevaluatedPart);
		} else if (multiplicandUnevaluatedPart == null && thisUnevaluatedPart != null)
		{
			multipliedUnevaluatedPart = new Expression(thisUnevaluatedPart);
		} else if (thisUnevaluatedPart != null && multiplicandUnevaluatedPart != null)
		{
			multipliedUnevaluatedPart = new Expression(thisUnevaluatedPart, Calculate.MULTIPLY, multiplicandUnevaluatedPart);
		}
		//multiply the numerical and unevaluated parts
		if (multipliedNumericalPart == null && multipliedUnevaluatedPart != null)
		{
			return multipliedUnevaluatedPart;
		} else if (multipliedNumericalPart != null && multipliedUnevaluatedPart == null)
		{
			return multipliedNumericalPart;
		} else if (multipliedNumericalPart != null && multipliedUnevaluatedPart != null)
		{
			return new Expression(multipliedNumericalPart, Calculate.MULTIPLY, multipliedUnevaluatedPart);
		} else
		{
			//TODO remove assert after testing
			assert false;
			return null;
		}
	}
	
	public Expression divide(Expression dividend)
	{
		//load this expression's data
		Number thisNumericalPart = this.m_numericalPart;
		String thisUnevaluatedPart = this.m_unevaluatedPart;
		//load dividend expression's data
		Number dividendNumericalPart = dividend.getNumericalPart();
		String dividendUnevaluatedPart = dividend.getUnevaluatedPart();
		//divide the numerical parts
		Number dividedNumericalPart = null;
		if (thisNumericalPart == null && dividendNumericalPart != null)
		{
			dividedNumericalPart = dividendNumericalPart;
		} else if (thisNumericalPart != null && dividendNumericalPart == null)
		{
			dividedNumericalPart = thisNumericalPart;
		} else if (thisNumericalPart != null && dividendNumericalPart != null)
		{
			dividedNumericalPart = thisNumericalPart.divide(dividendNumericalPart);
		}
		//divide the unevaluated parts
		Expression dividedUnevaluatedPart = null;
		if (thisUnevaluatedPart == null && dividendUnevaluatedPart != null)
		{
			dividedUnevaluatedPart = new Expression(dividendUnevaluatedPart);
		} else if (thisUnevaluatedPart != null && dividendUnevaluatedPart == null)
		{
			dividedUnevaluatedPart = new Expression(thisUnevaluatedPart);
		} else if (thisUnevaluatedPart != null && dividendUnevaluatedPart != null)
		{
			dividedUnevaluatedPart = new Expression(thisUnevaluatedPart, Calculate.DIVIDE, dividendUnevaluatedPart);
		}
		//combine for final divided expression
		if (dividedNumericalPart == null)
		{
			return dividedUnevaluatedPart;
		} else if (dividedUnevaluatedPart == null)
		{
			return dividedNumericalPart;
		} else
		{
			return new Expression(dividedNumericalPart, Calculate.DIVIDE, dividedUnevaluatedPart);
		}
	}
}
