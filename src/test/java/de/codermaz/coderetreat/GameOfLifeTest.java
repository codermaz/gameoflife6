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
		Board board = new Board( sampleBoard9x9 );

		int numberOfAliveCells = board.getNumberOfAliveCells( sampleBoard9x9 );
		Assert.assertThat( numberOfAliveCells, CoreMatchers.equalTo( 45 ) );
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