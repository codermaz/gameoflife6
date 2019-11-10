package de.codermaz.coderetreat.gameoflife.model;

import de.codermaz.coderetreat.gameoflife.gamelogic.Samples;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;


public class BoardInfoTest
{
	private String directoryOfTestResources;

	@Before
	public void setUp()
	{
		String baseLocation = "de/codermaz/coderetreat/gameoflife/";
		String testResourceFile = "fileToGetLocationOfTestResources.txt";
		URL resourceOfTestFiles = getClass().getClassLoader().getResource( baseLocation + testResourceFile );
		//		C:\Users\...\git\coderetreat\gameoflife\build\resources\test\de\codermaz\coderetreat\gameoflife\
		directoryOfTestResources = getParent( Objects.requireNonNull( resourceOfTestFiles ).toString() );
	}

	private String getParent(String resourcePath)
	{
		int index = resourcePath.lastIndexOf( '/' );
		if( index > 0 )
		{
			return resourcePath.substring( 0, index );
		}
		return "/";
	}

	@Test
	public void testReadRows()
	{
		String baseLocation = "de/codermaz/coderetreat/gameoflife/";
		String file = "model/boardInfo.xml";
		URL resource = getClass().getClassLoader().getResource( baseLocation + file );
		String fileFullPath = resource.getPath();
		BoardInfo boardInfo = new BoardInfo();
		Optional<BoardInfoXml> boardInfoOptional = boardInfo.jaxbXmlFileToObject( fileFullPath );
		if( boardInfoOptional.isPresent() )
		{
			BoardInfoXml boardInfoXml = boardInfoOptional.get();
			System.out.println( boardInfoXml );
			Assert.assertThat( "Name of board should be A1_board", boardInfoXml.getName(), CoreMatchers.equalTo( "A1_board" ) );
			boolean rowsCountIsCorrect = boardInfoXml.getRowsCount() == boardInfoXml.getRows().size();
			Assert.assertThat( "Rows count of board should be same as defined in file " + fileFullPath, rowsCountIsCorrect,
				CoreMatchers.is( Boolean.TRUE ) );
			int colsCountDefinedInFile = boardInfoXml.getColsCount();
			boolean allRowsHaveRightColumnLength = boardInfoXml.getRows().stream()
				.allMatch( row -> boardInfo.calculateColsCount( row ) == colsCountDefinedInFile );
			Assert.assertThat( "All rows must have defined column length and only (1 0 .) or space in file " + fileFullPath,
				allRowsHaveRightColumnLength, CoreMatchers.is( true ) );
		}
		else
		{
			System.out.println( fileFullPath + " is faulty, couldn't be unmarshalled" );
		}
	}

	@Test
	public void testWriteRows()
	{
		String fullPathOfXmlFile = preparePathForTestFile();
		BoardInfo boardInfo = new BoardInfo();

		if( boardInfo.prepareFileForMarshalling( fullPathOfXmlFile ) )
		{
			System.out.println( fullPathOfXmlFile + " could not prepared for marshalling!" );
			return;
		}

		BoardInfoXml boardInfoXml = boardInfo.gameBoardToBoardInfoXml( Samples.A_INFINITY_BOARD_9x9 );
		boardInfoXml.setName( "A_INFINITY_BOARD_9x9" );
		if( !boardInfo.jaxbObjectToXML( boardInfoXml, fullPathOfXmlFile ) )
		{
			System.out.println( "Board info could not be written to " + fullPathOfXmlFile );
			return;
		}

		Optional<BoardInfoXml> boardInfoOptional = boardInfo.jaxbXmlFileToObject( fullPathOfXmlFile );
		if( boardInfoOptional.isPresent() )
		{
			boardInfoXml = boardInfoOptional.get();
			System.out.println( boardInfoXml );
			Assert.assertThat( "Name of board should be A_INFINITY_BOARD_9x9", boardInfoXml.getName(),
				CoreMatchers.equalTo( "A_INFINITY_BOARD_9x9" ) );
			String actual = String.valueOf( boardInfoXml.getRows().get( 0 ).charAt( 0 ) );
			Assert.assertThat( "Cell [0][0] of board should be 0", actual, CoreMatchers.equalTo( "0" ) );
		}
	}

	public String preparePathForTestFile()
	{
		String fileName = "/model/boardInfo2.xml";
		String fileFullPath = directoryOfTestResources + fileName;
		int index = fileFullPath.indexOf( "file:/" );
		return fileFullPath.substring( index + 6 );
	}

}