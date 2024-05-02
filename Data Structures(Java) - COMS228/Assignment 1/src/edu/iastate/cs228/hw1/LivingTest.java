package edu.iastate.cs228.hw1;
/**
 *  
 * @author ncros
 *
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LivingTest {

	@Test
	public void testGetAge1()
	{
		Plain p = new Plain(2);
		int population[] = new int[5];
		int pop2[] = new int[]{1,1,0,1,1};
		Badger b = new Badger(p, 0, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Grass g = new Grass(p, 1, 0);
		Empty e = new Empty(p, 1, 1);
		p.grid[0][0] = b;
		p.grid[0][1] = r;
		p.grid[1][0] = g;
		p.grid[1][1] = e;
		b.census(population);
		assertEquals( pop2[0], population[0]);
		assertEquals( pop2[1], population[1]);
		assertEquals( pop2[2], population[2]);
		assertEquals( pop2[3], population[3]);
		assertEquals( pop2[4], population[4]);
	}
}
