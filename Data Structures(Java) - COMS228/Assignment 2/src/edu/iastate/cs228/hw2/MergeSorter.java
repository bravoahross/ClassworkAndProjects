package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

@SuppressWarnings("unused")
public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if needed
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		// Constructs sorter of type MergeSort
		super(pts);
		algorithm = "MergeSort";
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		// Call merge sort
		mergeSortRec(points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		if (pts.length < 2) {
			return;
		}
		int mid = pts.length/2;
		// Creates left and right arrays 
		Point[] leftPart = new Point[mid];
		Point[] rightPart = new Point[pts.length - mid];
		
		// Copy values to left part
		for (int i = 0; i < leftPart.length; i++) {
			leftPart[i] = pts[i];
		}
		
		// Copy values to right part
		for (int i = mid; i < pts.length; i++) {
			rightPart[i - mid] = pts[i];
		}
		
		// Recursively sort each half of the given array
		mergeSortRec(leftPart);
		mergeSortRec(rightPart);
		
		// Merge the split arrays in order of smallest number in each partition
		merge(pts, leftPart, rightPart, mid, pts.length - mid);
	}

	/**
	 * This is a merge helper method to merge the two array halves in order of smallest to largest
	 * 
	 * @param sorted		sorted array which is the result
	 * @param leftPart		left part of the unsorted array
	 * @param rightPart		right part of the unsorted array
	 * @param leftLength	length of left array
	 * @param rightLength	length of right array
	 */
	private void merge(Point[] sorted, Point[] leftPart, Point[] rightPart, int leftLength, int rightLength) {
		int leftPos = 0;
		int rightPos = 0;
		int currentPos = 0;
		
		// Grabs the lower value of the two arrays and puts it into the sorted array
		while (leftPos < leftLength && rightPos < rightLength) {
			if (pointComparator.compare(leftPart[leftPos], rightPart[rightPos]) == 1) {
				sorted[currentPos] = leftPart[leftPos];
				leftPos++;
			}
			else {
				sorted[currentPos] = rightPart[rightPos];
				rightPos++;
			}
			currentPos++;
			
			// If there are leftover values in either the right or left array add them now
			while (leftPos < leftLength) {
				sorted[currentPos++] = leftPart[leftPos++];
			}
			while (rightPos < rightLength) {
				sorted[currentPos++] = rightPart[rightPos++];
			}
		}
	}
}