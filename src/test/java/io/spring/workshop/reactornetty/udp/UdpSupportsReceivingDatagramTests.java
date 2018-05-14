package io.spring.workshop.reactornetty.udp;

import org.junit.Test;
import reactor.netty.Connection;
import reactor.netty.resources.LoopResources;

import static org.junit.Assert.assertNotNull;

/**
 * Learn how to create UDP server
 *
 * @author Violeta Georgieva
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/udp/UdpServer.html">UdpServer javadoc</a>
 */
public class UdpSupportsReceivingDatagramTests {

    @Test
    public void supportsReceivingDatagramTest() {
        // TODO
        // Task 1:
        // 1.1. Prepare the UDP server
        // 1.2. Configure the port to which this server should bind
        // 1.3. Bind the server
        // 1.4. Subscribe to the returned Mono<Connection> and block
        // 1.5. Create a new simple LoopResources
        // 1.6. Configure the UDP server to run on this newly created LoopResources
        // 1.7. Apply a wire logger configuration
        LoopResources loopResources = null;
        Connection server1 = null;

        assertNotNull(server1);

        // TODO
        // Task 3:
        // 3.1. Prepare the UDP server
        // 3.2. Configure the port to which this server should bind
        // 3.3. Bind the server
        // 3.4. Subscribe to the returned Mono<Connection> and block
        // 3.5. Create a new simple LoopResources
        // 3.6. Configure the UDP server to run on this newly created LoopResources
        // 3.7. Apply a wire logger configuration
        //
        // Task 5:
        // 5.1. As soon as bind operation completed successfully, open a DatagramChannel,
        //      connect to the server1's port, send data and close the channel
        Connection server2 = null;

        assertNotNull(server2);

        // TODO
        // Task 2:
        // 2.1. Close the underlying channel opened by the UDP server

        // TODO
        // Task 4:
        // 4.1. Close the underlying channel opened by the UDP server
    }
}
