package de.codermaz.coderetreat.gameoflife.guifx;

import de.codermaz.coderetreat.gameoflife.gamelogic.Samples;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Deque;


public class GameOfLifeModel
{
	private SimpleObjectProperty<int[][]> gameBoard    = new SimpleObjectProperty<>( null );
	private SimpleObjectProperty<Scene>   returnScene  = new SimpleObjectProperty<>( null );
	private SimpleObjectProperty<Stage>   primaryStage = new SimpleObjectProperty<>( null );
	private SimpleIntegerProperty         yearsGone    = new SimpleIntegerProperty( 0 );
	private SimpleIntegerProperty         aliveCells   = new SimpleIntegerProperty( 0 );

	private Menu fileMenu;
	private Menu openRecentMenu;
	private int  INDEX_OF_OPEN_RECENT_UNDER_FILE_MENU = 2;

	@PostConstruct
	public void init()
	{
		//		this.board = Samples.BOARD_9x9;
		//		this.board = Samples.INFINITY_BOARD_9x9;
		this.gameBoard.set( Samples.A_INFINITY_BOARD_9x9 );
		//		this.board = Samples.PLUS_INFINITY_BOARD_9x9;
		//		this.board = Samples.EMPTY_BOARD_25x25;
	}

	public SimpleObjectProperty<int[][]> gameBoardProperty()
	{
		return gameBoard;
	}

	public SimpleObjectProperty<Stage> primaryStageProperty()
	{
		return primaryStage;
	}

	public SimpleObjectProperty<Scene> returnSceneProperty()
	{
		return returnScene;
	}

	public int getYearsGone()
	{
		return yearsGone.get();
	}

	public SimpleIntegerProperty yearsGoneProperty()
	{
		return yearsGone;
	}

	public void yearsGonePlusByOne()
	{
		this.yearsGone.set( yearsGone.get() + 1 );
	}

	public SimpleIntegerProperty aliveCellsProperty()
	{
		return aliveCells;
	}

	public Menu getFileMenu()
	{
		return fileMenu;
	}

	public void setFileMenu(Menu fileMenu)
	{
		this.fileMenu = fileMenu;
	}

	public Menu getOpenRecentMenu()
	{
		return openRecentMenu;
	}

	public void setOpenRecentMenu(Menu openRecentMenu)
	{
		this.openRecentMenu = openRecentMenu;
	}

	public void prepareSubMenu(Deque<File> recentOpenedFiles)
	{
		if( recentOpenedFiles.isEmpty() )
		{
			openRecentMenu.getItems().clear();
			return;
		}
		updateOpenRecentMenu( recentOpenedFiles );
	}

	public void updateOpenRecentMenu(Deque<File> recentOpenedFiles)
	{
		if( openRecentMenu != null && !openRecentMenu.getItems().isEmpty() )
			openRecentMenu.getItems().clear();

		fileMenu.getItems().remove( INDEX_OF_OPEN_RECENT_UNDER_FILE_MENU );
		for(File file : recentOpenedFiles)
		{
			MenuItem menuItem = new MenuItem( file.getAbsolutePath() );
			openRecentMenu.getItems().add( menuItem );
		}

		fileMenu.getItems().add( INDEX_OF_OPEN_RECENT_UNDER_FILE_MENU, openRecentMenu );
	}

	public void runInFXThread(Runnable runnable)
	{
		if( Platform.isFxApplicationThread() )
			runnable.run();
		else
			Platform.runLater( () -> runnable.run() );
	}

}