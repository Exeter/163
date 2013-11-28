package calculate.structures;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A mathematical expression with a known value
 */
public class Number extends Expression
{
	
	final public static String NEGATIVE_SIGN = "-";
	final private BigDecimal m_value;
	final private Expression m_exactValue;
	
	/**
	 * Constructor. Takes some text and converts it into a number, if possible.
	 * Since numerical representations do not have spaces before or after, this method
	 * removes them if there are any.
	 *  
	 * @param representation		the value of this number and also the same representation.
	 * 								for example, three is represented by "3" and has value "3".
	 */
	public Number(String representation)
	{
		super(representation.trim());
		this.m_value = new BigDecimal(representation.trim()).stripTrailingZeros();
		this.m_exactValue = new Expression(this.m_representation);
	}
	
	public Number(BigDecimal value)
	{
		super(value.stripTrailingZeros());
		this.m_value = value.stripTrailingZeros();
		this.m_exactValue = new Expression(this.m_value.toString());
	}
	
	/**
	 * 
	 * @param representation		the symbol that represents this number. for example, pi represents pi
	 * @param value					the value of this number. for example, 3.1415926535... is the value of pi
	 */
	public Number(String representation, BigDecimal value)
	{
		super(value.stripTrailingZeros());
		this.m_representation = representation;
		this.m_value = value.stripTrailingZeros();
		this.m_exactValue = new Expression(this.m_value.toString());
	}
	
	public Number(String representation, BigDecimal approximateValue, Expression exactValue)
	{
		super(representation);
		this.m_value = approximateValue;
		this.m_exactValue = exactValue;
	}
	
	/**
	 * @return 		numerical value of this number
	 */
	final public BigDecimal getValue()
	{
		return this.m_value;
	}
	
	final public Expression getExactValue()
	{
		return this.m_exactValue;
	}
	
	public Number add(Number anotherNumber)
	{
		return new Number(this.m_value.add(anotherNumber.getValue()));
	}
	
	public Number subtract(Number anotherNumber)
	{
		return new Number(this.m_value.subtract(anotherNumber.getValue()));
	}
	
	public Number multiply(Number anotherNumber)
	{
		return new Number(this.m_value.multiply(anotherNumber.getValue()));
	}
	
	public Number divide(Number anotherNumber)
	{
		try
		{
			return new Number(this.m_value.divide(anotherNumber.getValue()));
		} catch (ArithmeticException nonTerminatingException)
		{
			return new Number(this.m_value.divide(anotherNumber.getValue(), floatingPoint, RoundingMode.HALF_UP));
		}
	}
	
	public Number mod(Number anotherNumber)
	{
		return new Number(this.m_value.remainder(anotherNumber.getValue()));
	}
	
	public Number pow(Number anotherNumber)
	{
		double doubleRepresentation = Math.pow(this.m_value.doubleValue(), anotherNumber.getValue().doubleValue());
		return new Number(new BigDecimal(doubleRepresentation));
	}
}
