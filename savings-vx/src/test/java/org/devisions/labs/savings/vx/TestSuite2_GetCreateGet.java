package org.devisions.labs.savings.vx;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.devisions.labs.savings.vx.config.MainConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("🚀 SavingsAccount get, create, get again.")
@ExtendWith(VertxExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestSuite2_GetCreateGet {

    /** URI for testing with ownerId=0. */
    private static final String uri = "/savings/byowner/0";

    private static final String accountName = "TestSuite2_Account";

    private static final String accountDesc = "Temporary testing account while running TestSuite2";

    private static final Logger logger = LoggerFactory.getLogger(TestSuite2_GetCreateGet.class);

    @BeforeEach
    void deployVerticle(Vertx vertx, VertxTestContext testContext) {
        MainConfig.getInstance().enableTestingMode();
        vertx.deployVerticle(new StartVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    @Order(1)
    @DisplayName("1. No account for unknown user")
    void noAccountExists(Vertx vertx, VertxTestContext testContext) throws Throwable {

        WebClient client = WebClient.create(vertx);
        client.get(8888, "localhost", uri)
            .send(testContext.succeeding(resp -> {
                testContext.verify(() -> assertThat(resp.statusCode()).isEqualTo(404))
                    .completeNow();
            }));

    }

    @Test
    @Order(2)
    @DisplayName("2. Create & get account")
    void createAndGetAccount(Vertx vertx, VertxTestContext testContext) {

        WebClient client = WebClient.create(vertx);
        // create the account
        client.post(8888, "localhost", uri)
            .sendJsonObject(
                new JsonObject()
                    .put("name", accountName)
                    .put("description", accountDesc)
                , testContext.succeeding(resp -> {
                    testContext.verify(() -> {
                        assertThat(resp.statusCode()).isEqualTo(201);
                        assertThat(resp.bodyAsJsonObject().getString("name")).isEqualTo(accountName);
                    });
                }));
        // get the account
        client.get(8888, "localhost", uri)
            .send(testContext.succeeding(resp -> {
                testContext.verify(() -> {
                    logger.debug("After creation, got account: {}\n", resp.bodyAsString());
                    assertThat(resp.statusCode()).isEqualTo(200);
                })
                    .completeNow();
            }));

    }

    @Test
    @Order(3)
    @DisplayName("2. Create, get, create again an account")
    void createAndGetAndCreateAgainAccount(Vertx vertx, VertxTestContext testContext) {

        WebClient client = WebClient.create(vertx);
        // create the account, it should end with HTTP status code '201'
        client.post(8888, "localhost", uri)
            .sendJsonObject(
                new JsonObject()
                    .put("name", accountName)
                    .put("description", accountDesc)
                , testContext.succeeding(resp -> {
                    testContext.verify(() -> {
                        assertThat(resp.statusCode()).isEqualTo(201);
                        assertThat(resp.bodyAsJsonObject().getString("name")).isEqualTo(accountName);
                    });
                }));
        // get the account, it should end with HTTP status code 200
        client.get(8888, "localhost", uri)
            .send(testContext.succeeding(resp -> {
                testContext.verify(() -> {
                    logger.debug("After creation, got account: {}\n", resp.bodyAsString());
                    assertThat(resp.statusCode()).isEqualTo(200);
                })
                    .completeNow();
            }));
        // create the account, it should end with HTTP status code '401'
        // as an account already exists for that user
        client.post(8888, "localhost", uri)
            .sendJsonObject(
                new JsonObject()
                    .put("name", accountName)
                    .put("description", accountDesc)
                , testContext.succeeding(resp -> {
                    testContext.verify(() -> {
                        assertThat(resp.statusCode()).isEqualTo(401);
                        assertThat(resp.bodyAsJsonObject().getString("name")).isEqualTo(accountName);
                    });
                }));

    }

}
