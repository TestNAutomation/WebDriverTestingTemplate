package qa.webdriver.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;

import au.com.bytecode.opencsv.CSVReader;

import qa.webdriver.util.GoogleSearchPage;
import qa.webdriver.util.GoogleUtilities;

@RunWith(Parameterized.class)
public class GoogleTest2 extends GoogleUtilities {

	private static String testName, searchString, ddMatch;

	public GoogleTest2( String tName, String sString, String dMatch ) {
		testName = tName;
		searchString = sString;
		ddMatch = dMatch;
		testXOffset = 700;
	}

	@Before
	public void setUp() {	
		if ( driver == null ) initializeRemoteBrowser( "firefox", "localhost", 4444 );
		staticlogger.info("Finished setUpGoogleTest2");
	}

	@Parameters(name = "{0}: {1}: {2}")
	public static Iterable<String[]> loadTestsFromFile2() {
		File tFile = loadGradleResource( System.getProperty("user.dir") + separator +  "build" +
				separator + "resources" + separator +  "test" + separator + "testdata2.csv" );
		List<String[]> rows = null;
		if ( tFile.exists() ) {
			CSVReader reader = null;
			try {
				reader = new CSVReader( new FileReader( tFile ), ',' );
				rows = reader.readAll();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		staticlogger.info("Finished loadTestsFromFile2()");
		return rows;
	}  

	@Test
	public void testWithPageObject() {
		staticlogger.info("{} being run...", testName );
		driver.get("http://www.google.com");
		GoogleSearchPage gs = new GoogleSearchPage();
		gs.setSearchString( searchString );
		selectInGoogleDropdown( ddMatch );  
		gs.clickSearchButton();
		waitTimer(2, 1000);
		getElementByLocator( By.cssSelector( "div.gbqlca" ) ).click(); // click Google logo
		staticlogger.info("Page object test '{}' is done.", testName );
	}

	@Test
	public void testFluentPageObject() {    	
		staticlogger.info("{} being run...", testName );
		driver.get("https://www.google.com/webhp?hl=en&tab=ww");
		GoogleSearchPage gsp = new GoogleSearchPage();
		gsp.withFluent().clickSearchField()
		.setSearchString( searchString ).waitForTime(2, 1000)
		.selectItem( ddMatch ).clickSearchButton()
		.waitForTime(2, 1000).clickLogo(); //click Google logo
		staticlogger.info("Fluent test '{}' is done.", testName );
	}

	@After
	public void cleanUp() {
		staticlogger.info("Finished cleanUpGoogleTest2");
		driver.get("about:about");
		// remaining open browser window will garbage collect within 30 seconds
	}

}