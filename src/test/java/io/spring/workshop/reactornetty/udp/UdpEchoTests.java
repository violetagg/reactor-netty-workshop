package io.spring.workshop.reactornetty.udp;

import org.junit.Test;
import reactor.netty.Connection;
import reactor.netty.resources.LoopResources;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Learn how to create UDP server and client
 *
 * @author Violeta Georgieva
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/udp/UdpServer.html">UdpServer javadoc</a>
 * @see <a href="http://next.projectreactor.io/docs/netty/snapshot/api/reactor/netty/udp/UdpClient.html">UdpClient javadoc</a>
 */
public class UdpEchoTests {

    @Test
    public void echoTest() throws Exception {
        // TODO
        // Task 1:
        // 1.1. Prepare the UDP server
        // 1.2. Configure the port to which this server should bind
        // 1.3. Bind the server
        // 1.4. Subscribe to the returned Mono<Connection> and block
        //
        // Task 3:
        // 3.1. Create a new simple LoopResources
        // 3.2. Configure the UDP server to run on this newly created LoopResources
        // 3.3. Apply a wire logger configuration
        //
        // Task 4:
        // 4.1. Attach an IO handler to react on connected client
        // 4.2. When receive an object (io.netty.channel.socket.DatagramPacket),
        //      transform it and send the new object to the client
        LoopResources loopResources = null;
        Connection server = null;

        assertNotNull(server);

        // TODO
        // Task 5:
        // 5.1. Prepare the UDP client
        // 5.2. Obtain the server's port and provide it as a port to which this
        //      client should connect
        // 5.3. Connect the client
        // 5.4. Subscribe to the returned Mono<Connection> and block
        //
        // Task 7:
        // 7.1. Create a new simple LoopResources
        // 7.2. Configure the UDP server to run on this newly created LoopResources
        // 7.3. Apply a wire logger configuration
        //
        // Task 8:
        // 8.1. Attach an IO handler
        // 8.2. Send a string over the wire
        // 8.3. When receive an object (io.netty.channel.socket.DatagramPacket),
        //      inspect whether is it the expected reply and decrements the count of the latch
        CountDownLatch latch = new CountDownLatch(1);
        Connection client = null;

        assertNotNull(client);

        assertTrue(latch.await(30, TimeUnit.SECONDS));

        // TODO
        // Task 2:
        // 2.1. Close the underlying channel opened by the UDP server

        // TODO
        // Task 6:
        // 6.1. Close the underlying channel opened by the UDP client
    }
}
