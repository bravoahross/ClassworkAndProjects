package edu.iastate.cs228.hw4;

/**
 * @author ncross4
 */

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main 
{

    public static void main(String[] args) 
    {
    	// Print user prompt
        System.out.print("Please enter filename to decode: ");
        
        // Create new scanner s to get filename
        Scanner s = new Scanner(System.in);
        String filename = s.next();
        
        // Try/catch to catch FileNotFound
        try 
        {
            // Open File and scan the first 2 lines
            File file = new File(filename);
            Scanner fileScan = new Scanner(file);
            String treeStructure = fileScan.nextLine();
            String binaryCode = fileScan.nextLine();
            
            // If the file has three lines, add the second to the first and make the third line the binaryCode
            if (fileScan.hasNext()) 
            {
                treeStructure = treeStructure + '\n' + binaryCode;
                //Put the 3rd line into binaryCode
                binaryCode = fileScan.nextLine();
            }
            
            //Make a new key based on the given character map
            MessageDecode Key = new MessageDecode(treeStructure);
            
            //Print a header to the message key
            System.out.println("Character:  Binary Code:");
            System.out.println("------------------------");
            
            //Print the Key to the binary message 
            MessageDecode.printKey(Key, "");
            
            //Print a header to the message
            System.out.println("");
            System.out.println("Decoded Message:");
            System.out.println("----------------");
            
            //Using the key, decode the Binary encoded message
            Key.decode(binaryCode);
            
            //Close File Scanner
            fileScan.close();
        } 
        
        //Catch FileNotFound
        catch (FileNotFoundException e)
        {
            System.out.println("File Not Found Exception: " + filename);
        }
        
        //Close Filename Scanner
        s.close();
    }
}