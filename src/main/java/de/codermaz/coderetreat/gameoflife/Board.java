package de.codermaz.coderetreat.gameoflife;

public class Board
{
	public static final int     WRONG_BOARD_MATRIX = -1;
	private final       int[][] board;
	private             int     rowsNumber;

	public Board(int[][] board)
	{
		this.board = board;
	}

	public int readNumberOfCells()
	{
		int numberOfCells = 0;
		rowsNumber = board.length;

		for( int[] row : board )
		{
			// check: column numbers are equal to row numbers
			if( row.length != rowsNumber )
				return WRONG_BOARD_MATRIX;
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

	public int[][] getBoard()
	{
		return board;
	}

	public int getRowsNumber()
	{
		return rowsNumber;
	}

}
