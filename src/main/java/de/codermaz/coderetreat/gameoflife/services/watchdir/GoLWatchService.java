package de.codermaz.coderetreat.gameoflife.services.watchdir;

import de.codermaz.coderetreat.gameoflife.guifx.configuration.ConfigurationModel;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;


public class GoLWatchService
{
	@Inject
	private ConfigurationModel configurationModel;

	private WatchService               watchService;
	private HashMap<WatchKey, Path>    keys;
	private SimpleObjectProperty<Path> watchDir = new SimpleObjectProperty<>();

	@PostConstruct
	public void init()
	{
		try
		{
			watchService = FileSystems.getDefault().newWatchService();
			keys = new HashMap<>();
			watchDir.addListener( this::monitorAndFetchXmlFiles );
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void monitorAndFetchXmlFiles(Observable observable)
	{
		keys.clear();
		startMonitoringWithSubDirs();
		processEventsWithinSubDirs();
	}

	/**
	 * Register the given directory with the WatchService; This function will be called by FileVisitor
	 */
	private void registerDirectory(Path dir) throws IOException
	{
		WatchKey key = dir.register( watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY );
		keys.put( key, dir );
	}

	/**
	 * Register the given directory, and all its sub-directories, with the WatchService.
	 */
	private void walkAndRegisterDirectories(final Path start) throws IOException
	{
		// register directory and sub-directories
		Files.walkFileTree( start, new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
			{
				registerDirectory( dir );
				return FileVisitResult.CONTINUE;
			}
		} );
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	private void processEventsWithinSubDirs()
	{
		for(; ; )
		{
			// wait for key to be signalled
			WatchKey key;
			try
			{
				key = watchService.take();
			}
			catch(InterruptedException x)
			{
				System.err.println( "thread is interrupted: " + x.getMessage() );
				return;
			}

			Path dir = keys.get( key );
			if( dir == null )
			{
				System.err.println( "WatchKey not recognized!!" );
				continue;
			}

			for(WatchEvent<?> event : key.pollEvents())
			{
				@SuppressWarnings("rawtypes")
				WatchEvent.Kind kind = event.kind();

				// Context for directory entry event is the file name of entry
				@SuppressWarnings("unchecked")
				Path name = ((WatchEvent<Path>)event).context();
				Path child = dir.resolve( name );

				// print out event
				// System.out.format( "%s: %s\n", event.kind().name(), child );

				// if directory is created, and watching recursively, then register it and its sub-directories
				if( kind == ENTRY_CREATE )
				{
					try
					{
						if( Files.isDirectory( child ) )
						{
							walkAndRegisterDirectories( child );
						}
						else
						{
							// System.out.println( "watchDir.toString() = " + watchDir.toString() ); // C:\Temp\GoL\boards\col\watch
							// System.out.println(
							//   "child.getParent().toString() = " + child.getParent().toString() ); // C:\Temp\GoL\boards\col\watch\a1\a11
							//	System.out.println( "name.toString() = " + name.toString() ); // a - Kopie (4).xml
							if( child.getParent().toString().length() != watchDir.get().toString().length() )
							{
								String relativeDir = child.getParent().toString().substring( watchDir.get().toString().length() + 1 );
								System.out.println( "relativeDir = " + relativeDir ); // a1\a11
							}
							System.out.println( child.getParent().resolve( name ) );
						}
					}
					catch(IOException x)
					{
						// do something useful
					}
				}
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if( !valid )
			{
				keys.remove( key );

				// all directories are inaccessible
				if( keys.isEmpty() )
				{
					break;
				}
			}
		}
	}

	public boolean startMonitoringWithSubDirs()
	{
		try
		{
			walkAndRegisterDirectories( watchDir.get() );
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public void startMonitoringOnlyRootDir()
	{
		try
		{
			registerDirectory( watchDir.get() );
			processEventsOnlyForRootDir();
		}
		catch(InterruptedException | IOException e)
		{
			e.printStackTrace();
		}
	}

	private void processEventsOnlyForRootDir() throws InterruptedException
	{
		WatchKey key;
		while((key = watchService.take()) != null)
		{
			for(WatchEvent<?> event : key.pollEvents())
			{
				System.out.println( "Event kind:" + event.kind() + ". File affected: " + event.context() + "." );
			}
			key.reset();
		}
	}

	public SimpleObjectProperty<Path> watchDirProperty()
	{
		return watchDir;
	}
}
