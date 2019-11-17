package de.codermaz.coderetreat.gameoflife;

import com.airhacks.afterburner.injection.Injector;
import de.codermaz.coderetreat.gameoflife.guifx.GameOfLifeModel;
import de.codermaz.coderetreat.gameoflife.guifx.GameOfLifeView;
import de.codermaz.coderetreat.gameoflife.guifx.saveboard.SaveBoardModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application
{
	public static String TITLE_GAME_OF_LIFE = "Game of Life";

	public static void main(String[] args)
	{
		launch( args );
	}

	@Override
	public void start(Stage primaryStage)
	{
		instantiateModels();
		GameOfLifeView gameOfLifeView = new GameOfLifeView();
		Scene scene = new Scene( gameOfLifeView.getView() );
		final String uri = getClass().getResource( "app.css" ).toExternalForm();
		scene.getStylesheets().add( uri );

		primaryStage.setMinWidth( 1200 );
		primaryStage.setTitle( TITLE_GAME_OF_LIFE );
		primaryStage.setScene( scene );

		primaryStage.show();

	}

	public void instantiateModels()
	{
		Injector.instantiateModelOrService( GameOfLifeModel.class );
		Injector.instantiateModelOrService( SaveBoardModel.class );
	}
}
