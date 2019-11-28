package de.codermaz.coderetreat.gameoflife.guifx.configuration;

import javax.inject.Inject;


public class ConfigurationModel
{
	@Inject
	String startBoardXmlFile;
	@Inject
	String watchDir;

	public String getStartBoardXml()
	{
		return startBoardXmlFile;
	}

	public String getWatchDir()
	{
		return watchDir;
	}
}
