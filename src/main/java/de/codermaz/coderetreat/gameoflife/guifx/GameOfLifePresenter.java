package de.codermaz.coderetreat.gameoflife.guifx;

import de.codermaz.coderetreat.gameoflife.gamelogic.GameField;
import de.codermaz.coderetreat.gameoflife.gamelogic.Generation;
import de.codermaz.coderetreat.gameoflife.guifx.configuration.ConfigurationModel;
import de.codermaz.coderetreat.gameoflife.guifx.saveboard.SaveBoardModel;
import de.codermaz.coderetreat.gameoflife.guifx.saveboard.SaveBoardView;
import de.codermaz.coderetreat.gameoflife.guifx.youtube.YoutubeView;
import de.codermaz.coderetreat.gameoflife.model.BoardInfo;
import de.codermaz.coderetreat.gameoflife.model.BoardInfoXml;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static de.codermaz.coderetreat.gameoflife.App.TITLE_GAME_OF_LIFE;


public class GameOfLifePresenter
{
	@Inject
	ConfigurationModel configurationModel;
	@Inject
	LocalDate          experimentalDate;
	@Inject
	GameOfLifeModel    gameOfLifeModel;
	@Inject
	SaveBoardModel     saveBoardModel;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML
	private VBox     mainVBox;
	@FXML
	private Menu     fileMenu;
	@FXML
	private MenuItem menuOpen;
	@FXML
	private Menu     openRecentMenu;
	@FXML
	private MenuItem menuSave;
	@FXML
	private MenuItem menuSaveAs;
	@FXML
	private MenuItem menuQuit;
	@FXML
	private MenuItem menuInfo;
	@FXML
	private MenuItem menuInfoConway;

	@FXML
	private BarChart<Integer, Integer> aliveCellsBarChart;
	@FXML
	private GridPane                   boardGrid;
	@FXML
	private Label                      yearsGoneCountLabel;
	@FXML
	private Label                      aliveCellsCountLabel;
	@FXML
	private Slider                     secondsSlider;
	@FXML
	private Label                      inSecondsLabel;
	@FXML
	private Button                     exitButton;
	@FXML
	private Button                     showNextInIntervalButton;
	@FXML
	private Button                     stopGenerationButton;

	private int[][]                  board;
	private int[][]                  resetBoard;
	private ColumnConstraints        columnConstraints     = new ColumnConstraints();
	private RowConstraints           rowConstraints        = new RowConstraints();
	private GameField                gameField;
	private Generation               generation;
	private ScheduledExecutorService scheduledExecutorService;
	private Future<?>                future;
	private Runnable                 nextGenerationTask;
	private String                   GAME_OF_LIFE_WIKI_URL = "https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life";

	@FXML
	void initialize()
	{
		handleExecuterService();
		handleFileMenu();
		lookForStartXmlFile();
		prepareBoardBindings();

		startGame();
	}

	private void prepareBoardBindings()
	{
		board = gameOfLifeModel.gameBoardProperty().get();
		resetBoard = board.clone();
		gameOfLifeModel.gameBoardProperty().addListener( (observable, oldValue, newValue) -> {
			if( newValue != null )
			{
				board = newValue;
				resetBoard = board.clone();
				startGame();
			}
		} );
	}

	private void lookForStartXmlFile()
	{
		File startBoardXmlFile = new File( configurationModel.getStartBoardXml() );
		if( startBoardXmlFile.exists() )
		{
			updateBoardWithXmlFile( startBoardXmlFile );
			System.out.println(
				"start board is set according to board xml file: " + configurationModel.getStartBoardXml() + ", experimental date info: "
					+ experimentalDate );
		}
	}

	private void startGame()
	{
		updateGameField();
		gameOfLifeModel.aliveCellsProperty().set( gameField.getNumberOfAliveCells( board ) );
		updateBarChart();

		prepareBoardGrid();
		handleTextProperties();
		handleDisabilityProperties();
		handleSecondsSlider();

		showInitialBoard();
	}

	private void handleFileMenu()
	{
		final FileChooser fileChooser = new FileChooser();
		configuringFileChooser( fileChooser );
		menuOpen.setOnAction( event -> menuOpenPressed( fileChooser ) );
		menuSaveAs.setOnAction( event -> menuSaveAsSelected() );
		menuQuit.setOnAction( event -> Platform.exit() );

		gameOfLifeModel.setOpenRecentMenu( openRecentMenu );
		openRecentMenu.getItems().addListener( (ListChangeListener<? super MenuItem>)c -> {
			ObservableList<? extends MenuItem> openRecentMenuItems = c.getList();
			openRecentMenuItems.forEach( menuItem -> //
				menuItem.setOnAction( event -> //
					updateBoardWithXmlFile( new File( menuItem.getText() ) ) ) );
		} );
		gameOfLifeModel.setFileMenu( fileMenu );

		menuInfo.setOnAction( event -> menuHelpSelected() );
		menuInfoConway.setOnAction( event -> menuInfoConwaySelected() );

	}

	private void menuInfoConwaySelected()
	{
		YoutubeView youtubeView = new YoutubeView();
		Scene youtubeScene = new Scene( youtubeView.getView() );
		Stage primaryStage = (Stage)mainVBox.getScene().getWindow();
		gameOfLifeModel.primaryStageProperty().set( primaryStage );
		gameOfLifeModel.returnSceneProperty().set( primaryStage.getScene() );
		primaryStage.setTitle( TITLE_GAME_OF_LIFE + " - Does John Conway hate his Game of Life?" );
		primaryStage.setScene( youtubeScene );
		//		primaryStage.setMaximized( true );
		primaryStage.setFullScreen( true );
		primaryStage.show();

	}

	private void menuHelpSelected()
	{
		String contentText =
			"Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. "
				+ "At each step in time, the following transitions occur:\n\n" //
				+ "1. Any live cell with two or three neighbors survives.\n\n"
				+ "2. Any dead cell with three live neighbors becomes a live cell.\n\n"
				+ "3. All other live cells die in the next generation. Similarly, all other dead cells stay dead.\n\n"
				+ "Do you want to open WikiPage in browser?\n"//
				+ GAME_OF_LIFE_WIKI_URL;
		Alert alert = new Alert( Alert.AlertType.INFORMATION, contentText, ButtonType.YES, ButtonType.NO );
		alert.titleProperty().setValue( "Info" );
		alert.headerTextProperty().setValue( "Conway's Game of Life" );
		Optional<ButtonType> result = alert.showAndWait();
		if( result.get() == ButtonType.YES )
		{
			try
			{
				Desktop.getDesktop().browse( new URL( GAME_OF_LIFE_WIKI_URL ).toURI() );
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch(URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			// ... user chose NO or closed the dialog
		}
	}

	private void menuSaveAsSelected()
	{
		SaveBoardView saveBoardView = new SaveBoardView();
		Scene scene = new Scene( saveBoardView.getView() );
		Stage primaryStage = (Stage)mainVBox.getScene().getWindow();
		gameOfLifeModel.primaryStageProperty().set( primaryStage );
		gameOfLifeModel.returnSceneProperty().set( primaryStage.getScene() );
		primaryStage.setMinWidth( 1200 );
		File lastChosenBoardXmlFile = saveBoardModel.lastChosenFileProperty().get();
		String fileName = lastChosenBoardXmlFile == null ? " " : " " + lastChosenBoardXmlFile.getAbsolutePath() + " ";
		primaryStage.setTitle( TITLE_GAME_OF_LIFE + fileName + "- Save board" );
		primaryStage.setScene( scene );
		primaryStage.show();
	}

	private void menuOpenPressed(FileChooser fileChooser)
	{
		File lastChosenBoardXmlFile = saveBoardModel.lastChosenFileProperty().get();
		if( lastChosenBoardXmlFile != null )
			fileChooser.setInitialDirectory( new File( lastChosenBoardXmlFile.getParent() ) );
		File fileChosen = fileChooser.showOpenDialog( (boardGrid).getScene().getWindow() );
		if( fileChosen == null )
			return;
		updateBoardWithXmlFile( fileChosen );
	}

	private void updateBoardWithXmlFile(File fileChosen)
	{
		BoardInfo boardInfo = new BoardInfo();
		Optional<BoardInfoXml> boardInfoXml = boardInfo.jaxbXmlFileToObject( fileChosen.toString() );
		if( boardInfoXml.isPresent() )
		{
			saveBoardModel.addToRecentFiles( fileChosen );
			saveBoardModel.lastChosenFileProperty().set( fileChosen );
			Deque<File> recentOpenedFiles = saveBoardModel.getRecentOpenedFiles();
			gameOfLifeModel.prepareSubMenu( recentOpenedFiles );
			if( mainVBox.getScene() != null )
			{
				Stage stage = (Stage)mainVBox.getScene().getWindow();
				stage.setTitle( TITLE_GAME_OF_LIFE + " - " + boardInfoXml.get().getName() + " - " + fileChosen.getAbsolutePath() );
			}

			int[][] newBoard = boardInfo.transferBoardXmlToGameBoard( boardInfoXml.get() );

			cleanBoardGrid();
			gameOfLifeModel.gameBoardProperty().set( newBoard );
			gameOfLifeModel.yearsGoneProperty().set( 0 );
			aliveCellsBarChart.getData().clear();
		}
		else
		{
			saveBoardModel.removeFileFromRecentOpened( fileChosen );
			gameOfLifeModel.prepareSubMenu( saveBoardModel.getRecentOpenedFiles() );
		}
	}

	private void configuringFileChooser(FileChooser fileChooser)
	{
		fileChooser.setTitle( "Select board xml file" );

		fileChooser.getExtensionFilters().addAll(//
			new FileChooser.ExtensionFilter( "All Files", "*.*" ), //
			new FileChooser.ExtensionFilter( "XML", "*.xml" ) );
	}

	private void handleSecondsSlider()
	{
		secondsSlider.valueProperty().addListener( (observable, oldValue, newValue) -> {
			if( !secondsSlider.valueChangingProperty().get() )
			{
				if( newValue.longValue() != 0 )
				{
					restartExecutorService( newValue.longValue() );
				}
				else
				{
					showNextInIntervalButton.disableProperty().set( true );
				}
			}
		} );
	}

	private void restartExecutorService(long timeInterval)
	{
		if( Objects.nonNull( future ) )
		{
			showNextInIntervalButton.disableProperty().set( true );
			future.cancel( true );
			startScheduledFuture( (int)timeInterval );
		}
	}

	private void handleDisabilityProperties()
	{
		showNextInIntervalButton.disableProperty()
			.addListener( (observable, oldValue, newValue) -> stopGenerationButton.disableProperty().setValue( oldValue ) );

		stopGenerationButton.disableProperty().setValue( true );
	}

	private void handleExecuterService()
	{
		scheduledExecutorService = Executors.newScheduledThreadPool( 1 );

		nextGenerationTask = this::showNextGenerationInFxAppThread;

		exitButton.setOnAction( actionEvent -> {
			scheduledExecutorService.shutdown();
			System.out.println( "scheduledExecutorService is shutdown." );
			Platform.exit();
		} );
	}

	private void prepareBoardGrid()
	{
		assert boardGrid != null : "fx:id=\"boardGrid\" was not injected: check your FXML file 'gameoflife.fxml'.";
		boardGrid.getStyleClass().add( "gridpane" );

		double colPercentage = 100 / board[0].length - 1;
		columnConstraints.setPercentWidth( colPercentage );
		columnConstraints.setHgrow( Priority.ALWAYS );
		//		columnConstraints.setFillWidth( true );

		double rowPercentage = 100 / board.length;
		rowConstraints.setPercentHeight( rowPercentage );
		rowConstraints.setVgrow( Priority.ALWAYS );

		boardGrid.setMaxSize( Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE );
	}

	private void handleTextProperties()
	{
		yearsGoneCountLabel.textProperty().bind( new StringBinding()
		{
			{
				bind( gameOfLifeModel.yearsGoneProperty() );
			}

			@Override
			protected String computeValue()
			{
				return String.valueOf( gameOfLifeModel.getYearsGone() );
			}
		} );

		aliveCellsCountLabel.textProperty().bind( new StringBinding()
		{
			{
				bind( gameOfLifeModel.aliveCellsProperty() );
			}

			@Override
			protected String computeValue()
			{
				int numberOfAliveCells = gameField.getNumberOfAliveCells( board );
				if( numberOfAliveCells == 0 )
					stopAutoGeneration();
				return String.valueOf( numberOfAliveCells );
			}
		} );

		inSecondsLabel.textProperty().bind( new StringBinding()
		{
			{
				bind( secondsSlider.valueProperty() );
			}

			@Override
			protected String computeValue()
			{
				int millis = (int)secondsSlider.getValue();
				String template = "in " + millis + " millis automatically";
				return template;
			}
		} );

	}

	private void updateBarChart()
	{
		if( aliveCellsBarChart.getData().size() < 100 )
		{
			XYChart.Series series = new XYChart.Series();
			String yearsGoneAsString = String.valueOf( gameOfLifeModel.getYearsGone() );
			series.setName( yearsGoneAsString );
			series.getData().add( new XYChart.Data( yearsGoneAsString, gameField.getNumberOfAliveCells( board ) ) );

			aliveCellsBarChart.getData().add( series );
		}
	}

	private void updateGameField()
	{
		gameField = new GameField( board );
		generation = new Generation( gameField );
	}

	private void showInitialBoard()
	{
		showBoard( board );
	}

	private void showBoard(int[][] board)
	{
		for(int i = 0; i < board.length; i++) // rows number of board
		{
			boardGrid.getColumnConstraints().add( columnConstraints );
			boardGrid.getRowConstraints().add( rowConstraints );
			for(int j = 0; j < this.board[0].length; j++) // columns number of board
			{
				boardGrid.add( newCell( this.board[i][j] ), j, i );
			}
		}
	}

	private Label newCell(int isAlive)
	{
		// for Labels text within
		//		Label label = new Label( String.valueOf( isAlive ) );
		//		label.setAlignment( Pos.CENTER );
		Label label = new Label();
		if( isAlive == 1 )
			label.setBackground( new Background( new BackgroundFill( Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY ) ) );
		else
			label.setBackground( new Background( new BackgroundFill( Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY ) ) );

		label.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
		return label;
	}

	@FXML
	void nextGenerationButtonPressed(ActionEvent event)
	{
		showNextGenerationInFxAppThread();
	}

	private void showNextGeneration()
	{
		cleanBoardGrid();

		updateGameField();
		generation.calculateNextGeneration();
		board = generation.getNextBoard();

		gameOfLifeModel.yearsGonePlusByOne();
		gameOfLifeModel.aliveCellsProperty().set( gameField.getNumberOfAliveCells( board ) );
		gameOfLifeModel.aliveCellsProperty().get(); // to invalidate BooleanBinding
		updateBarChart();
		showBoard( board );
	}

	private void cleanBoardGrid()
	{
		boardGrid.getChildren().clear();
		boardGrid.getRowConstraints().clear();
		boardGrid.getColumnConstraints().clear();
	}

	@FXML
	void stopGenerationButtonPressed(ActionEvent actionEvent)
	{
		stopAutoGeneration();
	}

	private void stopAutoGeneration()
	{
		if( Objects.nonNull( future ) && !future.isCancelled() )
		{
			System.out.println( "scheduledExecutorService is cancelled" );
			future.cancel( true );
		}
		showNextInIntervalButton.disableProperty().setValue( false );
	}

	@FXML
	void showNextGenerationInIntervalButtonPressed(ActionEvent actionEvent)
	{
		showNextInIntervalButton.disableProperty().setValue( true );
		long interval = (long)secondsSlider.getValue();
		if( interval != 0 )
			startScheduledFuture( interval );
	}

	private void startScheduledFuture(long interval)
	{
		System.out.println( "Time interval for scheduled future is set to " + interval + " millis" );
		future = scheduledExecutorService.scheduleAtFixedRate( nextGenerationTask, interval, interval, TimeUnit.MILLISECONDS );

	}

	@FXML
	void resetFieldButtonPressed(ActionEvent actionEvent)
	{
		cleanBoardGrid();
		gameOfLifeModel.yearsGoneProperty().set( 0 );
		aliveCellsBarChart.getData().clear();

		gameOfLifeModel.gameBoardProperty().set( null );
		gameOfLifeModel.gameBoardProperty().set( resetBoard );
	}

	@FXML
	void mouseOnBoardClicked(MouseEvent mouseEvent)
	{
		try
		{
			Label source = (Label)mouseEvent.getTarget();

			Integer colIndex = GridPane.getColumnIndex( source );
			Integer rowIndex = GridPane.getRowIndex( source );

			int isAlive = board[rowIndex][colIndex];
			board[rowIndex][colIndex] = (isAlive + 1) % 2;

			if( isAlive == 1 )
				source.setBackground( new Background( new BackgroundFill( Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY ) ) );
			else
				source.setBackground( new Background( new BackgroundFill( Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY ) ) );
			gameOfLifeModel.aliveCellsProperty().set( gameField.getNumberOfAliveCells( board ) );
			gameOfLifeModel.aliveCellsProperty().get();
		}
		catch(ClassCastException e)
		{
			// javafx.scene.layout.GridPane cannot be cast to javafx.scene.control.Label
			// ignore, because it occurs if user drags the gridpane
		}
	}

	@FXML
	void emptyFieldButtonPressed(ActionEvent actionEvent)
	{
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				board[i][j] = 0;
			}
		}
		cleanBoardGrid();
		gameOfLifeModel.yearsGoneProperty().set( 0 );
		aliveCellsBarChart.getData().clear();

		updateGameField();
		showInitialBoard();
	}

	private void showNextGenerationInFxAppThread()
	{
		if( Platform.isFxApplicationThread() )
			showNextGeneration();
		else
			Platform.runLater( this::showNextGeneration );
	}
}

