package edu.iastate.cs228.hw2;

/**
 * @author Noah Ross
 */

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter; 
import java.io.IOException; 
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
		
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		// Set the algorithm type
		sortingAlgorithm = algo;
		if (pts == null || pts.length == 0) {
			throw new IllegalArgumentException("Null array");
		}
		// Deep copy array
		points = new Point[pts.length];
		for (int i = 0; i < pts.length; i++) {
			points[i] = pts[i];
		}
	}

	
	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		// Try to input a file and add the points to an arraylist which is coppied to an array
		try {
			ArrayList<Point> pointsList = new ArrayList<>();
			sortingAlgorithm = algo;
			// Create a file and scanner to read with
			File file = new File(inputFileName);
			Scanner scan = new Scanner(file);
			
			// Add points to array list from scanned file 
			while (scan.hasNextInt()) {
				pointsList.add(new Point (scan.nextInt(), scan.nextInt()));
			}
			scan.close();
			points = new Point[pointsList.size()];
			// Copy array list to array points
			points = pointsList.toArray(points);
		}
		
		// Catches for the two exceptions mentioned in the javadoc
		catch (FileNotFoundException e) {
			throw new FileNotFoundException("Input file not found: " + inputFileName);
		}
		catch (NoSuchElementException e) { // For odd numbered files
			throw new InputMismatchException("Invalid input file: file contains odd number of elements");
		}
	}

	
	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan()
	{
		// TODO  
		AbstractSorter aSorter; 
		
		// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the two 
		// rounds of sorting, have aSorter do the following: 
		// 
		//     a) call setComparator() with an argument 0 or 1. 
		//
		//     b) call sort(). 		
		// 
		//     c) use a new Point object to store the coordinates of the medianCoordinatePoint
		//
		//     d) set the medianCoordinatePoint reference to the object with the correct coordinates.
		//
		//     e) sum up the times spent on the two sorting rounds and set the instance variable scanTime. 
	    // A switch to populate a sorter based on the type of sorting method
		switch(sortingAlgorithm) {
		case InsertionSort:
			aSorter = new InsertionSorter(points);
			break;
	    case MergeSort:
	    	aSorter = new MergeSorter(points);
	    	break;
		case QuickSort:
			aSorter = new QuickSorter(points);
			break;
	    case SelectionSort:
	    	aSorter = new SelectionSorter(points);
	    	break;
	    /* This default should never be reached. I included it so that the compiler understands that
	    aSorter will always be initialized. */
	    default: 									
	    	aSorter = new InsertionSorter(points);	
	}
		// Sets comparator to X sort and tracks time and median
		aSorter.setComparator(0);
		long timeX = System.nanoTime();
		aSorter.sort();
		timeX -= System.nanoTime();
		int medianX = aSorter.getMedian().getX();
		
		// Sets comparator to Y sort and tracks time and median
		aSorter.setComparator(1);
		long timeY = System.nanoTime();
		aSorter.sort();
		timeY -= System.nanoTime();
		int medianY = aSorter.getMedian().getY();
		
		// Sets total run time and median value for sorting method
		scanTime = (timeX + timeY) * -1;
		medianCoordinatePoint = new Point(medianX, medianY);
	}
	
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{ 
		// Prints off runtime and size stats 
		if (sortingAlgorithm == Algorithm.SelectionSort || sortingAlgorithm == Algorithm.InsertionSort) {
			return sortingAlgorithm.toString() + "     " + points.length + "   " + scanTime;
		}
		else{
			return sortingAlgorithm.toString() + "         " + points.length + "   " + scanTime;
		}
	}
	
	
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		
		// Write the median coordinate when scan is called
		return ("MCP: " + medianCoordinatePoint.toString());
	}

	
	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{
		// Writes to the output file 
		String outputFileName = "point.txt";
		try {
			File file = new File(outputFileName);
			FileWriter writer = new FileWriter(file);
			writer.write(toString());
			writer.close();
		}
		// In this version of java IOException is what the File system throws
		catch(IOException e) {
			throw new FileNotFoundException("Invalid output file");
		}
	}			
}