package de.codermaz.coderetreat.gameoflife;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;


public class BoardTest
{
	private int[][] sampleBoard9x9 = new int[][] { //
		{ 0, 0, 0, 1, 1, 1, 0, 1, 1 }, //
		{ 1, 0, 0, 1, 1, 0, 0, 1, 0 }, //
		{ 0, 1, 0, 0, 1, 1, 1, 1, 0 }, //
		{ 1, 0, 0, 1, 1, 0, 0, 1, 1 }, //
		{ 1, 1, 1, 0, 1, 0, 0, 1, 0 }, //
		{ 0, 1, 1, 1, 1, 0, 0, 1, 1 }, //
		{ 1, 0, 0, 0, 1, 1, 1, 1, 1 }, //
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
		Assert.assertThat( numberOfAliveCells, CoreMatchers.equalTo( 46 ) );
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

	@Test public void testCellDiesIfLessThan2orMoreThan3Neighbours()
	{

		Board board = new Board( sampleBoard9x9 );

		int cellRow = 1;
		int cellCol = 0;

		nextGeneration( board, cellRow, cellCol );

		int isCellAlive = sampleBoard9x9[cellRow][cellCol];

		Assert.assertThat( isCellAlive, CoreMatchers.equalTo( 0 ) );

	}

	@Test public void testCellLivesIf2NeighboursAndCellIsAlive()
	{
		Board board = new Board( sampleBoard9x9 );

		int cellRow = 6;
		int cellCol = 0;

		nextGeneration( board, cellRow, cellCol );
		int isCellAlive = sampleBoard9x9[cellRow][cellCol];

		Assert.assertThat( isCellAlive, CoreMatchers.equalTo( 1 ) );
	}

	@Test public void testCellLivesIf3Neighbours()
	{
		Board board = new Board( sampleBoard9x9 );

		int cellRow = 1;
		int cellCol = 2;

		nextGeneration( board, cellRow, cellCol );
		int isCellAlive = sampleBoard9x9[cellRow][cellCol];

		Assert.assertThat( isCellAlive, CoreMatchers.equalTo( 1 ) );
	}

	private void nextGeneration(Board board, int cellRow, int cellCol)
	{
		int neighboursCount = board.getNeighboursCount( cellRow, cellCol );
		if( neighboursCount < 2 || neighboursCount > 3 )
		{
			sampleBoard9x9[cellRow][cellCol] = 0;
		}
		else if( neighboursCount == 2 && sampleBoard9x9[cellRow][cellCol] == 1 )
		{
			sampleBoard9x9[cellRow][cellCol] = 1;
		}
		else if( neighboursCount == 3 )
		{
			sampleBoard9x9[cellRow][cellCol] = 1;
		}
	}

}