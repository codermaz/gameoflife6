package de.codermaz.coderetreat.gameoflife;

import com.airhacks.afterburner.injection.Injector;
import de.codermaz.coderetreat.gameoflife.guifx.GameOfLifeModel;
import de.codermaz.coderetreat.gameoflife.guifx.GameOfLifeView;
import de.codermaz.coderetreat.gameoflife.guifx.configuration.ConfigurationModel;
import de.codermaz.coderetreat.gameoflife.guifx.saveboard.SaveBoardModel;
import de.codermaz.coderetreat.gameoflife.services.watchdir.GoLWatchService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;


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
		injectCustomProperties(); // experimental
		instantiateModels();
		GameOfLifeView gameOfLifeView = new GameOfLifeView();
		Scene scene = new Scene( gameOfLifeView.getView() );

		primaryStage.setMinWidth( 1200 );
		primaryStage.setTitle( TITLE_GAME_OF_LIFE );
		primaryStage.setScene( scene );
		final String uri = getClass().getResource( "app.css" ).toExternalForm();
		scene.getStylesheets().add( uri );

		primaryStage.show();

	}

	public void instantiateModels()
	{
		Injector.instantiateModelOrService( ConfigurationModel.class );
		Injector.instantiateModelOrService( GameOfLifeModel.class );
		Injector.instantiateModelOrService( SaveBoardModel.class );
		Injector.instantiateModelOrService( GoLWatchService.class );
	}

	public void injectCustomProperties()
	{
		LocalDate date = LocalDate.of( 2019, Month.NOVEMBER, 23 );
		Map<Object, Object> customProperties = new HashMap<>();
		customProperties.put( "experimentalDate", date ); // injected in GameOfLifePresenter
		Injector.setConfigurationSource( customProperties::get );
	}

	@Override
	public void stop() throws Exception
	{
		Injector.forgetAll();
	}
}
