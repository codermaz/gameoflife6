package de.codermaz.coderetreat.gameoflife.stdout;

import de.codermaz.coderetreat.gameoflife.gamelogic.GameField;
import de.codermaz.coderetreat.gameoflife.gamelogic.Generation;
import de.codermaz.coderetreat.gameoflife.gamelogic.Samples;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;


public class GameOfLifeStdOut
{
	public GameOfLifeStdOut()
	{

	}

	public static void startGoL()
	{
		printBoardToSOut( Samples.BOARD_9x9 );
		GameField gameField = new GameField( Samples.BOARD_9x9 );
		Generation generation = new Generation( gameField );
		System.out.println( "<next generation>" );
		generation.calculateNextGeneration();
		printBoardToSOut( generation.getNextBoard() );
	}

	private static void printBoardToSOut(int[][] board)
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

	// FOR JAVAFX without afterburner
	public void start(Stage primaryStage) throws Exception
	{
		String file = "gameoflife.fxml";
		Optional<URL> rootResource = getResource( file );
		if( !rootResource.isPresent() )
		{
			printFileErrorAndExit( file );
		}
		else
		{
			try
			{
				//				GameOfLifeModel gameOfLifeModel = new GameOfLifeModel( Samples.BOARD_9x9 );
				Parent root = FXMLLoader.load( rootResource.get() );
				primaryStage.setTitle( "Game Of Life" );
				primaryStage.setScene( new Scene( root ) );
				// startGoL();
				primaryStage.show();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private Optional<URL> getResource(String file) throws java.io.IOException
	{
		// 1. Alternative
		//		String baseLocationForNewFile= "src/main/java/de/codermaz/coderetreat/";
		//		URL url = new File( baseLocationForNewFile+ "gameoflife/gameoflife.fxml" ).toURI().toURL();
		//		Parent root = FXMLLoader.load( url );
		// 2. Alternative
		String baseLocation = "de/codermaz/coderetreat/gameoflife/";
		URL resource = getClass().getClassLoader().getResource( baseLocation + file );
		return Optional.ofNullable( resource );
	}

	private void printFileErrorAndExit(String file)
	{
		System.err.println( "file '" + file + "' not found" );
		Platform.exit();
	}

}
