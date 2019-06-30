package org.devisions.labs.savings.vx;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("🚀 StartVerticle Tests")
@ExtendWith(VertxExtension.class)
class TestSuite1_StartVerticle {

    @BeforeEach
    void deployVerticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new StartVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void verticleDeployed(Vertx vertx, VertxTestContext testContext) throws Throwable {

        WebClient client = WebClient.create(vertx);
        client.get(8888, "localhost", "/test-up-with-fake-rsc")
            .send(testContext.succeeding(resp -> {
                testContext.verify(() -> assertThat(resp.statusCode()).isEqualTo(404))
                    .completeNow();
            }));

    }

}
