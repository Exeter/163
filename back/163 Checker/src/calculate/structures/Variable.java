package calculate.structures;

import java.math.BigDecimal;

public class Variable extends Number 
{
	
	public Variable(String representation, BigDecimal value) 
	{
		super(representation, value);
	}
	
	public boolean hasValue()
	{
		return getValue() == null;
	}

	@Override
	final public boolean equals(Object o)
	{
		if (o instanceof Variable)
		{
			return this.getRepresentation().equals(((Variable) o).getRepresentation());
		} else
		{
			return false;
		}
	}

	@Override
	public int hashCode() 
	{
		return super.hashCode();
	}
}
