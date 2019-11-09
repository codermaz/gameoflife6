package de.codermaz.coderetreat.gameoflife.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;


public class BoardInfo
{
	public static String DEAD_COL_CHARS  = "0.";
	public static String ALIVE_COL_CHARS = "1+";
	private final String filename;

	public BoardInfo(String filename)
	{
		this.filename = filename;
	}

	public Optional<BoardInfoXml> jaxbXmlFileToObject()
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance( BoardInfoXml.class );
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			InputStream fileResource = new FileInputStream( filename );
			BoardInfoXml boardInfoXml = (BoardInfoXml)jaxbUnmarshaller.unmarshal( fileResource );
			return Optional.of( boardInfoXml );
		}
		catch(JAXBException | FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public int calculateColsCount(String row)
	{
		int numbers = 0;
		for(int i = 0; i < row.length(); i++)
		{
			char c = row.charAt( i );
			boolean isValidColChar = DEAD_COL_CHARS.contains( String.valueOf( c ) ) || ALIVE_COL_CHARS.contains( String.valueOf( c ) );
			if( isValidColChar )
			{
				numbers++;
			}
			else if( c != ' ' )
			{
				return -1;
			}
		}
		return numbers;
	}

	public String getFilename()
	{
		return filename;
	}
}
