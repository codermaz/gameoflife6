package de.codermaz.coderetreat.gameoflife;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;


public class BoardTest
{

	@Test public void testNumberOfCellsOfInitialBoard()
	{
		int[][] sampleBoard9x9 = prepareManualSampleBoard9x9();
		Board board = new Board( sampleBoard9x9 );

		Assert.assertThat( board.readNumberOfCells(), IsEqual.equalTo( 81 ) );
	}

	@Test public void testNumberOfAliveCellsOfBoard()
	{
		int[][] sampleBoard9x9 = prepareManualSampleBoard9x9();
		Board board = new Board( sampleBoard9x9 );

		int numberOfAliveCells = board.getNumberOfAliveCells( sampleBoard9x9 );
		Assert.assertThat( numberOfAliveCells, CoreMatchers.equalTo( 45 ) );
	}

	int[][] prepareManualSampleBoard9x9()
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