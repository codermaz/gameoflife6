package de.codermaz.coderetreat.gameoflife.gui.saveboard;

import com.airhacks.afterburner.views.FXMLView;
import java.util.function.Function;


public class SaveBoardView extends FXMLView
{
	public SaveBoardView()
	{
	}

	public SaveBoardView(Function<String, Object> injectionContext)
	{
		super( injectionContext );
	}
}
