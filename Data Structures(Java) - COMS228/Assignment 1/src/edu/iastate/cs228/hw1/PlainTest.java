package edu.iastate.cs228.hw1;
/**
 *  
 * @author ncros
 *
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PlainTest {

	
	@Test
	public void testRandomInit()
	{
		Plain p = new Plain(2);
		p.randomInit();
		for(int r = 0; r < 2; r++)
		{
			for(int c =0; c < 2; c++)
			{
				assertTrue(p.grid[r][c] instanceof Living);
			}
		}
	}
	
	@Test
	public void testToString()
	{
		Plain p = new Plain(2);
		Badger b = new Badger(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 0, 0);
		Empty e = new Empty(p, 0, 0);
		Grass g = new Grass(p, 0, 0);
		p.grid[0][0] = b;
		p.grid[0][1] = r;
		p.grid[1][0] = g;
		p.grid[1][1] = e;
		assertEquals("B0 R0 \nG  E  \n", p.toString());
	}
	@Test
	public void testGetWidth()
	{
		Plain p = new Plain(2);
		assertEquals(2, p.getWidth());
	}
}
