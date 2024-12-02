package com.sandbox.presentation;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GreetingControllerLoadTest extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8010");


    ScenarioBuilder scn = scenario("Greeting API Test")
            .exec(
                    http("Get Greeting")
                            .get("/api/v1/greeting")
                            .check(
                                    status().is(200),

                                    jsonPath("$[*]").count().is(2),

                                    jsonPath("$[*]").findAll().transform(strings ->
                                            strings.contains("Bonjour !") && strings.contains("Hello")
                                    ).is(true)
                            )
            );

    {
        setUp(
                scn.injectOpen(constantUsersPerSec(3).during(10)) // Scenario: 3 users over 10 seconds
        ).protocols(httpProtocol)
                .assertions(
                        forAll().failedRequests().percent().lte(0.1)
                );
    }
}
