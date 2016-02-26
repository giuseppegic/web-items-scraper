# web-items-scraper

=== Dependencies/Prerequisites ===
- Java 7+
- Maven 3+ (build time only)

=== Run tests and build ===
- From the root directory of the project (from now on $project) run the following command: mvn clean install.
  This will output a test results report.
  The built artifact (web-items-scraper-0.0.1.jar)can be found in the build directory ($project/target).
  
- Tests can also be run within an IDE. E.g. in Eclipse to run all tests.
  Right click on the "src/test/java" folder and select "Run as" -> "JUnit test"

=== Run the application ===
- Once the application as been built (see previous step) it can be launched from its build directory.

- The command to run is the following: 
  java -jar web-items-scraper-0.0.1.jar $URL_WITH_ITEM_LIST [$COMPACT]

- Following is a description of the possible properties.
  $URL_WITH_ITEM_LIST: Mandatory parameter indicating the url to the list of items. 
  e.g.   http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html

  $COMPACT: Optional parameter settable to 'false' to generate compact json. 
            By default the generated JSON will be indented.

- Running the app from an IDE
  Alternatively the application can be launched from within an IDE, respecting the before said usage.
  The main method is contained in the uk.co.gg.web.scrapers.shopping.application.ShoppingWebScraperApp class.
  E.g. in Eclipse run the main method after customizing the appropriate run configuration with the described parameters.