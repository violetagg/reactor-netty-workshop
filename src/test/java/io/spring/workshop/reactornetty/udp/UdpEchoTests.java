package io.spring.workshop.reactornetty.udp;

import org.junit.jupiter.api.Test;
import reactor.netty.Connection;
import reactor.netty.udp.UdpServer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Learn how to create a UDP server and client
 *
 * @author Violeta Georgieva
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/udp/UdpServer.html">UdpServer javadoc</a>
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/udp/UdpClient.html">UdpClient javadoc</a>
 */
public class UdpEchoTests {

    @Test
    public void echoTest() {
        Connection server =
                UdpServer.create() // Prepares a UDP server for configuration.
                         .port(0)  // Configures the port number as zero, this will let the system pick up
                                   // an ephemeral port when binding the server.
                         .bind()   // Binds the UDP server and returns a Mono<Connection>.
                         .block(); // Blocks and waits the server to finish initialising.

        assertNotNull(server);

        server.disposeNow();       // Stops the server and releases the resources.
    }
}
