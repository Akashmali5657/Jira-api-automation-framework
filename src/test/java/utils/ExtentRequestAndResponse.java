package utils;

import io.restassured.response.Response;

public class ExtentRequestAndResponse {

    public static void logRequestAndResponse(String method, String endpoint, Response response) {

        String log =
                "REQUEST:\n" + method + " " + endpoint +"\n" +
                        "\n\nRESPONSE:\n" + response.asPrettyString();

        ScenarioContext.scenario.log(log);
    }
}
