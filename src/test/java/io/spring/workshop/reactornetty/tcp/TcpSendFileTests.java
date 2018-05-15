package io.spring.workshop.reactornetty.tcp;

import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;
import reactor.netty.tcp.TcpSslContextSpec;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Learn how to create TCP server and client
 *
 * @author Violeta Georgieva
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/tcp/TcpServer.html">TcpServer javadoc</a>
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/tcp/TcpClient.html">TcpClient javadoc</a>
 */
public class TcpSendFileTests {

    @Test
    public void sendFileTest() throws Exception {
        SelfSignedCertificate cert = new SelfSignedCertificate();
        TcpSslContextSpec sslContextBuilder =
                TcpSslContextSpec.forServer(cert.certificate(), cert.privateKey());
        DisposableServer server =
                TcpServer.create()   // Prepares a TCP server for configuration.
                         .port(0)    // Configures the port number as zero, this will let the system pick up
                                     // an ephemeral port when binding the server.
                         .secure(spec -> spec.sslContext(sslContextBuilder))   // Use self singed certificate.
                         .wiretap(true)  // Applies a wire logger configuration.
                         .handle((in, out) ->
                                 in.receive()
                                   .asString()
                                   .flatMap(s -> {
                                       try {
                                           Path file = Paths.get(getClass().getResource(s).toURI());
                                           return out.sendFile(file)
                                                     .then();
                                       } catch (URISyntaxException e) {
                                           return Mono.error(e);
                                       }
                                   })
                                   .log("tcp-server"))
                         .bindNow(); // Starts the server in a blocking fashion, and waits for it to finish initializing.

        assertNotNull(server);

        Connection client =
                TcpClient.create()            // Prepares a TCP client for configuration.
                         .port(server.port()) // Obtains the server's port and provide it as a port to which this
                                              // client should connect.
                         // Configures SSL providing an already configured SslContext.
                         .secure(spec -> spec.sslContext(
                                 TcpSslContextSpec.forClient()
                                                  .configure(builder -> builder.trustManager(InsecureTrustManagerFactory.INSTANCE))))
                         .wiretap(true)       // Applies a wire logger configuration.
                         .connectNow();       // Blocks the client and returns a Connection.

        assertNotNull(client);

        server.disposeNow(); // Stops the server and releases the resources.

        client.disposeNow(); // Stops the client and releases the resources.
    }
}
