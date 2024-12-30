# testng-elpais-uitest

[TestNG](http://testng.org) Integration with BrowserStack.

## The repository performs below steps using testng maven framework
- Visit El País Website: Ensure the site displays text in Spanish.
- Scrape Articles: Navigate to the Opinion section, scrape the first five articles, and print their titles and content in Spanish.
- Download Cover Images: If available, download and save each article's cover image locally.
- Translate Titles: Use a translation API (e.g., Google Translate API) to translate the article titles from Spanish to English.
- Analyze Translated Titles: Identify and count repeated words in the translated titles.

## Using Maven


### Run Test on Local Browser 
- Clone the repository
- Install dependencies `mvn compile`
- To run the test suite having cross-browser with parallelization, run `set BROWSERSTACK_AUTOMATION=false mvn clean test -P local-machine` ( the browsers are configured via testng.xml)
- The suite will run test in parallel with 3 threads 

### Run Test on Browserstack

- Clone the repository
- Replace YOUR_USERNAME and YOUR_ACCESS_KEY with your BrowserStack access credentials in browserstack.yml.
- Install dependencies `mvn compile`
- To run the test suite having cross-platform with parallelization, run `mvn clean test -P browserstack`
- You can configure capabilities for browserstack in browserstack.yml


## View Browserstack Results
* You can view your test results on the [BrowserStack Automate dashboard](https://www.browserstack.com/automate)
