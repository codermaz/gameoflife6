package de.codermaz.coderetreat.gameoflife.gui;

import de.codermaz.coderetreat.gameoflife.gamelogic.Samples;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.annotation.PostConstruct;
import java.io.File;


public class GameOfLifeModel
{
	private SimpleIntegerProperty         yearsGone       = new SimpleIntegerProperty( 0 );
	private SimpleIntegerProperty         aliveCells      = new SimpleIntegerProperty( 0 );
	private SimpleObjectProperty<File>    lastChoosenFile = new SimpleObjectProperty();
	private SimpleObjectProperty<int[][]> gameBoard       = new SimpleObjectProperty<>( null );

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

	public SimpleObjectProperty<File> lastChoosenFileProperty()
	{
		return lastChoosenFile;
	}

}
