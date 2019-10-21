package de.codermaz.coderetreat;

public class Board
{
	private final int[][] board;

	public Board(int[][] board)
	{
		this.board = board;
	}

	public int readNumberOfFiledsOfInitialBoard()
	{
		int numberOfFields = 0;
		int rowsNumber = board.length;

		for( int[] row : board )
		{
			// check: column numbers are equal to row numbers
			if( row.length != rowsNumber )
				return -1;
			numberOfFields += row.length;
		}

		return numberOfFields;
	}

}
