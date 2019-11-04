package de.codermaz.coderetreat.gameoflife.gui;

import de.codermaz.coderetreat.gameoflife.gamelogic.Samples;
import javafx.beans.property.SimpleIntegerProperty;

import javax.annotation.PostConstruct;


public class GameOfLifeModel
{
	private int[][]               board;
	private SimpleIntegerProperty yearsGone = new SimpleIntegerProperty( 0 );

	@PostConstruct public void init()
	{
		System.out.println( "GameOfLifeModel initialized..." );
		this.board = Samples.BOARD_9x9;
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
}
