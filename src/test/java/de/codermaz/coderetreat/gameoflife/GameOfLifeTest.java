package de.codermaz.coderetreat.gameoflife;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class GameOfLifeTest
{

	private List<int[]> boardAsList;

	@Before public void setUp()
	{
		BoardTest boardTest = new BoardTest();
		boardAsList = Arrays.stream( boardTest.prepareManualSampleBoard9x9() ).collect( Collectors.toList() );
	}

	@After public void tearDown()
	{
	}

	@Test public void testNeighboursCount()
	{
		int cellRow;
		int cellCol;
		int neighboursCount;
		int[][] tests = { { 0, 0, 1 }, { 0, 8, 2 }, { 8, 8, 2 }, { 0, 4, 4 }, { 1, 0, 1 } };
		for( int i = 0; i < tests.length; i++ )
		{
			cellRow = tests[i][0];
			cellCol = tests[i][1];
			neighboursCount = tests[i][2];
			System.out.println(
				"testNeighboursCount for [cellRow = " + cellRow + ", cellCol = " + cellCol + ", neighboursCount = " + neighboursCount + "]" );
			Assert.assertThat( "number of neighbours is wrong", getNeighboursCount( cellRow, cellCol ),
				CoreMatchers.equalTo( neighboursCount ) );
		}
	}

	private int getNeighboursCount(int cellRow, int cellCol)
	{
		List<int[]> neighbourRows = readNeighbourRows( cellRow );
		int neighboursWithOwnValue = readNeighboursInCol( neighbourRows, cellCol );
		// at last own value must be subtracted
		neighboursWithOwnValue -= boardAsList.get( cellRow )[cellCol];

		return neighboursWithOwnValue;
	}

	private List<int[]> readNeighbourRows(int cellRow)
	{
		List<int[]> neighbourRows = new ArrayList<>();
		neighbourRows.add( boardAsList.get( cellRow ) );

		if( cellRow != 0 )
		{
			neighbourRows.add( boardAsList.get( cellRow - 1 ) );
		}
		if( cellRow != boardAsList.size() - 1 )
		{
			neighbourRows.add( boardAsList.get( cellRow + 1 ) );
		}

		return neighbourRows;
	}

	private int readNeighboursInCol(List<int[]> neighbourRows, int cellCol)
	{
		int colNumbers = boardAsList.get( 0 ).length;
		int neighboursCount = 0;
		for( int[] neighbourRow : neighbourRows )
		{
			neighboursCount += neighbourRow[cellCol];

			if( cellCol != 0 )
			{
				neighboursCount += neighbourRow[cellCol - 1];
			}
			if( cellCol != colNumbers - 1 )
			{
				neighboursCount += neighbourRow[cellCol + 1];
			}
		}

		return neighboursCount;
	}
}