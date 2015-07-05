package io.mikael.countries;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

public class CountriesTest {

    @AfterClass
    public static void tearDown() {
        Spark.stop();
        RestAssured.unregisterParser("text/json");
    }

    @BeforeClass
    public static void setup() throws Exception {
        SparkCountries.main();
        RestAssured.registerParser("text/json", Parser.JSON);
    }

    @Before
    public void setUp() {
        RestAssured.port = Spark.SPARK_DEFAULT_PORT;
    }

    @Test
    public void finland() {
        when().
                get("/countries/{cca2}", "FI").
        then().
                statusCode(HttpStatus.SC_OK).
                body("name.common", is("Finland")).
                body("region", is("Europe"));
    }

    @Test
    public void sweden() {
        when().
                get("/countries/{cca2}", "SE").
        then().
                statusCode(HttpStatus.SC_OK).
                body("name.common", is("Sweden")).
                body("region", is("Europe"));
    }

}
