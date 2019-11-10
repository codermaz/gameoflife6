package de.codermaz.coderetreat.gameoflife.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "boardInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoardInfoXml
{
	private String name;
	private int    rowsCount;
	private int    colsCount;

	@XmlElementWrapper(name = "rows")
	@XmlElement(name = "row")
	private List<String> rows;

	public String getName()
	{
		return name;
	}

	public int getRowsCount()
	{
		return rowsCount;
	}

	public int getColsCount()
	{
		return colsCount;
	}

	public List<String> getRows()
	{
		return rows;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setRowsCount(int rowsCount)
	{
		this.rowsCount = rowsCount;
	}

	public void setColsCount(int colsCount)
	{
		this.colsCount = colsCount;
	}

	public void setRows(List<String> rows)
	{
		this.rows = rows;
	}

	@Override
	public String toString()
	{
		String boardInfo = "BoardInfo{" + "name='" + name + '\'' + ", rowsCount=" + rowsCount + ", colsCount=" + colsCount + ", rows: \n\t";
		String rowsSeparated = String.join( "\n\t", rows );
		return boardInfo + rowsSeparated;
	}
}
