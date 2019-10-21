package de.codermaz.coderetreat;

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

	private int numberOfFieldsOfInitialBoard()
	{
		int[][] board = prepareSampleBoard9x9();

		return readNumberOfFiledsOfInitialBoard( board );
	}

	private int readNumberOfFiledsOfInitialBoard(int[][] board)
	{
		int numberOfFields = 0;
		int rowsNumber = board.length;

		for( int[] row : board )
		{
			// check: column numbers are equal to row numbers
			if( row.length != rowsNumber )
				return -1;
			numberOfFields+=row.length;
		}

		return numberOfFields;
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