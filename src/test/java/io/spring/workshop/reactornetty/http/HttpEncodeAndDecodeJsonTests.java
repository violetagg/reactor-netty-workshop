package io.spring.workshop.reactornetty.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.netty.DisposableServer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // TODO
        // Task 1:
        // 1.1. Prepare the HTTP server
        // 1.2. Configure the port to which this server should bind
        // 1.3. Start the server in a blocking fashion and wait for it
        //      to finish initializing
        //
        // Task 3:
        // 3.3. Apply a wire logger configuration
        //
        // Task 4:
        // 4.1. Add to the request pipeline a new handler 'JsonObjectDecoder'
        // 4.2. When receive the request body, transform it to string
        // 4.3. Use the snippet below to do JSON decoding, prepare response as batches
        //      and do JSON encoding
        // ----
        //    out.send(in.withConnection(c -> c.addHandler(new JsonObjectDecoder()))
        //               .receive()
        //               .asString()
        //               .map(jsonDecoder)
        //               .concatMap(Flux::fromArray)
        //               .window(5)
        //               .concatMap(w -> w.collectList().map(jsonEncoder))))
        // ----
        // 4.4. Send the response
        DisposableServer server = null;

        assertNotNull(server);

        // TODO
        // Task 5:
        // 5.1. Prepare the HTTP client
        // 5.2. Obtain the server's port and provide it as a port to which this
        //      client should connect
        // 5.3. Apply a wire logger configuration
        // 5.4. Add to the request pipeline a new handler 'JsonObjectDecoder'
        // 5.5. Prepare a POST request
        // 5.6. Specify the path
        // 5.7. Send the snippet below as a request body. It will do JSON encoding
        // ----
        //      Flux.range(1, 10)
        //          .map(i -> new Pojo("test " + i))
        //          .collectList()
        //          .map(jsonEncoder)
        // ----
        // 5.8. Receive the response, transform it to string and do JSON decoding
        // ----
        //      <response-bytes>.asString()
        //                      .map(jsonDecoder)
        //                      .concatMap(Flux::fromArray)
        // ----
        // 5.9. Subscribe to the returned Flux<Pojo> and block the last element
        Pojo response = null;

        assertEquals("test 10", response.getName());

        // TODO
        // Task 2:
        // 2.1. Close the underlying channel opened by the HTTP server
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
