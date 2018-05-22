package io.spring.workshop.reactornetty.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;
import reactor.netty.DisposableServer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

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
        //     .map(jsonDecoder)
        //     .concatMap(Flux::fromArray)
        //     .window(5)
        //     .concatMap(w -> out.send(w.collectList()
        //                               .map(jsonEncoder)))
        // ----
        // 4.4. Send the response
        DisposableServer server = null;

        assertNotNull(server);

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
