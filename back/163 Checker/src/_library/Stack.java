package _library;

public class Stack<E> 
{

	private StackElement m_firstElement = null;
	
	public Stack()
	{
		
	}
	
	/**
	 * @return 			if there are anymore values in the stack
	 */
	public boolean hasNext()
	{
		return this.m_firstElement != null;
	}
	
	/**
	 * adds the given value to the top of the stack.
	 * 
	 * @param element		what to add to the stack
	 */
	public void push(E element)
	{
		StackElement elementToAdd = new StackElement(element);
		elementToAdd.setNextElement(this.m_firstElement);
		this.m_firstElement = elementToAdd;
	}
	
	/**
	 * removes the first value on the stack and returns it
	 * 
	 * @return		value at the top of the stack. null if there is nothing in the stack
	 */
	public E pop()
	{
		if (this.m_firstElement == null)
		{
			return null;
		}
		StackElement firstElement = this.m_firstElement;
		this.m_firstElement = this.m_firstElement.getNextElement();
		return firstElement.getValue();
	}
	
	/**
	 * returns the first value on the stack
	 * 
	 * @return		value at the top of the stack. null if there is nothing in the stack
	 */
	public E peek()
	{
		if (this.m_firstElement == null)
		{
			return null;
		}
		StackElement firstElement = this.m_firstElement;
		return firstElement.getValue();
	}

	final private class StackElement
	{
		private E m_value;
		private StackElement m_nextElement = null;
		
		public StackElement(E value)
		{
			this.m_value = value;
		}
		
		public E getValue()
		{
			return this.m_value;
		}
		
		public StackElement getNextElement()
		{
			return this.m_nextElement;
		}
		
		public void setNextElement(StackElement newNextElement)
		{
			this.m_nextElement = newNextElement;
		}
	}
}
