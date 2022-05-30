package io.github.nickacpt.abst.io.sockets.endpoint


/**
 * Represents a connection endpoint,
 * meaning something that can connect to something or accept connections from something.
 */
interface ConnectionEndpoint {

    /**
     * Property that represents whether this endpoint is connected to something.
     *
     * @return Whether this endpoint is connected to something.
     */
    val isConnected: Boolean

    /**
     * Property that represents whether this endpoint should disconnect when an error occurs.
     *
     * @return Whether this endpoint should disconnect when an error occurs.
     */
    val disconnectOnError: Boolean

    /**
     * Handler for when a message is received.
     */
    fun onMessageReceived(message: ByteArray)

    /**
     * Handler for when a connection is closed.
     */
    fun onConnectionClosed()

    /**
     * Handler for when an error occurs.
     */
    fun onError(error: Throwable)

    /**
     * Requests that this endpoint disconnect.
     */
    fun disconnect()

    /**
     * Sends a message to this endpoint.
     *
     * The message is sent asynchronously and will be framed with MessagePack.
     * @param message The message to send.
     */
    fun sendMessage(message: ByteArray)

}