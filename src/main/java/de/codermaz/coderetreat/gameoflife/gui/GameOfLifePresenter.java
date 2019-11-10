package de.codermaz.coderetreat.gameoflife.gui;

import de.codermaz.coderetreat.gameoflife.App;
import de.codermaz.coderetreat.gameoflife.gamelogic.GameField;
import de.codermaz.coderetreat.gameoflife.gamelogic.Generation;
import de.codermaz.coderetreat.gameoflife.model.BoardInfo;
import de.codermaz.coderetreat.gameoflife.model.BoardInfoXml;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GameOfLifePresenter
{

	@Inject
	GameOfLifeModel gameOfLifeModel;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML
	private VBox     mainVBox;
	@FXML
	private MenuItem menuOpen;
	@FXML
	private MenuItem menuSave;
	@FXML
	private MenuItem menuQuit;

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
	private ColumnConstraints        columnConstraints = new ColumnConstraints();
	private RowConstraints           rowConstraints    = new RowConstraints();
	private GameField                gameField;
	private Generation               generation;
	private ScheduledExecutorService scheduledExecutorService;
	private Future<?>                future;
	private Runnable                 nextGenerationTask;

	@FXML
	void initialize()
	{
		handleExecuterService();
		handleFileMenu();

		board = gameOfLifeModel.gameBoardProperty().get();
		resetBoard = board.clone();
		gameOfLifeModel.gameBoardProperty().addListener( (observable, oldValue, newValue) -> {
			if( newValue != null )
			{
				board = newValue;
				startGame();
			}
		} );

		startGame();
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
		menuOpen.setOnAction( event -> {
			File lastChosenBoardXmlFile = gameOfLifeModel.lastChoosenFileProperty().get();
			if( lastChosenBoardXmlFile != null )
				fileChooser.setInitialDirectory( new File( lastChosenBoardXmlFile.getParent() ) );
			File fileChosen = fileChooser.showOpenDialog( (boardGrid).getScene().getWindow() );
			BoardInfo boardInfo = new BoardInfo();
			Optional<BoardInfoXml> boardInfoXml = boardInfo.jaxbXmlFileToObject( fileChosen.toString() );
			if( boardInfoXml.isPresent() )
			{
				Stage stage = (Stage)mainVBox.getScene().getWindow();
				stage.setTitle( App.TITLE_GAME_OF_LIFE + " - " + boardInfoXml.get().getName() + " - " + fileChosen.getAbsolutePath() );
				gameOfLifeModel.lastChoosenFileProperty().set( fileChosen );

				int[][] newBoard = boardInfo.transferBoardXmlToGameBoard( boardInfoXml.get() );

				cleanBoardGrid();
				gameOfLifeModel.gameBoardProperty().set( newBoard );
				resetBoard = newBoard.clone();
			}

		} );
		menuQuit.setOnAction( event -> Platform.exit() );
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

		nextGenerationTask = () -> showNextGenerationInFxAppThread();

		exitButton.setOnAction( actionEvent -> {
			scheduledExecutorService.shutdown();
			System.out.println( "scheduledExecutorService is shutdown." );
			Platform.exit();
		} );
	}

	private void showNextGenerationInFxAppThread()
	{
		if( Platform.isFxApplicationThread() )
			showNextGeneration();
		else
			Platform.runLater( this::showNextGeneration );
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
}

