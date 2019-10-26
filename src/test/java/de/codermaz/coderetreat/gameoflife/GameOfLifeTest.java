package de.codermaz.coderetreat.gameoflife;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class GameOfLifeTest
{

	private int[][] board;

	@Before public void setUp()
	{
		BoardTest boardTest = new BoardTest();
		board = boardTest.prepareSampleBoard9x9();
	}

	@After public void tearDown()
	{
	}

	@Test public void testNeighboursCount()
	{
		int neighboursCount = 0; // for row=0, col=4

		Assert.assertThat( "number of neighbors is wrong", neighboursCount, CoreMatchers.equalTo( 4 ) );
	}
}