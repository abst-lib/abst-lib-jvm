package io.github.nickacpt.abst.io.sockets.endpoint

import org.msgpack.core.MessagePack
import org.msgpack.core.MessagePacker
import org.msgpack.core.MessageUnpacker
import java.io.InputStream
import java.io.OutputStream


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
     * The input stream of this endpoint.
     * This is used to read data from the endpoint.
     */
    val inputStream: InputStream

    /**
     * The output stream of this endpoint.
     * This is used to write data to the endpoint.
     */
    val outputStream: OutputStream

    /**
     * The message packer of this endpoint.
     */
    val messagePacker: MessagePacker

    /**
     * The message unpacker of this endpoint.
     */
    val messageUnpacker: MessageUnpacker

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