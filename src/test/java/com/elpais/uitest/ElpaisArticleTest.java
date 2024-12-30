package com.elpais.uitest;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ElpaisArticleTest extends BaseTest {
	
    @Test
    public void elpaisArticleTest() throws Exception {
    	  String allCombinedTranslatedText ="";
    	  String url = "https://elpais.com/";
    	  int numberOfArticle = 5; 

          // Open the URL and maximize the browser window
          getDriver().get(url);
          getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
          getDriver().manage().window().maximize();

          // Wait for the cookie popup to load and accept it
          WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
          wait.until(ExpectedConditions.elementToBeClickable(By.id("didomi-notice-agree-button"))).click();
          
          String spanishText = getDriver().findElement(By.xpath("//li[@id='edition_head'][1]//span")).getText();
          boolean verifyLanguage = translateText(spanishText).contains("Spain");
          Assert.assertTrue(verifyLanguage);

          // Navigate to the "Opinión" section
          wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='csw']//a[contains(text(),'Opinión')]"))).click();

          // Fetch the first 5 articles
          List<WebElement> titles = getDriver().findElements(By.xpath("//div//header[contains(@class,'c_h')]//h2/a"));
          List<WebElement> contents = getDriver().findElements(By.xpath("//div//header[contains(@class,'c_h')]//following-sibling::p"));
          List<WebElement> images = getDriver().findElements(By.xpath("  //div//header[contains(@class,'c_h')]//h2/a/ancestor::header/preceding-sibling::figure//img"));      
     

          for (int i = 0; i < numberOfArticle; i++) {
              String title = titles.get(i).getText();
              String content = contents.get(i).getText();
              String imgSrc = images.get(i).getAttribute("src");
      
              // Print Article Title and Content
              System.out.println("Article title at " + (i + 1) + " is: " + title);
              System.out.println("Article content at " + (i + 1) + " is: " + content);
           
              System.out.println("Image Source: " + imgSrc);
              downloadImage(imgSrc, title.replaceAll("[^a-zA-Z0-9]", "") + ".jpg");  // Specify the name and extension you want
              System.out.println("Image downloaded successfully!");
           
              // Translate Title 	
              String translatedTitle = translateText(title);

              System.out.println("Original Content: " + title + " | Translated Content: " + translatedTitle);
           
              translatedTitle = translatedTitle.replace("{\"trans\":\"", "").replace("\"}","");
              
              // Concatenate Translated Text 
              allCombinedTranslatedText =  translatedTitle + " "+ allCombinedTranslatedText;
          }

          //System.out.println("Combined Content " + allCombinedTranslatedText);
  
  
          countWords(allCombinedTranslatedText);
    }
    
    
    // method to translate text via api
    public static String translateText(String text) {
        String apiKey = "18407878d5msh7d392cb33162d10p1ec955jsn842205bf7fe1"; // Securely fetch API key
        String apiUrl = "https://google-translate113.p.rapidapi.com/api/v1/translator/text";
        String apiHost = "google-translate113.p.rapidapi.com";

        try {
            HttpResponse<String> response = Unirest.post(apiUrl)
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .header("Content-Type", "application/json")
                .body(String.format("{\"from\":\"es\",\"to\":\"en\",\"text\":\"%s\"}", text))
                .asString();

            if (response.getStatus() == 200) {
                return response.getBody();
            } else {
                System.err.println("Failed to translate text. Status: " + response.getStatus() + ", Body: " + response.getBody());
                return "Translation failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while translating.";
        }
    }

   
    // method to count words repeated
    private static void countWords(String text)
    
    {
    	 String[] words = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
         
         // Create a map to store the word frequency
         Map<String, Integer> wordCount = new HashMap<>();
         
         // Count the frequency of each word
         for (String word : words) {
             wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
         }
         
         // Check for words that occur more than twice
         System.out.println("Words that occur more than twice:");
         for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
             if (entry.getValue() > 2) {
                 System.out.println(entry.getKey() + ": " + entry.getValue());
             }
         }
     }
    
    
    private static void downloadImage(String imageUrl, String destinationPath) {
        try {
            // Create a URL object from the image source
            URL url = new URL(imageUrl);

            // Open the input stream for the image
            InputStream in = url.openStream();

            // Create the output file path (destination)
            Path destination = Paths.get(destinationPath);

            // Create an output stream to write the image to file
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);

            // Close the input stream
            in.close();

        } catch (IOException e) {
            System.out.println("Error downloading the image: " + e.getMessage());
        }
    }
    

 

}
