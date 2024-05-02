package edu.iastate.cs228.hw1;
/**
 *  
 * @author ncros
 *
 */

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EmptyTest {
	
	/**
	 * Tests for if who method returns with the proper state
	 */
	@Test
	public void whoTest()
	{
		Plain p = new Plain(3);
		Empty e = new Empty(p, 1, 1);
		assertTrue(State.EMPTY == e.who());
	}
	
	/*
	 * If rabbits >= 2 then State = RABBIT
	 */
	@Test
	public void RabbitCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Empty e = new Empty(p, 0, 0);
		Rabbit r = new Rabbit(p, 0, 1, 0);
		Rabbit r1 = new Rabbit(p, 1, 0, 0);
		Rabbit r2 = new Rabbit(p, 1, 1, 0);
		p.grid[0][0] = e;
		p.grid[0][1] = r;
		p.grid[1][0] = r1;
		p.grid[1][1] = r2;
		
		Living subject = e.next(next);
		assertTrue(State.RABBIT == subject.who());
	}
	
	/*
	 * If Foxes >= 2 then State = FOX
	 */
	@Test
	public void FoxCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Empty e = new Empty(p, 0, 0);
		Fox f1 = new Fox(p, 0, 1, 0);
		Fox f2 = new Fox(p, 1, 0, 0);
		Rabbit r = new Rabbit(p, 1, 1, 0);
		p.grid[0][0] = e;
		p.grid[0][1] = f1;
		p.grid[1][0] = f2;
		p.grid[1][1] = r;
		
		Living subject = e.next(next);
		assertTrue(State.FOX == subject.who());
	}
	
	/*
	 * If badgers >= 2 then State = BADGER
	 */
	@Test
	public void BadgerCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Empty e = new Empty(p, 0, 0);
		Badger b1 = new Badger(p, 0, 1, 0);
		Badger b2 = new Badger(p, 1, 0, 0);
		Rabbit r = new Rabbit(p, 1, 1, 0);
		p.grid[0][0] = e;
		p.grid[0][1] = b1;
		p.grid[1][0] = b2;
		p.grid[1][1] = r;
		
		Living subject = e.next(next);
		assertTrue(State.BADGER == subject.who());
	}
	
	/*
	 * If Grass >= 1 then State = GRASS
	 */
	@Test
	public void GrassCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Empty e = new Empty(p, 0, 0);
		Grass g1 = new Grass(p, 0, 1);
		Grass g2 = new Grass(p, 1, 0);
		Rabbit r = new Rabbit(p, 1, 1, 0);
		p.grid[0][0] = e;
		p.grid[0][1] = g1;
		p.grid[1][0] = g2;
		p.grid[1][1] = r;
		
		Living subject = e.next(next);
		assertTrue(State.GRASS == subject.who());
	}
	
	/*
	 * otherwise, empty
	 */
	@Test
	public void EmptyCond()
	{
		Plain p = new Plain(2);
		Plain next = new Plain(2);
		Empty e1 = new Empty(p, 0, 0);
		Empty e2 = new Empty(p, 0, 1);
		Badger b = new Badger(p, 1, 0, 0);
		Rabbit r = new Rabbit(p, 1, 1, 0);
		p.grid[0][0] = e1;
		p.grid[0][1] = e2;
		p.grid[1][0] = b;
		p.grid[1][1] = r;
		
		Living subject = e1.next(next);
		assertTrue(State.EMPTY == subject.who());
	}
}