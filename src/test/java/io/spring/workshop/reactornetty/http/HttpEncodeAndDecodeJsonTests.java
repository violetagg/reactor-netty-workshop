package io.spring.workshop.reactornetty.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Learn how to create HTTP server and client
 *
 * @author Violeta Georgieva
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/http/server/HttpServer.html">HttpServer javadoc</a>
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/http/client/HttpClient.html">HttpClient javadoc</a>
 */
public class HttpEncodeAndDecodeJsonTests {
    private Function<List<Pojo>, ByteBuf> jsonEncoder;
    private Function<String, Pojo[]> jsonDecoder;

    @BeforeEach
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
                                  out.send(in.withConnection(c -> c.addHandler(new JsonObjectDecoder()))
                                             .receive()
                                             .asString()
                                             .map(jsonDecoder)
                                             .concatMap(Flux::fromArray)
                                             .window(5)
                                             .concatMap(w -> w.collectList().map(jsonEncoder))))
                          .wiretap(true)  // Applies a wire logger configuration.
                          .bindNow(); // Starts the server in a blocking fashion, and waits for it to finish initializing.

        assertNotNull(server);

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
