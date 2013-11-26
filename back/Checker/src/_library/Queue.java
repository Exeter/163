package _library;

public class Queue<E>
{
	private QueueElement m_firstElement = null;
	private QueueElement m_lastElement = null;
	
	public Queue()
	{
		
	}
	
	/**
	 * @return		if there are any more elements in this queue
	 */
	public boolean hasNext()
	{
		return this.m_firstElement != null;
	}
	
	/**
	 * adds the given value to the end of the queue
	 * 
	 * @param value		value to add
	 */
	public void push(E value)
	{
		QueueElement newElement = new QueueElement(value);
		if (this.m_lastElement == null)
		{
			this.m_firstElement = newElement;
			this.m_lastElement = newElement;
		} else
		{
			this.m_lastElement.setNext(newElement);
			this.m_lastElement = newElement;
		}
	}
	
	/**
	 * removes and returns the first value in the queue
	 * 
	 * @return		value at the front of the queue
	 */
	public E pop()
	{
		QueueElement elementToRemove = this.m_firstElement;
		this.m_firstElement = elementToRemove.getNext();
		return elementToRemove.getValue();
	}
	
	/**
	 * returns the first value in the queue
	 * 
	 * @return		value at the front of the queue
	 */
	public E peek()
	{
		return this.m_firstElement.getValue();
	}
	
	final private class QueueElement
	{
		
		private E m_value;
		private QueueElement m_nextElement = null;
		
		public QueueElement(E value)
		{
			this.m_value = value;
		}
		
		public E getValue()
		{
			return this.m_value;
		}
		
		public QueueElement getNext()
		{
			return this.m_nextElement;
		}
		
		public void setNext(QueueElement nextElement)
		{
			this.m_nextElement = nextElement;
		}
	}
}
