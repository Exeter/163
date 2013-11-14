package calculate.structures;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import calculate.Calculate;

public class Fraction extends Number 
{	
	final private static BigDecimal NEGATIVE_ONE = new BigDecimal("-1");
	final private static BigDecimal ZERO = new BigDecimal("0");
	final private static BigDecimal ONE = new BigDecimal("1");
	final public static BigDecimal ONE_DENOMINATOR = ONE;
	final private static BigDecimal TEN = new BigDecimal("10");
	private BigInteger m_numerator;
	private BigInteger m_denominator;
	private boolean m_isNegative = false;
	
	public Fraction(BigInteger numerator, BigInteger denominator, boolean isNegative)
	{
		super(new BigDecimal(numerator.doubleValue() / denominator.doubleValue()));
		this.m_isNegative = isNegative;
		this.m_numerator = numerator;
		this.m_denominator = denominator;
		simplify();
	}
	
	public Fraction(BigDecimal numerator, BigDecimal denominator)
	{
		super(numerator.divide(denominator, floatingPoint, RoundingMode.HALF_UP));
		if (numerator.compareTo(ZERO) < 0)
		{
			this.m_isNegative = !this.m_isNegative;
		}
		if (denominator.compareTo(ZERO) < 0)
		{
			this.m_isNegative = !this.m_isNegative;
		}
		BigDecimal modifiedNumerator = numerator;
		BigDecimal modifiedDenominator = denominator;
		if (modifiedNumerator.compareTo(ZERO) == 0)
		{
			modifiedNumerator = ZERO;
		}
		if (modifiedDenominator.compareTo(ZERO) == 0)
		{
			modifiedDenominator = ZERO;
		}
		while (modifiedNumerator.ulp().compareTo(ONE) < 0 || modifiedDenominator.ulp().compareTo(ONE) < 0)
		{
			modifiedNumerator = modifiedNumerator.multiply(TEN).stripTrailingZeros();
			modifiedDenominator = modifiedDenominator.multiply(TEN).stripTrailingZeros();
		}
		this.m_numerator = modifiedNumerator.toBigIntegerExact().abs();
		this.m_denominator = modifiedDenominator.toBigIntegerExact().abs();
		simplify();
	}
	
	final public BigInteger getNumerator()
	{
		return this.m_numerator;
	}
	
	final public BigInteger getDenominator()
	{
		return this.m_denominator;
	}
	
	final public boolean isNegative()
	{
		return this.m_isNegative;
	}
	
	final public boolean isInteger()
	{
		return this.m_denominator.compareTo(ONE.toBigIntegerExact()) == 0;
	}
	
	final public String getExactFractionalValue()
	{
		String rtn = "";
		if (this.m_isNegative)
		{
			rtn = Number.NEGATIVE_SIGN;
		}
		if (this.m_denominator.compareTo(ONE.toBigIntegerExact()) == 0)
		{
			return rtn + this.m_numerator.toString();
		} else
		{
			return rtn + this.m_numerator.toString() + "/" + this.m_denominator.toString();
		}
	}
	
	final public void simplify()
	{
		BigInteger gcd = gcd(this.m_numerator, this.m_denominator);
		this.m_numerator = this.m_numerator.divide(gcd);
		this.m_denominator = this.m_denominator.divide(gcd);
	}
	
	/**
	 * finds the greatest common divisor of two numbers a and b
	 * 
	 * @param a			an integer
	 * @param b			another integer
	 * @return			the greatest common divisor of a and b
	 */
	final private static BigInteger gcd(BigInteger a, BigInteger b)
	{
		if (b.compareTo(ZERO.toBigIntegerExact()) == 0)
		{
			return a;
		} else
		{
			return gcd(b, a.mod(b));
		}
	}
	
	/**
	 * finds the lowest common multiple of two numbers a and b
	 * 
	 * @param a			an integer
	 * @param b			another integer
	 * @return			the lowest common multiple of a and b
	 */
	final private static BigInteger lcm(BigInteger a, BigInteger b)
	{
		BigInteger gcd = gcd(a, b);
		return a.multiply(b).divide(gcd);
	}
	
	final public Fraction add(Fraction anotherFraction)
	{
		//determine the lowest common denominator
		BigInteger newDenominator = lcm(this.m_denominator, anotherFraction.getDenominator());
		//calculate the multipler for this fraction
		
		BigInteger thisMultiplier = newDenominator.divide(this.m_denominator);
		if (this.m_isNegative)
		{
			thisMultiplier = thisMultiplier.multiply(NEGATIVE_ONE.toBigIntegerExact());
		}
		//determine the new numerator using the lowest common denominator
		BigInteger thisNumerator = this.m_numerator.multiply(thisMultiplier);
		
		//calculate the multiplier for the augend
		BigInteger augendMultiplier = newDenominator.divide(anotherFraction.getDenominator());
		if (anotherFraction.isNegative())
		{
			augendMultiplier = augendMultiplier.multiply(NEGATIVE_ONE.toBigIntegerExact());
		}
		//determine the new augend numerator when using the lowest common denominator
		BigInteger augendNumerator = anotherFraction.getNumerator().multiply(augendMultiplier);
		
		//determine the numerator of the new fraction
		BigInteger newNumerator = thisNumerator.add(augendNumerator);
		//determine the sign of the fraction
		boolean newIsNegative = false;
		if (newNumerator.compareTo(ZERO.toBigIntegerExact()) < 0)
		{
			newIsNegative = true;
		}
		return new Fraction(newNumerator.abs(), newDenominator, newIsNegative);
	}
	
	@Override
	final public Fraction add(Number anotherNumber)
	{
		//convert the augend to a fraction
		Fraction augendFractionalEquivalent = new Fraction(anotherNumber.getValue(), ONE);
		return add(augendFractionalEquivalent);
	}
	
	@Override
	final public Expression add(Expression anotherExpression)
	{
		return new Expression(this, Calculate.PLUS, anotherExpression);
	}
	
	final public Fraction subtract(Fraction anotherFraction)
	{
		//determine the lowest common denominator
		BigInteger newDenominator = lcm(this.m_denominator, anotherFraction.getDenominator());
		//calculate the multipler for this fraction
		
		BigInteger thisMultiplier = newDenominator.divide(this.m_denominator);
		if (this.m_isNegative)
		{
			thisMultiplier = thisMultiplier.multiply(NEGATIVE_ONE.toBigIntegerExact());
		}
		//determine the new numerator using the lowest common denominator
		BigInteger thisNumerator = this.m_numerator.multiply(thisMultiplier);
		
		//calculate the multiplier for the subtrahend
		BigInteger subtrahendMultiplier = newDenominator.divide(anotherFraction.getDenominator());
		if (anotherFraction.isNegative())
		{
			subtrahendMultiplier = subtrahendMultiplier.multiply(NEGATIVE_ONE.toBigIntegerExact());
		}
		//determine the new subtrahend numerator when using the lowest common denominator
		BigInteger subtrahendNumerator = anotherFraction.getNumerator().multiply(subtrahendMultiplier);
		
		//determine the numerator of the new fraction
		BigInteger newNumerator = thisNumerator.subtract(subtrahendNumerator);
		//determine the sign of the fraction
		boolean newIsNegative = false;
		if (newNumerator.compareTo(ZERO.toBigIntegerExact()) < 0)
		{
			newIsNegative = true;
		}
		return new Fraction(newNumerator.abs(), newDenominator, newIsNegative);
	}
	
	@Override
	final public Fraction subtract(Number anotherNumber)
	{
		//convert the subtrahend to a fraction
		Fraction subtrahendFractionalEquivalent = new Fraction(anotherNumber.getValue(), ONE);
		return subtract(subtrahendFractionalEquivalent);
	}
	
	@Override
	final public Expression subtract(Expression anotherExpression)
	{
		return new Expression(this, Calculate.MINUS, anotherExpression);
	}
	
	final public Fraction multiply(Fraction anotherFraction)
	{
		//multiply the numerator of this fraction and the numerator of the multiplicand
		BigInteger newNumerator = this.m_numerator.multiply(anotherFraction.getNumerator()).abs();
		//multiply this fraction's denominator with the multiplicand's denominator
		BigInteger newDenominator = this.m_denominator.multiply(anotherFraction.getDenominator()).abs();
		//determine if the product is positive or negative
		boolean newIsNegative = false;
		if (this.m_isNegative)
		{
			newIsNegative = !newIsNegative;
		}
		if (anotherFraction.isNegative())
		{
			newIsNegative = !newIsNegative;
		}
		return new Fraction(newNumerator, newDenominator, newIsNegative);
	}
	
	@Override
	final public Fraction multiply(Number anotherNumber)
	{
		//convert the multiplicand to a fraction
		Fraction multiplicandFractionalEquivalent = new Fraction(anotherNumber.getValue(), ONE);
		return multiply(multiplicandFractionalEquivalent);
	}
	
	@Override
	final public Expression multiply(Expression anotherExpression)
	{
		return new Expression(this, Calculate.MULTIPLY, anotherExpression);
	}
	
	final public Fraction divide(Fraction anotherFraction)
	{
		//multiply numerator of this fraction with denominator of other fraction to get new numerator
		BigInteger newNumerator = this.m_numerator.multiply(anotherFraction.getDenominator());
		//multiply denominator of this fraction with numerator of other fraction to get new denominator
		BigInteger newDenominator = this.m_denominator.multiply(anotherFraction.getNumerator());
		//determine if result is positive or negative
		boolean newIsNegative = false;
		if (this.m_isNegative)
		{
			newIsNegative = !newIsNegative;
		}
		if (anotherFraction.isNegative())
		{
			newIsNegative = !newIsNegative;
		}
		return new Fraction(newNumerator, newDenominator, newIsNegative);
	}
	
	@Override
	final public Fraction divide(Number anotherNumber)
	{
		Fraction divisorFractionalEquivalent = new Fraction(anotherNumber.getValue(), ONE);
		return divide(divisorFractionalEquivalent);
	}
	
	@Override
	final public Expression divide(Expression anotherExpression)
	{
		return new Expression(this, Calculate.DIVIDE, anotherExpression);
	}
	
	final public Fraction mod(Fraction anotherFraction)
	{
		Fraction quotient = divide(anotherFraction);
		boolean isQuotientNegative = quotient.isNegative();
		BigInteger integerQuotient = quotient.getValue().toBigInteger();
		Fraction remainder = subtract(anotherFraction.multiply(new Fraction(integerQuotient, ONE.toBigIntegerExact(), isQuotientNegative)));
		return remainder;
	}
	
	@Override
	final public Fraction mod(Number anotherNumber)
	{
		Fraction modulusFractionalEquivalent = new Fraction(anotherNumber.getValue(), ONE);
		return mod(modulusFractionalEquivalent);
	}
	
	final public Fraction powInteger(Number anInteger)
	{
		int exponent = Integer.parseInt(anInteger.getValue().stripTrailingZeros().toString());
		//calculate new numerator
		BigInteger newNumerator = this.m_numerator.pow(exponent);
		BigInteger newDenominator = this.m_denominator.pow(exponent);
		boolean newIsNegative = false;
		if (newNumerator.compareTo(ZERO.toBigIntegerExact()) < 0)
		{
			newIsNegative = !newIsNegative;
		}
		if (newDenominator.compareTo(ZERO.toBigIntegerExact()) < 0)
		{
			newIsNegative = !newIsNegative;
		}
		return new Fraction(newNumerator, newDenominator, newIsNegative);
	}
}
