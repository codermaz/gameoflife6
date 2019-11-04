package de.codermaz.coderetreat.gameoflife.gui;

import de.codermaz.coderetreat.gameoflife.gamelogic.GameField;
import de.codermaz.coderetreat.gameoflife.gamelogic.Generation;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GameOfLifePresenter
{

	@Inject GameOfLifeModel gameOfLifeModel;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML private BarChart<Integer, Integer> aliveCellsBarChart;

	@FXML private GridPane boardGrid;
	@FXML private Label    yearsGoneCountLabel;
	@FXML private Label    aliveCellsCountLabel;
	@FXML private Slider   secondsSlider;
	@FXML private Label    inSecondsLabel;
	@FXML private Button   exitButton;
	@FXML private Button   showNextInIntervalButton;
	@FXML private Button   stopGenrationButton;

	private int[][]                  board;
	private ColumnConstraints        columnConstraints = new ColumnConstraints();
	private RowConstraints           rowConstraints    = new RowConstraints();
	private GameField                gameField;
	private Generation               generation;
	private ScheduledExecutorService scheduledExecutorService;
	private Future<?>                future;
	private Runnable                 nextGenerationTask;

	@FXML void initialize()
	{
		handleExecuterService();

		board = gameOfLifeModel.getBoard();
		updateGameField();
		updateBarChart();

		prepareBoardGrid();
		handleTextProperties();
		handleDisabilityProperties();
		secondsSlider.valueProperty().addListener( (observable, oldValue, newValue) -> {
			if( !secondsSlider.valueChangingProperty().get() )
			{
				restartExecutorService( newValue.longValue() );
			}
		} );

		showInitialBoard();
	}

	private void restartExecutorService(long timeInterval)
	{
		if( Objects.nonNull( future ) )
		{
			showNextInIntervalButton.disableProperty().setValue( true );
			future.cancel( true );
			startScheduledFuture( (int)timeInterval );
		}
	}

	private void handleDisabilityProperties()
	{
		showNextInIntervalButton.disableProperty().addListener( (observable, oldValue, newValue) -> {
			stopGenrationButton.disableProperty().setValue( oldValue );
		} );

		stopGenrationButton.disableProperty().setValue( true );
	}

	private void handleExecuterService()
	{
		scheduledExecutorService = Executors.newScheduledThreadPool( 1 );

		nextGenerationTask = () -> {
			System.out.println( "scheduledExecutorService is running..." );
			showNextGenerationInFxAppThread();
		};

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

			@Override protected String computeValue()
			{
				return String.valueOf( gameOfLifeModel.getYearsGone() );
			}
		} );

		aliveCellsCountLabel.textProperty().bind( new StringBinding()
		{
			{
				bind( gameOfLifeModel.yearsGoneProperty() );
			}

			@Override protected String computeValue()
			{
				return String.valueOf( gameField.getNumberOfAliveCells( board ) );
			}
		} );

		inSecondsLabel.textProperty().bind( new StringBinding()
		{
			{
				bind( secondsSlider.valueProperty() );
			}

			@Override protected String computeValue()
			{
				int seconds = (int)secondsSlider.getValue();
				String template = "in " + seconds + " secs automatically";
				return template;
			}
		} );

	}

	private void updateBarChart()
	{

		XYChart.Series series = new XYChart.Series();
		String yearsGoneAsString = String.valueOf( gameOfLifeModel.getYearsGone() );
		series.setName( yearsGoneAsString );
		series.getData().add( new XYChart.Data( yearsGoneAsString, gameField.getNumberOfAliveCells( board ) ) );

		aliveCellsBarChart.getData().add( series );
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
		for( int i = 0; i < board.length; i++ ) // rows number of board
		{
			boardGrid.getColumnConstraints().add( columnConstraints );
			boardGrid.getRowConstraints().add( rowConstraints );
			for( int j = 0; j < this.board[0].length; j++ ) // columns number of board
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
		else //if( isAlive == 0 )
			label.setBackground( new Background( new BackgroundFill( Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY ) ) );

		label.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
		return label;
	}

	@FXML void nextGenerationButtonPressed(ActionEvent event)
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

		updateBarChart();
		showBoard( board );
	}

	private void cleanBoardGrid()
	{
		boardGrid.getChildren().clear();
		boardGrid.getRowConstraints().clear();
		boardGrid.getColumnConstraints().clear();
	}

	@FXML void stopGenrationButtonPressed(ActionEvent actionEvent)
	{
		System.out.println( "scheduledExecutorService is cancelled" );
		future.cancel( true );
		showNextInIntervalButton.disableProperty().setValue( false );
	}

	@FXML void showNextGenerationInIntervalButtonPressed(ActionEvent actionEvent)
	{
		showNextInIntervalButton.disableProperty().setValue( true );
		int interval = (int)secondsSlider.getValue();
		startScheduledFuture( interval );
	}

	private void startScheduledFuture(int interval)
	{
		System.out.println( "Time interval for scheduled future is set :" + interval + " secs" );
		future = scheduledExecutorService.scheduleAtFixedRate( nextGenerationTask, interval, interval, TimeUnit.SECONDS );
	}

	@FXML void resetFieldButtonPressed(ActionEvent actionEvent)
	{
		cleanBoardGrid();
		gameOfLifeModel.yearsGoneProperty().set( 0 );
		aliveCellsBarChart.getData().clear();

		board = gameOfLifeModel.getBoard();
		updateGameField();
		showInitialBoard();
	}
}

