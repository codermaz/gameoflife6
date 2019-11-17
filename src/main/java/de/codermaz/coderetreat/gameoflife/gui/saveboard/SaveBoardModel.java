package de.codermaz.coderetreat.gameoflife.gui.saveboard;

import javafx.beans.property.SimpleObjectProperty;
import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Deque;
import java.util.LinkedList;


public class SaveBoardModel
{
	private final Deque<File>                recentOpenedFiles      = new LinkedList<>();
	private       SimpleObjectProperty<File> lastChosenFile         = new SimpleObjectProperty();
	private       int                        MAX_RECENT_FILES_COUNT = 10;

	@PostConstruct
	public void init()
	{
		this.lastChosenFile.set( new File( System.getProperty( "user.dir" ) ) );
	}

	public SimpleObjectProperty<File> lastChosenFileProperty()
	{
		return lastChosenFile;
	}

	public void addToRecentFiles(File file)
	{
		removeFileFromRecentOpened( file );
		recentOpenedFiles.addFirst( file );
		if( recentOpenedFiles.size() > MAX_RECENT_FILES_COUNT )
		{
			recentOpenedFiles.removeLast();
		}
	}

	public void removeFileFromRecentOpened(File file)
	{
		recentOpenedFiles.removeIf( addedFile -> addedFile.equals( file ) );
	}

	public void clearRecentFiles()
	{
		recentOpenedFiles.clear();
	}

	public Deque<File> getRecentOpenedFiles()
	{
		return recentOpenedFiles;
	}
}
