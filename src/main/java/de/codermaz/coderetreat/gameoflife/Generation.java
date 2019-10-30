package de.codermaz.coderetreat.gameoflife;

public class Generation
{

	private final int[][]   futureBoard;
	private final GameField gameField;

	public Generation(GameField gameField)
	{
		this.gameField = gameField;
		int[][] board = gameField.getBoard();
		futureBoard = new int[board.length][board[0].length];
	}

	public void nextGeneration(int cellRow, int cellCol)
	{
		int neighboursCount = gameField.getNeighboursCount( cellRow, cellCol );
		if( neighboursCount < 2 || neighboursCount > 3 )
		{
			futureBoard[cellRow][cellCol] = 0;
		}
		else if( neighboursCount == 2 && gameField.getBoard()[cellRow][cellCol] == 1 )
		{
			futureBoard[cellRow][cellCol] = 1;
		}
		else if( neighboursCount == 3 )
		{
			futureBoard[cellRow][cellCol] = 1;
		}
	}

	public void calculateNextGeneration()
	{
		int rowNumbers = gameField.getBoard().length;
		int colNumbers = gameField.getBoard()[0].length;

		for( int i = 0; i < rowNumbers; i++ )
		{
			for( int j = 0; j < colNumbers; j++ )
			{
				nextGeneration( i, j );
			}
		}

	}

	public int[][] getNextBoard()
	{
		return futureBoard;
	}
}
