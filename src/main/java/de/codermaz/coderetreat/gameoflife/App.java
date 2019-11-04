package de.codermaz.coderetreat.gameoflife;

import com.airhacks.afterburner.injection.Injector;
import de.codermaz.coderetreat.gameoflife.gui.GameOfLifeModel;
import de.codermaz.coderetreat.gameoflife.gui.GameOfLifeView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application
{
	public static void main(String[] args)
	{
		launch( args );
	}

	@Override public void start(Stage primaryStage)
	{
		Injector.instantiateModelOrService( GameOfLifeModel.class );
		GameOfLifeView gameOfLifeView = new GameOfLifeView();
		Scene scene = new Scene( gameOfLifeView.getView() );
		final String uri = getClass().getResource( "app.css" ).toExternalForm();
		scene.getStylesheets().add( uri );

		primaryStage.setMinWidth( 1200 );
		primaryStage.setTitle( "Game Of Life" );
		primaryStage.setScene( scene );
		primaryStage.show();

	}
}
