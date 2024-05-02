package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GrassTest {
	
	/**
	 * Tests for if who method returns with the proper state
	 */
	@Test
	public void whoTest()
	{
		Plain p = new Plain(3);
		Grass g = new Grass(p, 1, 1);
		assertTrue(State.GRASS == g.who());
	}
	
	/**
	 * if rabbits >= grass * 3 then State = EMPTY
	 */
	@Test
	public void EatenCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Grass g = new Grass(p, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Rabbit r1 = new Rabbit(p, 1, 0, 0);
		Rabbit r2 = new Rabbit(p, 1, 1, 0);
		p.grid[0][0] = g;
		p.grid[0][1] = r;
		p.grid[1][0] = r1;
		p.grid[1][1] = r2;
		
		Living subject = g.next(next);
		assertTrue(State.EMPTY == subject.who());
	}
	
	/**
	 * if rabbits >= 3 then State = RABBIT
	 */
	@Test
	public void RabbitCond()
	{
		Plain p = new Plain(3);
		Plain next = new Plain(3);
		Grass g1 = new Grass(p, 0, 0);
		Grass g2 = new Grass(p, 0, 1);
		Grass g3 = new Grass(p, 0, 2);
		Grass g4 = new Grass(p, 1, 0);
		Grass g5 = new Grass(p, 1, 1);
		Grass g6 = new Grass(p, 1, 2);
		Rabbit r = new Rabbit(p, 2, 0, 0);
		Rabbit r1 = new Rabbit(p, 2, 1, 0);
		Rabbit r2 = new Rabbit(p, 2, 2, 0);
		p.grid[0][0] = g1;
		p.grid[0][1] = g2;
		p.grid[0][2] = g3;
		p.grid[1][0] = g4;
		p.grid[1][1] = g5;
		p.grid[1][2] = g6;
		p.grid[2][0] = r;
		p.grid[2][1] = r1;
		p.grid[2][2] = r2;
		
		Living subject = g5.next(next);
		assertTrue(State.RABBIT == subject.who());
	}
	
	/**
	 * otherwise grass
	 */
	@Test
	public void GrassCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Empty e1 = new Empty(p, 0, 0);
		Empty e2 = new Empty(p, 0, 1);
		Badger b = new Badger(p, 1, 0, 0);
		Grass g = new Grass(p, 1, 1);
		p.grid[0][0] = e1;
		p.grid[0][1] = e2;
		p.grid[1][0] = b;
		p.grid[1][1] = g;
		
		Living subject = g.next(next);
		assertTrue(State.GRASS == subject.who());
	}
}