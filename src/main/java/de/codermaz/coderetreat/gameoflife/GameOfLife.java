package de.codermaz.coderetreat.gameoflife;

public class GameOfLife
{
	private static final Samples samples = new Samples();

	public GameOfLife()
	{

	}

	public static void main(String[] args)
	{
		printBoard( Samples.BOARD_9x9 );
		GameField gameField = new GameField( Samples.BOARD_9x9 );
		Generation generation = new Generation( gameField );
		System.out.println( "<next generation>" );
		generation.calculateNextGeneration();
		printBoard( generation.getNextBoard() );
	}

	private static void printBoard(int[][] board)
	{
		for( int[] row : board )
		{
			for( int cell : row )
			{
				System.out.print( cell + " " );
			}
			System.out.println();
		}
	}

}
