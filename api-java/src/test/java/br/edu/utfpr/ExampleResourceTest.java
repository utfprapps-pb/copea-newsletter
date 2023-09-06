package br.edu.utfpr;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testHelloEndpoint_Without_Authorization() {
        RestAssured.given()
                .when().get("/api/hello")
                .then()
                .statusCode(401);
    }

}
