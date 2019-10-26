package de.codermaz.coderetreat.gameoflife;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;


public class BoardTest
{
	private int[][] sampleBoard9x9 = new int[][] { //
		{ 0, 0, 0, 1, 1, 1, 0, 1, 1 }, //
		{ 1, 0, 1, 1, 1, 0, 0, 1, 0 }, //
		{ 0, 1, 0, 0, 1, 1, 1, 1, 0 }, //
		{ 1, 0, 0, 1, 1, 0, 0, 1, 1 }, //
		{ 1, 1, 1, 0, 1, 0, 0, 1, 0 }, //
		{ 0, 1, 1, 1, 1, 0, 0, 1, 0 }, //
		{ 0, 0, 0, 0, 1, 1, 1, 1, 1 }, //
		{ 0, 1, 0, 1, 1, 1, 0, 1, 0 }, //
		{ 0, 1, 1, 0, 0, 1, 1, 1, 0 } };

	@Test public void testNumberOfCellsOfInitialBoard()
	{
		Board board = new Board( sampleBoard9x9 );

		Assert.assertThat( board.readNumberOfCells(), IsEqual.equalTo( 81 ) );
	}

	@Test public void testNumberOfAliveCellsOfBoard()
	{
		Board board = new Board( sampleBoard9x9 );

		int numberOfAliveCells = board.getNumberOfAliveCells( sampleBoard9x9 );
		Assert.assertThat( numberOfAliveCells, CoreMatchers.equalTo( 45 ) );
	}

	@Test public void testNeighboursCount()
	{
		int cellRow;
		int cellCol;
		int neighboursCount;
		int[][] tests = { { 0, 0, 1 }, { 0, 8, 2 }, { 8, 8, 2 }, { 0, 4, 4 }, { 1, 0, 1 } };

		Board board = new Board( sampleBoard9x9 );

		for( int[] test : tests )
		{
			cellRow = test[0];
			cellCol = test[1];
			neighboursCount = test[2];
			System.out.println(
				"testNeighboursCount for [cellRow = " + cellRow + ", cellCol = " + cellCol + ", neighboursCount = " + neighboursCount + "]" );
			Assert.assertThat( "number of neighbours is wrong", board.getNeighboursCount( cellRow, cellCol ),
				CoreMatchers.equalTo( neighboursCount ) );
		}
	}

}