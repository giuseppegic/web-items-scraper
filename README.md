# web-items-scraper

=== Dependencies/Prerequisites ===
Java 7+, Maven 3+

=== Run tests and build ===
From the root directory of the project (from now on $project) run the following command: mvn clean install.
This will output a test results report and create a jar (web-items-scraper-0.0.1.jar) in the build directory ($project/target).
Tests can also be run within an IDE. E.g. in Eclipse to run all tests, right click on the  "src/test/resources" folder and select "Run as" -> "JUnit test"

=== Run the application ===
Once the application as been built (see previous step) it can be run from the build directory.
The command to run is the following: 
java -jar web-items-scraper-0.0.1.jar $URL_WITH_ITEM_LIST [$COMPACT]

$URL_WITH_ITEM_LIST: the url to the list of items. e.g. http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html

$COMPACT: By default the generated JSON will be indented. Set this optional parameter to false to generate compact json.

Alternatively the application can be launched from within an IDE, respecting the before said usage.
E.g. in Eclipse it is possible to run the app by running the main method for uk.co.gg.web.scrapers.shopping.application.ShoppingWebScraperApp and adding in the run configuration the required $URL_WITH_ITEM_LIST parameter and optionally the $COMPACT parameter.