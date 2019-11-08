package de.codermaz.coderetreat.gameoflife.gui;

import de.codermaz.coderetreat.gameoflife.gamelogic.Samples;
import javafx.beans.property.SimpleIntegerProperty;
import javax.annotation.PostConstruct;


public class GameOfLifeModel
{
	private int[][]               board;
	private SimpleIntegerProperty yearsGone  = new SimpleIntegerProperty( 0 );
	private SimpleIntegerProperty aliveCells = new SimpleIntegerProperty( 0 );

	@PostConstruct
	public void init()
	{
		//		this.board = Samples.BOARD_9x9;
		//		this.board = Samples.INFINITY_BOARD_9x9;
		this.board = Samples.A_INFINITY_BOARD_9x9;
		//		this.board = Samples.PLUS_INFINITY_BOARD_9x9;
		//		this.board = Samples.EMPTY_BOARD_25x25;
	}

	public int[][] getBoard()
	{
		return board;
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

}
