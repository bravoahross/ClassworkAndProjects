package edu.iastate.cs228.hw4;

/**
 * 
 * @author ncross4
 *
 */

public  class  MessageDecode 
{
    public MessageDecode leftNode;
    public MessageDecode rightNode;
    public char charKey;
    
    /**
     * Constructor for a single node with no children
     * @param charKey
     */
    public MessageDecode(char charKey) 
    {
        this.charKey = charKey;
    }

    /**
     * Constructor for a String of nodes and instructions
     * @param treeStructure
     */
    public MessageDecode(String treeStructure) 
    {
        // Start branching at ^ because the tree structure always starts with that
        charKey = '^';
        
        // Current node being analyzed 
        MessageDecode currentNode;
        
        // Cycle through every character in the string
        for (char key : treeStructure.substring(1, treeStructure.length()).toCharArray()) 
        {
            currentNode = this;
            
            // Starting from the current node at the beginning, determine if the left branch is full, all the way until it is null
            while (currentNode.leftNode != null && (!isFull(currentNode.leftNode)) || currentNode.rightNode != null) 
            {
                // If the left tree branching is full, move right and check new branch
                if (isFull(currentNode.leftNode)) 
                {
                    currentNode = currentNode.rightNode;
                }
                
                // If the left tree is not full, move left and check new branch
                else 
                {
                    currentNode = currentNode.leftNode;
                }
            }
            
            // Add current node if it doesn't have a left child
            if (currentNode.leftNode == null) 
            {
                currentNode.leftNode = new MessageDecode(key);
            }
            
            // If the left tree is full, add to right
            else if (isFull(currentNode.leftNode)) 
            {
                currentNode.rightNode = new MessageDecode(key);
            }
            
            // Else add to left 
            else 
            {
                currentNode.leftNode = new MessageDecode(key);
            }
        }
    }

    /**
     * Method to recursively print the Key to the console
     * @param Key
     * @param code
     */
    public static void printKey(MessageDecode Key, String code) 
    {

        // Base case for printing key
        if (Key.leftNode == null && Key.rightNode == null && (int) Key.charKey != 0 && Key.charKey != '^') 
        {
            // Prints char and code to the key
            System.out.println(" " + (Key.charKey != '\n' ? Key.charKey : "\\n") + "          " + code);
        }
        // If there is a left branch, take it first to print key
        if (Key.leftNode != null) 
        {
            // Add a 0 to code when calling again in order to keep track of traversal
            printKey(Key.leftNode, code + '0');
        }
        // If there is a right branch, take it next to print key
        if (Key.rightNode != null) 
        {
            // Add a 1 to code when calling again in order to keep track of traversal
            printKey(Key.rightNode, code + '1');
        }
    }

    /**
     * Helper Method:
     * Check if a tree is full
     * @param tree
     * @return boolean
     */
    public static boolean isFull(MessageDecode tree) 
    {
    	// If the left and right nodes are empty and the character isn't 0 or ^, branch is full
        if (tree.leftNode == null && tree.rightNode == null && (int) tree.charKey != 0 && tree.charKey != '^')
        {
        	return true;
        }
        
        // If the left or right node are empty, branch is not full
        if ((tree.leftNode == null) || (tree.rightNode == null))
        {
        	return false;
        }
        
        // Takes the left and right nodes and travels down them recursively to find if all branches are full
        return (isFull(tree.leftNode) && isFull(tree.rightNode));
    }
    
    /**
     * Helper Method:
     * Allows the user to just pass in a message to decode
     * @param binaryCode
     */
    public void decode(String binaryCode) 
    {
        decode(this, binaryCode);
    }

    /**
     * Decodes and prints the message given the binary code and the tree
     * @param tree
     * @param binaryCode
     */
    public void decode(MessageDecode tree, String binaryCode) 
    {
        // Keep track of tree traversal
        MessageDecode current = tree;
        
        // Loop for ever bit in the binary message
        for (char bit : binaryCode.toCharArray()) 
        {
            // If it's a 0 move left 
            if (bit == '0') 
            {
                current = current.leftNode;
                
            // Else move right
            }
            else 
            {
                current = current.rightNode;
            }
            
            // If the new current node is a leaf, print char and reset current to beginning of key
            if (current.leftNode == null && current.rightNode == null && (int) current.charKey != 0 && current.charKey != '^') 
            {
                System.out.print(current.charKey);
                current = tree;
            }
        }
    }
}