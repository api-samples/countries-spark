package io.mikael.countries;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static spark.Spark.get;

public class SparkCountries {

    private final static Map<String, Country> COUNTRIES;

    private final static ObjectMapper JSON;

    static {
        JSON = new ObjectMapper();
        JSON.enable(SerializationFeature.INDENT_OUTPUT);
        try (final InputStream is = SparkCountries.class.getClassLoader().getResourceAsStream("countries.json")) {
            final List<Country> input = JSON.readValue(is, new TypeReference<List<Country>>() { });
            COUNTRIES = input.stream().collect(
                    toMap(c -> c.cca2, Function.identity(), (a, b) -> a));
        } catch (final IOException e) {
            throw new RuntimeException("unable to parse countries", e);
        }
    }

    public static void main(final String ... args) throws Exception {
        get("/countries/:cca2", (req, res) -> {
            final String cca2 = req.params(":cca2");
            res.type("text/json");
            if (COUNTRIES.containsKey(cca2)) {
                return COUNTRIES.get(req.params(":cca2"));
            } else {
                res.status(404);
                return Collections.emptyMap();
            }
        }, JSON::writeValueAsString);
    }

}
