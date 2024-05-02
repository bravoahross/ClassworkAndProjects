package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AnimalTest {

	/*
	 * A badger that's age is 0
	 */
	@Test
	public void testGetAge1()
	{
		Plain p = new Plain(2);
		Badger b = new Badger(p, 0, 0, 0);
		assertEquals(0, b.myAge());
	}
	/*
	 * A Rabbit that's age is 3
	 */
	@Test
	public void testGetAge2()
	{
		Plain p = new Plain(2);
		Rabbit r = new Rabbit(p, 0, 0, 3);
		assertEquals(3, r.myAge());
	}
	
}
