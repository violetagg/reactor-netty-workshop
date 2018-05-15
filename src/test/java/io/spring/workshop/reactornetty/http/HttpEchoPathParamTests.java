package io.spring.workshop.reactornetty.http;

import org.junit.jupiter.api.Test;
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
public class HttpEchoPathParamTests {

    @Test
    public void echoPathParamTest() {
        DisposableServer server =
                HttpServer.create()   // Prepares an HTTP server for configuration.
                          .port(0)    // Configures the port number as zero, this will let the system pick up
                                      // an ephemeral port when binding the server.
                          .route(routes ->
                                  // The server will respond only on POST requests
                                  // where the path starts with /test and then there is path parameter
                                  routes.post("/test/{param}", (req, res) ->
                                          res.sendString(req.receive()
                                                            .asString()
                                                            .map(s -> s + ' ' + req.param("param") + '!')
                                                            .log("http-server"))))
                          .wiretap(true)  // Applies a wire logger configuration.
                          .bindNow(); // Starts the server in a blocking fashion, and waits for it to finish initializing.

        assertNotNull(server);

        server.disposeNow();          // Stops the server and releases the resources.
    }
}
