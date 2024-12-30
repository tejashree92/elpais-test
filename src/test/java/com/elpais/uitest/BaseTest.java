package com.elpais.uitest;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BaseTest {
	 private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

  
    @BeforeMethod
    @Parameters({"browser"})
    public void setup(String browser) throws MalformedURLException {
        if (browser.equalsIgnoreCase("chrome")) {
            driver.set(new ChromeDriver());
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver.set(new FirefoxDriver());
        } else if (browser.equalsIgnoreCase("edge")) {
            driver.set(new EdgeDriver());
        } else {
        	//  driver.set(new ChromeDriver());
        }
    }

    public WebDriver getDriver() {
        return driver.get();
    }
    
    
    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
    	driver.get().quit();
    }
}
