package de.codermaz.coderetreat.gameoflife.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BoardInfo
{
	public static String DEAD_CELL_CHARS  = "0.";
	public static String ALIVE_CELL_CHARS = "1+";

	public BoardInfo()
	{

	}

	public Optional<BoardInfoXml> jaxbXmlFileToObject(String filename)
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance( BoardInfoXml.class );
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			InputStream fileResource = new FileInputStream( filename );
			BoardInfoXml boardInfoXml = (BoardInfoXml)jaxbUnmarshaller.unmarshal( fileResource );
			boardInfoXml.setName( boardInfoXml.getName().trim() );

			return Optional.of( boardInfoXml );
		}
		catch(JAXBException | FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public boolean jaxbObjectToXML(BoardInfoXml boardInfoXml, String fileName)
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance( BoardInfoXml.class );
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); // To format XML

			OutputStream os = new FileOutputStream( fileName );
			jaxbMarshaller.marshal( boardInfoXml, os );
			//			jaxbMarshaller.marshal( boardInfoXml, System.out );
			return true;
		}
		catch(JAXBException | FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public int calculateColsCount(String row)
	{
		int numbers = 0;
		for(int i = 0; i < row.length(); i++)
		{
			char c = row.charAt( i );
			boolean isValidColChar = DEAD_CELL_CHARS.contains( String.valueOf( c ) ) || ALIVE_CELL_CHARS.contains( String.valueOf( c ) );
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

	public int[][] transferBoardXmlToGameBoard(BoardInfoXml boardInfoXml)
	{
		int rowsCount = boardInfoXml.getRowsCount();
		int colsCount = boardInfoXml.getColsCount();
		int[][] newBoard = new int[rowsCount][colsCount];
		List<String> rows = boardInfoXml.getRows();

		for(int i = 0; i < rowsCount; i++)
		{
			String row = rows.get( i );
			for(int j = 0, k = 0; k < row.length(); k++)
			{
				boolean isCharInDeadColChars = DEAD_CELL_CHARS.contains( String.valueOf( row.charAt( k ) ) );
				boolean isCharInLiveColChars = ALIVE_CELL_CHARS.contains( String.valueOf( row.charAt( k ) ) );

				if( isCharInLiveColChars )
				{
					newBoard[i][j] = 1;
					j++;
				}
				else
				{
					if( isCharInDeadColChars )
					{
						newBoard[i][j] = 0;
						j++;
					}
				}
			}
		}
		return newBoard;
	}

	public BoardInfoXml gameBoardToBoardInfoXml(int[][] gameBoard)
	{
		BoardInfoXml boardInfoXml = new BoardInfoXml();

		int rowsCount = gameBoard.length;
		boardInfoXml.setRowsCount( rowsCount );
		int colsCount = gameBoard[0].length;
		boardInfoXml.setColsCount( colsCount );

		List<String> rows = new ArrayList<>();
		for(int[] ints : gameBoard)
		{
			StringBuilder sb = new StringBuilder();
			for(int j = 0; j < colsCount; j++)
			{
				sb.append( ints[j] ).append( " " );
			}
			rows.add( sb.toString().trim() );
		}
		boardInfoXml.setRows( rows );
		return boardInfoXml;
	}

	public boolean prepareFileForMarshalling(String pathWithoutFilePrefix)
	{
		File file = new File( pathWithoutFilePrefix );

		if( !file.getParentFile().exists() )
		{
			if( !file.getParentFile().mkdirs() )
			{
				System.out.println( "Directory for " + pathWithoutFilePrefix + " could not created!" );
				return true;
			}
		}
		try
		{
			if( !file.exists() )
			{
				if( !file.createNewFile() )
				{
					System.out.println( "File " + pathWithoutFilePrefix + " could not created!" );
					return true;
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return true;
		}
		return false;
	}

}
