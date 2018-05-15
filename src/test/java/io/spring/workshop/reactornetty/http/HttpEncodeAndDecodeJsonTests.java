package io.spring.workshop.reactornetty.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

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
