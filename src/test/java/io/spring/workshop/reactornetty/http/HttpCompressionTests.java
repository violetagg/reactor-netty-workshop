package io.spring.workshop.reactornetty.http;

import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Learn how to create HTTP server and client
 *
 * @author Violeta Georgieva
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/http/server/HttpServer.html">HttpServer javadoc</a>
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/http/client/HttpClient.html">HttpClient javadoc</a>
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
                          .wiretap()  // Applies a wire logger configuration.
                          .bindNow(); // Starts the server in a blocking fashion, and waits for it to finish initializing.

        assertNotNull(server);

        String response =
                HttpClient.create()            // Prepares a HTTP client for configuration.
                          .port(server.port()) // Obtains the server's port and provides it as a port to which this
                                               // client should connect.
                          .compress(true)          // Enables compression.
                          .wiretap()           // Applies a wire logger configuration.
                          .get()               // Specifies that GET method will be used.
                          .uri("/test")        // Specifies the path.
                          .responseContent()   // Receives the response body.
                          .aggregate()
                          .asString()
                          .log("http-client")
                          .block();

        assertEquals("compressed response", response);

        server.disposeNow();          // Stops the server and releases the resources.
    }
}
