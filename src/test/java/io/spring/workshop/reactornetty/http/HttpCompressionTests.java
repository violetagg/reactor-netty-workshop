package io.spring.workshop.reactornetty.http;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Learn how to create HTTP server and client
 *
 * @author Violeta Georgieva
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/http/server/HttpServer.html">HttpServer javadoc</a>
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/http/client/HttpClient.html">HttpClient javadoc</a>
 */
public class HttpCompressionTests {

    @Test
    public void httpCompressionTest() {
        DisposableServer server =
                HttpServer.create()   // Prepares a HTTP server for configuration.
                          .port(0)    // Configures the port number as zero, this will let the system pick up
                                      // an ephemeral port when binding the server.
                          .handle((req, res) -> res.sendString(Mono.just("compressed response")))
                          .compress(true) // Enables compression.
                          .wiretap(true)  // Applies a wire logger configuration.
                          .bindNow(); // Starts the server in a blocking fashion, and waits for it to finish initializing.

        assertNotNull(server);

        server.disposeNow();          // Stops the server and releases the resources.
    }
}
