package edu.iastate.cs228.hw2;

/**
 *  
 * @author Noah Ross
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		// TODO 
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       PointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
		PointScanner[] scanners = new PointScanner[4]; 
		// Intro printed before trials
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning\n");
		System.out.println("Keys: 1 (random integers) 2 (file input) 3 (exit)");
		        // Scanner for user input
				Scanner scan = new Scanner(System.in);
				
				// For random number generation
				Random rand = new Random();
				
				// Initialize and print Trial number
				int trialnum = 1;
				System.out.print("Trial " + trialnum + ": ");
				
				// While there is user input
				while(scan.hasNextInt()) {
					// Store user input
					int input = scan.nextInt();
					
					// Uses Random point selection if the input is 1
					if (input == 1) {
						// Ask and store number of rand points
						System.out.print("Enter number of random points: ");
						int numRand = scan.nextInt();
						// Generate random points
						Point points[] = generateRandomPoints(numRand, rand);
						// Construct instance of all sorting algorithms to sort x and y
						scanners[0] = new PointScanner(points, Algorithm.SelectionSort);
						scanners[1] = new PointScanner(points, Algorithm.InsertionSort);
						scanners[2] = new PointScanner(points, Algorithm.MergeSort);
						scanners[3] = new PointScanner(points, Algorithm.QuickSort);
					}
					
					// Uses Text file if the input is 2
					else if (input == 2) {
						System.out.println("Points from a file");
						//Ask and store file name
						System.out.print("File name: ");
						String filename = scan.next();
						// Construct instance of all sorting algorithms to sort x and y
						scanners[0] = new PointScanner(filename, Algorithm.SelectionSort);
						scanners[1] = new PointScanner(filename, Algorithm.InsertionSort);
						scanners[2] = new PointScanner(filename, Algorithm.MergeSort);
						scanners[3] = new PointScanner(filename, Algorithm.QuickSort);
					}
					
					// Exits the program if the input is 3
					else if (input == 3) {
						System.out.println("Program Exited");
						break;
					}
					
					// Exits the program if the input is anything other than 1-3 
					else {
						System.out.println("Invalid input. Program Exited");
						break;
					}
					
					// Sort all arrays using different sorters
					for (PointScanner s : scanners) {
						s.scan();
					}
					
					// Format output
					System.out.println("\nalgorithm         size  time (ns)");
					System.out.println("----------------------------------");
					scanners[0].writeMCPToFile();
					
					// Print out formatted stats for each sorter
					for (PointScanner s : scanners) {
						System.out.println(s.stats());
					}
					System.out.println("----------------------------------\n");
					
					// Print next trial
					trialnum ++;
					System.out.print("Trial: " + trialnum + ": ");
					
				}
					// Close scanner
					scan.close();
		}

		// For each input of points, do the following. 
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() 
		//        method in the PointScanner class.  
		//
		//     c) After all four scans are done for the input, print out the statistics table from
		//		  section 2.
		//
		// A sample scenario is given in Section 2 of the project description. 
		
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] � [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		Point pts[] = new Point[numPts];
		//Check if numPts is zero or negative
		if (numPts < 1) {
			throw new IllegalArgumentException("Array can't have zero or negative number of points");
		}
		//Generate random points from -50 to 50 for x and y
		for (int i = 0; i < numPts; i++) {
			pts[i] = new Point(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
		}
		return pts;
	}
}
