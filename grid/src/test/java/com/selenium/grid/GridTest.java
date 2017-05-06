package com.selenium.grid;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class GridTest {
	final static Logger logger = Logger.getLogger(GridTest.class);

	private static WebDriver driver;
	private static String browserType;
	private static DesiredCapabilities cap;

	@BeforeMethod
	@Parameters({ "hubURL", "browserName" })
	public void setUp(String hubURL, String browserName) {
		cap = new DesiredCapabilities();
		try {

			if (browserName.contains("chrome")) {
				cap.setBrowserName("chrome");
				cap.setPlatform(Platform.WINDOWS);
				browserType = browserName;
				System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
				/*
				 * ChromeOptions options = new ChromeOptions();
				 * options.addArguments("--test-type");
				 * cap.setCapability(ChromeOptions.CAPABILITY, options);
				 */

			} else if (browserName.contains("ie")) {
				cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				cap.setBrowserName("internet explorer");
				cap.setPlatform(Platform.WIN10);
				browserType = browserName;
			} else if (browserName.contains("firefox")) {
				cap.setBrowserName("firefox");
				System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
				// cap.setPlatform(Platform.WINDOWS);
				browserType = browserName;
			}

			driver = new RemoteWebDriver(new URL(hubURL), cap);
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			logger.info("starting browser: '" + browserName + "'");
		} catch (Exception e) {
			logger.error("Starting remote browser failed for '" + browserName + "'", e);
		}
	}

	@AfterMethod
	public void tearDown() throws Exception {
		Thread.sleep(3 * 1000);
		driver.close();
		driver.quit();
	}

	@Test
	public void test_01() {
		try {

			driver.get("https://www.facebook.com/");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String title = driver.getTitle();
		System.out.println("test is ending on browser " + browserType + " on " + cap.getPlatform());
		logger.info("test is ending on browser " + browserType);
		Assert.assertEquals(title, "Facebook - Log In or Sign Up");

	}

	@Test
	public void test_02() {
		driver.get("https://www.google.com/");
		String title = driver.getTitle();
		System.out.println("test is ending on browser " + browserType + " on " + cap.getPlatform());
		logger.info("test is ending on browser " + browserType + " on " + cap.getPlatform());
		Assert.assertEquals(title, "Google");
	}

}
