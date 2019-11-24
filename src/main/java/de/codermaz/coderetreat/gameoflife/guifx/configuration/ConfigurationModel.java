package de.codermaz.coderetreat.gameoflife.guifx.configuration;

import javax.inject.Inject;


public class ConfigurationModel
{
	@Inject
	String startBoardXmlFile;

	public String getStartBoardXml()
	{
		return startBoardXmlFile;
	}
}
