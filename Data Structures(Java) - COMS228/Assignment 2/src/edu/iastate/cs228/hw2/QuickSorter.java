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
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 */
@SuppressWarnings("unused")
public class QuickSorter extends AbstractSorter
{
	
	// Other private instance variables if you need ... 
		
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts)
	{
		// Constructs sorter of type QuickSort
		super(pts);
		algorithm = "QuickSort";
	}
		

	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
	 * 
	 */
	@Override 
	public void sort()
	{
		quickSortRec(0, points.length - 1);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last)
	{
		// If the first is greater than the last value, recursively quick sort
		if (first < last) {
			int partPos = partition(first, last);
			quickSortRec(first, partPos - 1);
			quickSortRec(partPos + 1, last);
		}
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last)
	{
		// Store pivot point and initial position
		Point pivot = points[last];
		int pos = first - 1;
		
		for (int i = first; i <= last - 1; i++) {
			// Check if current point is less than pivot
			if (pointComparator.compare(points[i], pivot) == 1) {
			// Increment index and swap points
			pos ++;
			swap(pos, i);
			}
		}
	// Swap index + 1 and last
	swap(pos + 1, last); 
	return pos + 1;
	}	
}