package de.codermaz.coderetreat.gameoflife.services.watchdir;

import java.nio.file.Path;


public class BoardFileTO
{
	private Path   absoluteDir;
	private Path   fileName;
	private String relativeDir;

	public Path getAbsoluteDir()
	{
		return absoluteDir;
	}

	public void setAbsoluteDir(Path absoluteDir)
	{
		this.absoluteDir = absoluteDir;
	}

	public Path getFileName()
	{
		return fileName;
	}

	public void setFileName(Path fileName)
	{
		this.fileName = fileName;
	}

	public String getRelativeDir()
	{
		return relativeDir;
	}

	public void setRelativeDir(String relativeDir)
	{
		this.relativeDir = relativeDir;
	}
}


