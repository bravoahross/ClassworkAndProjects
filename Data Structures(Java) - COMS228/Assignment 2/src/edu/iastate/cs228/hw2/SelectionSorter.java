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
 * This class implements selection sort.   
 *
 */

@SuppressWarnings("unused")
public class SelectionSorter extends AbstractSorter
{
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts  
	 */
	public SelectionSorter(Point[] pts)  
	{
		// Constructs sorter of type SelectionSort
		super(pts);
		algorithm = "SelectionSort";
	}	

	
	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.  
	 * 
	 */
	@Override 
	public void sort()
	{
		for (int i = 0; i < points.length - 1; i++) {
			// Set minPos to the first index
			int minPos = i;
			
			// Find min value (and therefor minPos) in unsorted array
			for (int j = i + 1; j < points.length; j++) {
				// If the point at j > than the point at minPos, swap 
				if (pointComparator.compare(points[j], points[minPos]) == 1) {
					minPos = j;
				}
			}
			// Swap value at minPos to the next value in sorted array 
			swap(minPos, i);
		}
	}	
}