package io.spring.workshop.reactornetty.tcp;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.junit.Test;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

import static org.junit.Assert.assertNotNull;

/**
 * Learn how to create TCP server and client
 *
 * @author Violeta Georgieva
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/tcp/TcpServer.html">TcpServer javadoc</a>
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/tcp/TcpClient.html">TcpClient javadoc</a>
 */
public class TcpSendFileTests {

    @Test
    public void sendFileTest() throws Exception {
        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContextBuilder sslContextBuilder =
               SslContextBuilder.forServer(cert.certificate(), cert.privateKey());
        DisposableServer server =
                TcpServer.create()   // Prepares a TCP server for configuration.
                         .port(0)    // Configures the port number as zero, this will let the system pick up
                                     // an ephemeral port when binding the server.
                         .secure(spec -> spec.sslContext(sslContextBuilder))   // Use self singed certificate.
                         .wiretap()  // Applies a wire logger configuration.
                         .bindNow(); // Starts the server in a blocking fashion, and waits for it to finish initializing.

        assertNotNull(server);

        server.disposeNow(); // Stops the server and releases the resources.
    }
}
