package de.codermaz.coderetreat;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class GameOfLifeTest
{

	@Before public void setUp() throws Exception
	{

	}

	@After public void tearDown() throws Exception
	{
	}

	@Test public void testNumberOfFieldsOfInitialBoard()
	{

		Assert.assertThat( numberOfFieldsOfInitialBoard(), IsEqual.equalTo( 81 ) );
	}

	private int numberOfFieldsOfInitialBoard()
	{
		return 0;
	}

}