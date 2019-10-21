package de.codermaz.coderetreat;

public class Board
{
	public static final int     WRONG_BOARD_MATRIX = -1;
	private final       int[][] board;
	private             int     rowsNumber;

	public Board(int[][] board)
	{
		this.board = board;
	}

	public int readNumberOfFiledsOfInitialBoard()
	{
		int numberOfFields = 0;
		rowsNumber = board.length;

		for( int[] row : board )
		{
			// check: column numbers are equal to row numbers
			if( row.length != rowsNumber )
				return WRONG_BOARD_MATRIX;
			numberOfFields += row.length;
		}

		return numberOfFields;
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
