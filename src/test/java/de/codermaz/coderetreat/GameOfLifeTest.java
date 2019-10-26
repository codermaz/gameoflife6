package de.codermaz.coderetreat;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class GameOfLifeTest
{

	@Before public void setUp() throws Exception
	{

	}

	@After public void tearDown() throws Exception
	{
	}

	@Test public void testNumberOfFieldsOfInitialBoard()
	{
		Assert.assertThat( numberOfFieldsOfInitialBoard(), IsEqual.equalTo( 81 ) );
	}

	@Test public void testNumberOfDeadAndAliveCellsOfBoard()
	{
		int[][] sampleBoard9x9 = prepareSampleBoard9x9();

		int numberOfAliveCells = getNumberOfAliveCells( sampleBoard9x9 );

		Assert.assertThat( numberOfAliveCells, CoreMatchers.equalTo( 45 ) );
	}

	private int getNumberOfAliveCells(int[][] sampleBoard9x9)
	{
		int numberOfAliveCells;
		int[][] board = sampleBoard9x9;
		int aliveCells = 0;
		for( int[] row : board )
		{
			for( int colInRow : row )
			{
				if( colInRow == 1 )
				{
					aliveCells++;
				}
			}
		}
		numberOfAliveCells = aliveCells;
		return numberOfAliveCells;
	}

	private int numberOfFieldsOfInitialBoard()
	{
		int[][] sampleBoard9x9 = prepareSampleBoard9x9();
		Board board = new Board( sampleBoard9x9 );

		return board.readNumberOfFiledsOfInitialBoard();
	}

	private int[][] prepareSampleBoard9x9()
	{
		return new int[][] { //
			{ 0, 0, 0, 1, 1, 1, 0, 1, 1 }, //
			{ 1, 0, 1, 1, 1, 0, 0, 1, 0 }, //
			{ 0, 1, 0, 0, 1, 1, 1, 1, 0 }, //
			{ 1, 0, 0, 1, 1, 0, 0, 1, 1 }, //
			{ 1, 1, 1, 0, 1, 0, 0, 1, 0 }, //
			{ 0, 1, 1, 1, 1, 0, 0, 1, 0 }, //
			{ 0, 0, 0, 0, 1, 1, 1, 1, 1 }, //
			{ 0, 1, 0, 1, 1, 1, 0, 1, 0 }, //
			{ 0, 1, 1, 0, 0, 1, 1, 1, 0 } };
	}

}