package de.codermaz.coderetreat.gameoflife;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class GameFieldTest
{
	private GameField gameField;

	@Before public void setUp()
	{
		gameField = new GameField( Samples.BOARD_9x9 );
	}

	@Test public void testNumberOfCellsOfInitialBoard()
	{

		int[][] board = gameField.getBoard();
		Assert.assertThat( gameField.getNumberOfCells(), IsEqual.equalTo( board.length * board[0].length ) );
	}

	@Test public void testNumberOfAliveCellsOfBoard()
	{

		int numberOfAliveCells = gameField.getNumberOfAliveCells( gameField.getBoard() );
		Assert.assertThat( numberOfAliveCells, CoreMatchers.equalTo( 46 ) );
	}

	@Test public void testNeighboursCount()
	{
		int cellRow;
		int cellCol;
		int neighboursCount;
		int[][] tests = { { 7, 8, 4 }, { 0, 0, 1 }, { 0, 8, 2 }, { 8, 8, 2 }, { 0, 4, 4 }, { 1, 0, 1 } };

		for( int[] test : tests )
		{
			cellRow = test[0];
			cellCol = test[1];
			neighboursCount = test[2];
			printOut( cellRow, cellCol, neighboursCount, "neighboursCount" );
			Assert.assertThat( "number of neighbours is wrong", gameField.getNeighboursCount( cellRow, cellCol ),
				CoreMatchers.equalTo( neighboursCount ) );
		}
	}

	@Test public void testCellDiesIfLessThan2orMoreThan3Neighbours()
	{
		Generation generation = new Generation( gameField );

		int[][] tests = { { 0, 4, 0 }, { 7, 8, 0 }, { 1, 0, 0 } };

		for( int[] test : tests )
		{
			int cellRow = test[0];
			int cellCol = test[1];
			int isAlive = test[2];
			printOut( cellRow, cellCol, isAlive, "isAlive" );
			generation.nextGeneration( cellRow, cellCol );

			int isCellAlive = generation.getNextBoard()[cellRow][cellCol];

			Assert.assertThat( isCellAlive, CoreMatchers.equalTo( isAlive ) );
		}

	}

	@Test public void testCellLivesIf2NeighboursAndCellIsAlive()
	{
		Generation generation = new Generation( gameField );

		int cellRow = 8;
		int cellCol = 7;

		generation.nextGeneration( cellRow, cellCol );
		int isCellAlive = generation.getNextBoard()[cellRow][cellCol];

		Assert.assertThat( isCellAlive, CoreMatchers.equalTo( 1 ) );
	}

	@Test public void testCellLivesIf3Neighbours()
	{
		Generation generation = new Generation( gameField );

		int cellRow = 1;
		int cellCol = 2;

		generation.nextGeneration( cellRow, cellCol );
		int isCellAlive = generation.getNextBoard()[cellRow][cellCol];

		Assert.assertThat( isCellAlive, CoreMatchers.equalTo( 1 ) );
	}

	private void printOut(int cellRow, int cellCol, int expected, String expectedVariable)
	{
		System.out.println(
			"test [cellRow = " + cellRow + ", cellCol = " + cellCol + ", expected '" + expectedVariable + "' = " + expected + "]" );
	}

}