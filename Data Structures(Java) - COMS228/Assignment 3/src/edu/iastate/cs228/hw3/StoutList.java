package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
  /**
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
  /**
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
   */
  public Node head;
  
  /**
   * Dummy node for tail.
   */
  private Node tail;
  
  /**
   * Number of elements in the list.
   */
  private int size;
  
  /**
   * Constructs an empty list with the default node size.
   */
  public StoutList()
  {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize)
  {
    if ((nodeSize <= 0) || (nodeSize % 2 != 0)) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
   * @param head
   * @param tail
   * @param nodeSize
   * @param size
   */
  public StoutList(Node head, Node tail, int nodeSize, int size)
  {
	  this.head = head; 
	  this.tail = tail; 
	  this.nodeSize = nodeSize; 
	  this.size = size; 
  }
  /*
   * Returns the size of the StoutList
   */
  @Override
  public int size()
  {
    if (size > Integer.MAX_VALUE) 
    {
    	return Integer.MAX_VALUE;
    }
    else 
    {
    	return size;
    }
  }
  
  /*
   * Adds an item to the end of the StoutList 
   * Throws a NullPointerException if item is "Null"
   */
  @Override
  public boolean add(E item) throws NullPointerException
  {
    if (item == null) 
    {
    	throw new NullPointerException();
    } 
    else 
    {
    	add(size, item);
    	return true;
    }
  }
  
  /*
   * Adds an item to a specified position in the StoutList 
   * Throws a NullPointerException if item is "Null"
   * Throws an IndexOutOfBoundsException if pos is out of bounds
   */
  @Override
  public void add(int pos, E item) throws NullPointerException, IndexOutOfBoundsException
  {
    if (item == null) 
    {
    	throw new NullPointerException();
    }
    if ((pos < 0) || (pos > size)) 
    {
    	throw new IndexOutOfBoundsException();
	} 
    else 
    {
    	NodeData nodeInformation = find(pos);
    	add(nodeInformation.node, nodeInformation.offset, item);
    }
  }
  
  /*
   * Removes an item to a specified position in the StoutList 
   * Throws an IndexOutOfBoundsException if pos is out of bounds
   */
  @Override
  public E remove(int pos) throws IndexOutOfBoundsException
  {
    if ((pos < 0) || (pos > size)) 
    {
    	throw new IndexOutOfBoundsException();
	} 
    else 
    {
    	NodeData nodeInformation = find(pos);
    	return remove(nodeInformation);
    }
  }

  /**
   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
   * Traverse the list and copy its elements into an array, deleting every visited node along 
   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
   * (possibly) the last one must be full of elements.  
   *  
   * Comparator<E> must have been implemented for calling insertionSort().    
   */
@SuppressWarnings({ "unchecked", "rawtypes" })
public void sort()
  {
	  Iterator<E> iter = iterator();
	  E[] StoutArray = (E[]) new Comparable[size()];
	  int index;
	  // For-loop to copy the elements to the StoutArray from the list
	  for (index = 0; index < size; index++) 
	  {
		  StoutArray[index] = iter.next();
	  }
	  head.next = tail;
	  tail.previous = head;
	  size = 0;
	  
	  // Using comparator c, sort StoutArray 
	  Comparator<E> c = new ObjComparator();
	  insertionSort (StoutArray, c);
	  
	  // Copy sorted data back into list
	  for (index = 0; index < StoutArray.length; index++)
	  {
		  this.add(StoutArray[index]);
	  }
  }
  
  /**
   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  @SuppressWarnings("unchecked")	
  public void sortReverse() 
  {
	  Iterator<E> iter = iterator();
	  E[] StoutArray = (E[]) new Comparable[size()];
	  int index;
	  // For-loop to copy the elements to the StoutArray from the list
	  for (index = 0; index < size; index++) 
	  {
		  StoutArray[index] = iter.next();
	  }
	  head.next = tail;
	  tail.previous = head;
	  size = 0;
	  bubbleSort(StoutArray);
	  
	  // Copy sorted data back into list
	  for (index = 0; index < StoutArray.length; index++)
	  {
		  this.add(StoutArray[index]);
	  }
  }
  
  /*
   * Iterator 
   */
  @Override
  public Iterator<E> iterator()
  {
    return listIterator();
  }

  /*
   * Iterator for list starting at index 0 (undeclared)
   */
  @Override
  public ListIterator<E> listIterator()
  {
    return listIterator(0);
  }

  /*
   * Iterator for list starting at specified index
   * Throws index out of bounds if index is bigger than size or less than 0
   */
  @Override
  public ListIterator<E> listIterator(int index) throws IndexOutOfBoundsException
  {
	if ((index < 0) || (index > size)) 
	{
		throw new IndexOutOfBoundsException();
	}
	else 
	{
		StoutListIterator iter = new StoutListIterator(index);
		return iter;
	}
  }
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal()
  {
    return toStringInternal(null);
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }


  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node
  {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable. 
	// I suppressed unavoidable warning
    @SuppressWarnings("unchecked")
	public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      
      //useful for debugging
      //      System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
  
    /**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item)
    {
      if (count >= nodeSize)
      {
    	  return;
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      count--;
    }    
  }
  
  /**
   * 
   * @author ncross4
   * A class to add to the minimal Node class we were given
   *
   */
  private class NodeData 
	{
		/*
		 * The Node Object which contains the logic index.
		 */		
		public Node node;
		
		/*
		 * The offset of the logic index inside the node which
		 * contains the logic index
		 */		
		public int offset;

		/**
		 * NodeData Constructor
		 * @param node: A node that holds the logic index.
		 * @param offset: the offset of the logic index inside the node which contains the logic index
		 */
		public NodeData(Node node, int offset) 
		{
			this.node = node;
			this.offset = offset;
		}
	}

  /**
   * Returns a node containing the index and the offset
   * @param pos: index we are looking for.
   * @return NodeData object which contains a node with the index and the offset
   */
  NodeData find(int pos) 
  {
	if (pos == -1) 
	{
		return new NodeData(head, 0);
	}
	if (pos == size) 
	{ 
		return new NodeData(tail, 0);
	}
	Node current = head.next;
	int index = current.count - 1;
	while ((current != tail) && (pos > index)) 
	{
		current = current.next;
		index += current.count;
	}
	int offset = current.count + pos - index - 1;
	return new NodeData(current, offset);
  }

  /**
   * Add helper method
   * Throws Null Pointer Exception if the item is null
   * @param n: node to add to
   * @param offset: the position in the node to insert into
   * @param item: element to add
   * @return the NodeData containing position of item
   */
  private NodeData add(Node n, int offset, E item) throws NullPointerException
  {
	  if (item == null)
	  {
		  throw new NullPointerException();
	  }
	  NodeData NodeAns = null;
	  if (size == 0)
	  {
		  Node Node = new Node();
		  Node.addItem(item);
		  merge(head, Node);
		  NodeAns = new NodeData(Node, 0);
	  }
	  else if ((offset == 0) && (n.previous.count < nodeSize) && (n.previous != head))
	  {
		  n.previous.addItem(item);
		  NodeAns = new NodeData(n.previous, n.previous.count - 1);
	  }
	  else if ((offset == 0) && (n == tail) && (n.previous.count == nodeSize))
	  {
		  Node Node = new Node();
		  Node.addItem(item);
		  merge(tail.previous, Node);
		  NodeAns = new NodeData(Node, 0);
		}
	  else if (n.count < nodeSize)
	  {
		  n.addItem(offset, item);
		  NodeAns = new NodeData(n, offset);	
	  }
	  else
	  {
		  Node Node = new Node();
		  merge(n, Node);
		  for (int i = nodeSize - 1; i >= nodeSize - nodeSize / 2; i--) 
		  {
			  Node.addItem(0, n.data[i]);
			  n.removeItem(i);
		  }
		  if (offset <= nodeSize / 2) 
		  {
			  n.addItem(offset, item);
			  NodeAns = new NodeData(n, offset);
		  }
		  else
		  {
			  Node.addItem(offset - nodeSize / 2, item);
			  NodeAns = new NodeData(Node, offset - nodeSize / 2);
		  }
	  }
	  // Increment size after adding Node
	  size++;
	  return NodeAns;
  }
  
  /**
   * Remove helper method
   * @param nodeInfo: 
   * @return the removed element (target)
   */
  private E remove(NodeData nodeInformation) 
  {
	  E target = nodeInformation.node.data[nodeInformation.offset];
	  if ((nodeInformation.node.next) == tail && (nodeInformation.node.count == 1))
	  {
		  unmerge(nodeInformation.node); 
	  }
	  else if ((nodeInformation.node.next == tail) || (nodeInformation.node.count > nodeSize / 2))
	  {
		  nodeInformation.node.removeItem(nodeInformation.offset);
	  }
	  else if (nodeInformation.node.count <= nodeSize / 2)
	  {
		  nodeInformation.node.removeItem(nodeInformation.offset);
		  if (nodeInformation.node.next.count > nodeSize / 2) 
		  {
			  nodeInformation.node.addItem(nodeInformation.node.next.data[0]);
			  nodeInformation.node.next.removeItem(0);
		  } 
		  else 
		  {
			  for (E e : nodeInformation.node.next.data) 
				  if (e != null)
					  nodeInformation.node.addItem(e);
			  unmerge(nodeInformation.node.next);
		  }
	  }
	  size--;
	  return (target);
  }
  
  /**
   * Helper Method for merging nodes
   * @param node1: original node
   * @param node2: node to be merged to node 1
   */
  private void merge(Node node1, Node node2) 
  {
	  node2.previous = node1;
	  node2.next = node1.next;
	  node1.next.previous = node2;
	  node1.next = node2;
  }
  
  /**
   * Helper Method for unmerging nodes
   * @param current: Node to be unlinked
   */
  private void unmerge(Node current)
  {
	  current.previous.next = current.next;
	  current.next.previous = current.previous;
  }

  /**
   * Comparator for insertion sort
   * @author ncross4
   *
   * @param <E>
   */
  @SuppressWarnings({ "hiding", "rawtypes" })
private class ObjComparator<E extends Comparable<? super E>> implements Comparator 
  {
	  @SuppressWarnings("unchecked")
	@Override
	  public int compare(Object a, Object b) 
	  {
		  E value1 = (E) a;
		  E value2 = (E) b;
		  return value1.compareTo(value2);
	  }
  }
  
  private class StoutListIterator implements ListIterator<E>
  {
	// constants you possibly use ...   
	  
	// instance variables ... 
	private boolean Removeable = false;
	private int pointer;
	private NodeData end;
	  
    /**
     * Default constructor 
     */
    @SuppressWarnings("unused")
	public StoutListIterator()
    {
    	Removeable = false;
    	pointer = 0;
    	end = null;
    }

    /**
     * Constructor finds node at a given position.
     * Throws Index out of bounds if pos is too small (negative) or too big (pos > size)
     * @param pos
     */
    public StoutListIterator(int pos) throws IndexOutOfBoundsException
    {
    	if ((pos < 0) || (pos > size))
    	{
    		throw new IndexOutOfBoundsException();
    	}
    	Removeable = false;
    	pointer = pos;
    	end = null;
    }

    /**
     * Checks if the iterator has a next element
     */
    @Override
    public boolean hasNext()
    {
    	boolean hasNext = false;
    	if (pointer < size) 
    	{
    		hasNext = true;
    	}
    	return hasNext;
    }

    /**
     * Cycles the list to the next element
     * Throws No Such Element if there is no next element
     */
    @Override
    public E next() throws NoSuchElementException
    {
    	if (hasNext())
    	{
    		NodeData n = find(pointer++);
    		Removeable = true;
    		end = n;
    		return (n.node.data[n.offset]);
    	}
    	// If no next element, throw No Such Element
    	throw new NoSuchElementException();
    }

    /**
     * removes Node if possible
     * Throws Illegal State Exception if Removable is false
     */
    @Override
    public void remove() throws IllegalStateException
    {
    	if (!Removeable)
    	{
    		throw new IllegalStateException();
    	}
    	NodeData cursor = find(pointer);
    	if ((end.node == cursor.node) && (end.offset < cursor.offset) || (end.node != cursor.node))
    	{
    		pointer--;
    	}
    	StoutList.this.remove(end);
    	// Reset end, pointer, and removable status after remove
    	Removeable = false;
    	end = null;
    }

    /**
     * Checks if iterator has previous element
     */
	@Override
	public boolean hasPrevious() 
	{
		boolean hasPrevious = false;
		if (pointer > 0) {
			hasPrevious = true;
		}
		return hasPrevious;
	}
	
	/**
	 * Cycles to previous element
	 */
	@Override
	public E previous() throws NoSuchElementException
	{
		if (hasPrevious())
		{
			NodeData n = find(--pointer);
			end = n;
			Removeable = true;
			return n.node.data[n.offset];
		}
		throw new NoSuchElementException();
	}

	/**
	 * Returns the next index
	 */
	@Override
	public int nextIndex() 
	{
		return pointer;
	}
	
	/**
	 * Returns the previous index
	 */
	@Override
	public int previousIndex() 
	{
		return (pointer - 1);
	}

	/**
	 * Sets offset to e
	 */
	@Override
	public void set(E e) throws NullPointerException, IllegalStateException
	{
		if (e == null)
		{
			throw new NullPointerException();
		}
		if (!Removeable)
		{
			throw new IllegalStateException();
		}
		end.node.data[end.offset] = e;
	}
	
	/**
	 * Adds e to where the pointer is
	 */
	@Override
	public void add(E e) throws NullPointerException
	{
		if (e == null) 
		{
			throw new NullPointerException();
		}
		Removeable = false;
		StoutList.this.add(pointer++, e);
	}
  }
  
  /**
   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
   * @param arr   array storing elements from the list 
   * @param comp  comparator used in sorting 
   */
  private void insertionSort(E[] arr, Comparator<? super E> comp)
  {
	  int i, j;
	  E temp;
	  for (i = 1; i < arr.length; i++)
	  {
		  j = i;
		  while((j > 0) && (comp.compare(arr[j-1], arr[j]) > 0))
		  {
			  // Swap values
			  temp = arr[j];
			  arr[j] = arr[j-1];
			  arr [j-1] = temp;
			  j--;
		  }
	  }
  }
  
  /**
   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
   * description of bubble sort please refer to Section 6.1 in the project description. 
   * You must use the compareTo() method from an implementation of the Comparable 
   * interface by the class E or ? super E. 
   * @param arr  array holding elements from the list
   */
  private void bubbleSort(E[] arr)
  {
	  boolean sorted = true;
	  for (int i = 1; i < arr.length; i++)
	  {
		  if (arr[i - 1].compareTo(arr[i]) < 0)
		  {
			  // Swap values
			  E temp = arr[i - 1];
			  arr[i - 1] = arr[i];
			  arr[i] = temp;
			  sorted = false;
		  }
	  }
	  // If the array is unsorted, recursively calls bubble sort. Ends when sorted
	  if (sorted)
	  {
		  return;
	  }
	  else
	  {
		  bubbleSort(arr);
	  }
  }
}