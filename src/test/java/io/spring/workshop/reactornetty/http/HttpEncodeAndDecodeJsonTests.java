package io.spring.workshop.reactornetty.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Learn how to create HTTP server and client
 *
 * @author Violeta Georgieva
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/http/server/HttpServer.html">HttpServer javadoc</a>
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/http/client/HttpClient.html">HttpClient javadoc</a>
 */
public class HttpEncodeAndDecodeJsonTests {
    private Function<List<Pojo>, ByteBuf> jsonEncoder;
    private Function<String, Pojo[]> jsonDecoder;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();

        jsonEncoder = pojo -> {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mapper.writeValue(out, pojo);
                return Unpooled.copiedBuffer(out.toByteArray());
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        };

        jsonDecoder = s -> {
            try {
                return mapper.readValue(s, Pojo[].class);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Test
    public void encodeAndDecodeJsonTest() {
        DisposableServer server =
                HttpServer.create()   // Prepares a HTTP server for configuration.
                          .port(0)    // Configures the port number as zero, this will let the system pick up
                                      // an ephemeral port when binding the server.
                          .handle((in, out) ->
                                  // Calls the passed callback with Connection to operate on the
                                  // underlying channel state. This allows changing the channel pipeline.
                                  in.withConnection(c -> c.addHandler(new JsonObjectDecoder()))
                                    .receive()
                                    .asString()
                                    .map(jsonDecoder)
                                    .concatMap(Flux::fromArray)
                                    .window(5)
                                    .concatMap(w -> out.send(w.collectList().map(jsonEncoder))))
                          .wiretap()  // Applies a wire logger configuration.
                          .bindNow(); // Starts the server in a blocking fashion, and waits for it to finish initializing.

        assertNotNull(server);

        Pojo response =
                HttpClient.create()            // Prepares a HTTP client for configuration.
                          .port(server.port()) // Obtains the server's port and provides it as a port to which this
                                               // client should connect.
                          // Extends the channel pipeline.
                          .doOnResponse((res, conn) -> conn.addHandler(new JsonObjectDecoder()))
                          .wiretap()           // Applies a wire logger configuration.
                          .post()              // Specifies that POST method will be used.
                          .uri("/test")        // Specifies the path.
                          .send(Flux.range(1, 10)
                                    .map(i -> new Pojo("test " + i))
                                    .collectList()
                                    .map(jsonEncoder))
                          .response((res, byteBufFlux) ->
                              byteBufFlux.asString()
                                         .map(jsonDecoder)
                                         .concatMap(Flux::fromArray))
                          .blockLast();

        assertEquals("test 10", response.getName());

        server.disposeNow();          // Stops the server and releases the resources.
    }

    private static final class Pojo {
        private String name;

        Pojo() {
        }

        Pojo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Pojo {" + "name='" + name + '\'' + '}';
        }
    }
}
