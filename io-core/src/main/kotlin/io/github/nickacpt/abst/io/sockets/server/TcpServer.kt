package io.github.nickacpt.abst.io.sockets.server

import io.github.nickacpt.abst.io.sockets.getNewServerListenerThread
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

/**
 * Abstract class for creating and managing a server socket
 * This class sends and receives data framed using the MessagePack protocol.
 *
 * @param host The host to bind to.
 * @param port The port to bind to.
 */
abstract class TcpServer<C: TcpClientConnection>(val host: String, val port: UShort, val disconnectOnError: Boolean = true) {
    internal val serverSocket = ServerSocket(port.toInt(), 50, InetAddress.getByName(host))
    private val clientConnections = mutableListOf<C>()

    /**
     * Immutable list of all the clients currently connected to the server.
     */
    val clients: List<C>
        get() = clientConnections

    /**
     * Whether the server is currently listening for connections.
     */
    var isRunning = false
        internal set

    /**
     * Starts the server and listens for incoming connections in a separate thread.
     */
    fun start() {
        isRunning = true
        getNewServerListenerThread(this).start()
    }

    /**
     * Stops the server and closes all connections.
     */
    fun stop() {
        isRunning = false
        clientConnections.forEach { it.disconnect() }
        serverSocket.close()
    }

    internal fun handleClientConnection(connection: C) {
        clientConnections.add(connection)
        connection.startReceiving()
    }

    /**
     * Handles a message received from a client.
     */
    abstract fun handleClientMessage(connection: C, message: ByteArray)

    /**
     * Handles a client disconnection.
     */
    open fun handleClientDisconnect(connection: C) { }

    /**
     * Handles an error that occurred while handling a client connection.
     */
    open fun handleClientError(connection: C, error: Throwable) {
        error.printStackTrace()
    }

    open fun onError(e: Exception) {
        e.printStackTrace()
    }

    /**
     * Method to create a new client connection object.
     */
    abstract fun createClientConnection(socket: Socket): C

    internal fun onClientDisconnected(c: C) {
        clientConnections.remove(c)
        handleClientDisconnect(c)
    }
}

