package de.codermaz.coderetreat.gameoflife;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class GameField
{
	private static final int         NOT_SQUARE_BOARD = -1;
	private final        int[][]     board;
	private final        List<int[]> boardAsList;
	private              int         rowsNumber;

	public GameField(int[][] board)
	{
		this.board = board;
		boardAsList = Arrays.stream( this.board ).collect( Collectors.toList() );
	}

	public int getNumberOfCells()
	{
		int numberOfCells = 0;
		rowsNumber = board.length;

		for( int[] row : board )
		{
			// check: column numbers are equal to row numbers
			if( row.length != rowsNumber )
				return NOT_SQUARE_BOARD;
			numberOfCells += row.length;
		}

		return numberOfCells;
	}

	public int getNumberOfAliveCells(int[][] board)
	{
		int aliveCells = 0;
		for( int[] row : board )
		{
			for( int cell : row )
			{
				if( cell == 1 )
				{
					aliveCells++;
				}
			}
		}
		return aliveCells;
	}

	public int getNeighboursCount(int cellRow, int cellCol)
	{
		List<int[]> neighbourRows = readNeighbourRows( cellRow );
		int neighboursWithOwnValue = readNeighboursInCol( neighbourRows, cellCol );
		int neighboursCount = neighboursWithOwnValue - boardAsList.get( cellRow )[cellCol];

		return neighboursCount;
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

	public int[][] getBoard()
	{
		return board;
	}

	public int getRowsNumber()
	{
		return rowsNumber;
	}

}
