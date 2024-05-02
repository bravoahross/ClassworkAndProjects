package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;


/**
 *  
 * @author Noah Ross
 *
 */

/**
 * 
 * This class implements insertion sort.   
 *
 */

@SuppressWarnings("unused")
public class InsertionSorter extends AbstractSorter 
{
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) 
	{
		// Constructs sorter of type InsertionSort  
		super(pts);
		algorithm = "InsertionSort";
	}	

	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort()
	{
		// Go through array and sort using InsertionSort
		for (int i = 1; i < points.length; i++) {
			int previousIndex = i - 1;
			int currentIndex = i;
			
			// Move value back when the previous value is greater than the current
			while (previousIndex >= 0 && pointComparator.compare(points[currentIndex], points[previousIndex])== 1) {
				swap(currentIndex, previousIndex);
				previousIndex--;
				currentIndex--;
			}
		}
	}		
}