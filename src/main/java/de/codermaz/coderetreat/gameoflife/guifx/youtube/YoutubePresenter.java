package de.codermaz.coderetreat.gameoflife.guifx.youtube;

import de.codermaz.coderetreat.gameoflife.guifx.GameOfLifeModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.io.File;

import static de.codermaz.coderetreat.gameoflife.App.TITLE_GAME_OF_LIFE;


public class YoutubePresenter
{
	@FXML
	AnchorPane  youtubeAnchorPane;
	@FXML
	Button      backToGoLButton;
	@FXML
	ProgressBar loadContentProgressBar;
	@FXML
	WebView     youtubeWebView;
	private SimpleObjectProperty<Stage> primaryStage;
	private StringProperty              boardNameText  = new SimpleStringProperty();
	private SimpleObjectProperty<File>  lastChosenFile = new SimpleObjectProperty();
	private WebEngine                   webEngine;
	// A Worker load the page
	private Worker<Void>                worker;
	@Inject
	private GameOfLifeModel             gameOfLifeModel;

	@FXML
	void initialize()
	{
		primaryStage = gameOfLifeModel.primaryStageProperty();

		prepareButtons();
		showContentFromYoutube();
		prepareContentBindings();
	}

	private void prepareButtons()
	{
		backToGoLButton.setOnAction( event -> returnToGoL() );
	}

	private void returnToGoL()
	{
		primaryStage.get().setScene( gameOfLifeModel.returnSceneProperty().get() );
		webEngine.load( null );
		String title = boardNameText.get() == null ? TITLE_GAME_OF_LIFE :
			TITLE_GAME_OF_LIFE + " - " + boardNameText.get() + " - " + lastChosenFile.get().getAbsolutePath();
		primaryStage.get().setTitle( title );
		//			primaryStage.get().setMaximized( false );
		primaryStage.get().setFullScreen( false );
		primaryStage.get().show();
	}

	private void showContentFromYoutube()
	{
		webEngine = youtubeWebView.getEngine();
		worker = webEngine.getLoadWorker();
		webEngine.load( "https://www.youtube.com/watch?v=E8kUJL04ELA" );
	}

	private void prepareContentBindings()
	{
		worker.stateProperty().addListener( (observable, oldValue, newValue) -> {
			if( newValue == Worker.State.SUCCEEDED )
			{
				loadContentProgressBar.setVisible( false );
				bindKeyProperties();
			}
		} );

		loadContentProgressBar.progressProperty().bind( worker.progressProperty() );
	}

	private void bindKeyProperties()
	{
		Stage primaryStage = (Stage)youtubeAnchorPane.getScene().getWindow();
		if( primaryStage != null )
			primaryStage.getScene().setOnKeyPressed( event -> {
				switch(event.getCode())
				{
				case ESCAPE:
					System.out.println( "escape is pressed" );
					break;
				case LEFT:
					returnToGoL();
					break;
				}
			} );
	}
}
