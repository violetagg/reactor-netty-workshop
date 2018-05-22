package io.spring.workshop.reactornetty.http;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.netty.ByteBufFlux;
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
public class HttpEchoPathParamTests {

    @Test
    public void echoPathParamTest() {
        DisposableServer server =
                HttpServer.create()   // Prepares a HTTP server for configuration.
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
                          .wiretap()  // Applies a wire logger configuration.
                          .bindNow(); // Starts the server in a blocking fashion, and waits for it to finish initializing.

        assertNotNull(server);

        String response =
                HttpClient.create()             // Prepares a HTTP client for configuration.
                          .port(server.port())  // Obtains the server's port and provides it as a port to which this
                                                // client should connect.
                          .wiretap()            // Applies a wire logger configuration.
                          .headers(h -> h.add("Content-Type", "text/plain")) // Adds headers to the HTTP request
                          .post()              // Specifies that POST method will be used
                          .uri("/test/World")  // Specifies the path
                          .send(ByteBufFlux.fromString(Flux.just("Hello")))  // Sends the request body
                          .responseContent()   // Receives the response body
                          .aggregate()
                          .asString()
                          .log("http-client")
                          .block();

        assertEquals("Hello World!", response);

        server.disposeNow();          // Stops the server and releases the resources.
    }
}
