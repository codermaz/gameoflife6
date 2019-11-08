package de.codermaz.coderetreat.gameoflife.model;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import java.util.Optional;


public class BoardInfoTest
{

	@Test
	public void testReadRows()
	{
		String fileName = "boardInfo.xml";
		BoardInfo boardInfo = new BoardInfo( fileName );
		Optional<BoardInfoXml> boardInfoOptional = boardInfo.jaxbXmlFileToObject();
		if( boardInfoOptional.isPresent() )
		{
			BoardInfoXml boardInfoXml = boardInfoOptional.get();
			System.out.println( boardInfoXml );
			Assert.assertThat( "Name of board should be A1_board", boardInfoXml.getName(), CoreMatchers.equalTo( "A1_board" ) );
			boolean rowsCountIsCorrect = boardInfoXml.getRowsCount() == boardInfoXml.getRows().size();
			Assert.assertThat( "Rows count of board should be same as defined in file " + fileName, rowsCountIsCorrect,
				CoreMatchers.is( Boolean.TRUE ) );
			int colsCountDefinedInFile = boardInfoXml.getColsCount();
			boolean allRowsHaveRightColumnLength = boardInfoXml.getRows().stream()
				.allMatch( row -> boardInfo.calculateColsCount( row ) == colsCountDefinedInFile );
			Assert.assertThat( "All rows must have defined column length and only (1 0 .) or space in file " + fileName,
				allRowsHaveRightColumnLength, CoreMatchers.is( true ) );
		}
		else
		{
			System.out.println( fileName + " is faulty, couldn't be unmarshalled" );
		}
	}

}