package _library;

/**
 * A list of connected elements. Each element only knows the element following it.
 * The start of the list is the first element and the end of the list is the last element.
 * This list uses a header link just for implementation convenience, but the header link
 * does not affect usage of the linked list.
 * 
 * @param <E>		the type of element in this list
 */
public class LinkedList<E> 
{
	//Linked List properties:
	/**
	 * number of links the user added (excludes head link)
	 */
	private int m_numberOfUserDefinedLinks = 0;
	
	
	final private Link m_headerLink = new Link((E) null);
	private Link m_lastLinkInList = null;
	private Link m_previousLink = null;
	private Link m_currentLink = null;
	
	
	public LinkedList()
	{
		
	}
	
	/**
	 * adds an element to the end of the linked list
	 * 
	 * @param newElement		value to be added
	 */
	public void addElement(E newElement)
	{
		Link newLinkToAdd = new Link(newElement, null);
		if (this.m_lastLinkInList == null)
		{
			this.m_previousLink = this.m_headerLink;
			this.m_headerLink.setNextLink(newLinkToAdd);
			this.m_currentLink = newLinkToAdd;
			this.m_lastLinkInList = newLinkToAdd;
		} else
		{
			this.m_lastLinkInList.setNextLink(newLinkToAdd);
			this.m_lastLinkInList = newLinkToAdd;
		}
		this.m_numberOfUserDefinedLinks++;
	}
	
	/**
	 * inserts an element between the current element and the current next element.
	 * for example, if the current list is 4, 5, 7, 8, 9 and the current element is 5,
	 * if we call insert(6), the list becomes 4, 5, 6, 7, 8, 9
	 * 
	 * @param newElement		new element to insert
	 */
	public void insert(E newElement)
	{
		Link linkBefore = this.m_currentLink;
		Link linkAfter = this.m_currentLink.getNextLink();
		Link linkToInsert = new Link(newElement, linkAfter);
		linkBefore.setNextLink(linkToInsert);
	}
	
	/**
	 * gets the value of the current element
	 * 
	 * @return		the link the list currently refers to
	 */
	public E getCurrentElement()
	{
		return this.m_currentLink.getElement();
	}
	
	/**
	 * gets the value of the next element, but does not advance
	 * 
	 * @return			the value of the next element
	 */
	public E peek()
	{
		return this.m_currentLink.getNextLink().getElement();
	}
	
	/**
	 * moves the list's reference to the next element
	 */
	public void advance()
	{
		this.m_previousLink = this.m_currentLink;
		this.m_currentLink = this.m_currentLink.getNextLink();
	}
	
	/**
	 * moves the list's reference to the next element and returns it
	 * 
	 * @return		the next element in the list
	 */
	public E advanceAndGetNextElement()
	{
		advance();
		return getCurrentElement();
	}
	
	/**
	 * removes the current link from the list
	 */
	public void removeCurrentElement()
	{
		this.m_previousLink.setNextLink(this.m_currentLink.getNextLink());
	}
	
	/**
	 * @return		if the current element exists
	 */
	public boolean hasCurrent()
	{
		return this.m_currentLink != null;
	}
	
	/**
	 * @return		if there is an element following the current link
	 */
	public boolean hasNext()
	{
		if (this.m_currentLink == null)
		{
			return false;
		} else
		{
			return this.m_currentLink.getNextLink() != null;
		}
	}
	
	public void moveToStart()
	{
		this.m_previousLink = this.m_headerLink;
		this.m_currentLink = this.m_headerLink.getNextLink();
	}

	public void removeAll()
	{
		this.m_headerLink.setNextLink(null);
		this.m_currentLink = null;
		this.m_previousLink = null;
		this.m_lastLinkInList = null;
	}
	
	private class Link
	{

		private E m_element;
		private Link m_nextLink;
		
		public Link()
		{
			this.m_element = null;
			this.m_nextLink = null;
		}
		
		public Link(E initialElement)
		{
			this.m_element = initialElement;
			this.m_nextLink = null;
		}
		
		public Link(Link initialNextLink)
		{
			this.m_element = null;
			this.m_nextLink = initialNextLink;
		}
		
		public Link(E initialElement, Link initialNextLink)
		{
			this.m_element = initialElement;
			this.m_nextLink = initialNextLink;
		}
		
		public E getElement()
		{
			return this.m_element;
		}
		
		public void setElement(E newElement)
		{
			this.m_element = newElement;
		}
		
		public Link getNextLink()
		{
			return this.m_nextLink;
		}
		
		public void setNextLink(Link newNextLink)
		{
			this.m_nextLink = newNextLink;
		}
		
		public boolean hasNext()
		{
			return this.m_nextLink != null;
		}
	}

}
