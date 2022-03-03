package io.spring.workshop.reactornetty.udp;

import org.junit.jupiter.api.Test;
import reactor.netty.Connection;
import reactor.netty.resources.LoopResources;
import reactor.netty.udp.UdpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Learn how to create UDP server
 *
 * @author Violeta Georgieva
 * @see <a href="https://projectreactor.io/docs/netty/release/api/reactor/netty/udp/UdpServer.html">UdpServer javadoc</a>
 */
public class UdpSupportsReceivingDatagramTests {

    @Test
    public void supportsReceivingDatagramTest() {
        LoopResources loopResources = LoopResources.create("srd-test-udp");
        Connection server1 =
                UdpServer.create() // Prepares a UDP server for configuration.
                         .port(0)  // Configures the port number as zero, this will let the system pick up
                                   // an ephemeral port when binding the server.
                         .runOn(loopResources)     // Configures the UDP server to run on a specific LoopResources.
                         .wiretap(true)            // Applies a wire logger configuration.
                         .bind()   // Binds the UDP server and returns a Mono<Connection>.
                         .block(); // Blocks and waits the server to finish initialising.

        assertNotNull(server1);

        Connection server2 =
                UdpServer.create() // Prepares a UDP server for configuration.
                         .port(0)  // Configures the port number as zero, this will let the system pick up
                                   // an ephemeral port when binding the server.
                         .runOn(loopResources)     // Configures the UDP server to run on a specific LoopResources.
                         .wiretap(true)            // Applies a wire logger configuration.
                         .bind()   // Binds the UDP server and returns a Mono<Connection>.
                         .doOnSuccess(v -> {
                             try {
                                 DatagramChannel udp = DatagramChannel.open();
                                 udp.configureBlocking(true);
                                 udp.connect(new InetSocketAddress(((InetSocketAddress) server1.address()).getPort()));

                                 byte[] data = new byte[1024];
                                 new Random().nextBytes(data);
                                 for (int i = 0; i < 4; i++) {
                                     udp.write(ByteBuffer.wrap(data));
                                 }

                                 udp.close();
                             }
                             catch (IOException e) {
                                 e.printStackTrace();
                             }
                         })
                         .block(); // Blocks and waits the server to finish initialising.

        assertNotNull(server2);

        server1.disposeNow();       // Stops the server and releases the resources.

        server2.disposeNow();       // Stops the server and releases the resources.
    }
}
