package edu.iastate.cs228.hw1;

/**
 *  
 * @author ncros
 *
 */

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WildlifeTest {

	@Test
	public void testUpdatePlain1()
	{
		Plain p = new Plain(2);
		Empty e1 = new Empty(p, 0, 0);
		Empty e2 = new Empty(p, 0, 1);
		Empty e3 = new Empty(p, 1, 0);
		Empty e4 = new Empty(p, 1, 1);
		p.grid[0][0] = e1;
		p.grid[0][1] = e2;
		p.grid[1][0] = e3;
		p.grid[1][1] = e4;
		
		Plain p2 = new Plain(p.getWidth());
		Wildlife.updatePlain(p, p2);
		for (Living[] row : p2.grid) {
			for (Living cell : row) {
				assertTrue(cell.who() == State.EMPTY);
			}
		}
	}
	
	
}
