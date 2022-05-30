package io.github.nickacpt.abst.io.sockets.client

import io.github.nickacpt.abst.io.sockets.endpoint.ConnectionEndpoint
import io.github.nickacpt.abst.io.sockets.endpoint.InternalConnectionEndpoint
import io.github.nickacpt.abst.io.sockets.getNewClientConnectionThread
import io.github.nickacpt.abst.io.utils.BufferFramer
import org.msgpack.core.MessagePack
import org.msgpack.core.MessagePacker
import org.msgpack.core.MessageUnpacker
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Abstract class for creating and managing a socket connection to a remote host.
 *
 * This class sends and receives data framed using the MessagePack protocol.
 *
 * @param host The host to connect to.
 * @param port The port to connect to.
 * @param timeout The timeout for the connection.
 */
abstract class TcpClient(
    val host: String, val port: UShort, override val disconnectOnError: Boolean = true, private val timeout: Int = 1000
) : ConnectionEndpoint, InternalConnectionEndpoint {
    private val socket = Socket()

    final override val inputStream: InputStream
        get() = socket.getInputStream()

    final override val outputStream: OutputStream
        get() = socket.getOutputStream()

    override val messagePacker: MessagePacker by lazy {
        MessagePack.newDefaultPacker(outputStream)
    }

    override val messageUnpacker: MessageUnpacker by lazy {
        MessagePack.newDefaultUnpacker(inputStream)
    }

    override val isConnected get() = socket.isConnected && !socket.isClosed

    fun connect() {
        socket.connect(InetSocketAddress(host, port.toInt()), timeout)
        val thread = getNewClientConnectionThread(this)
        thread.start()
    }

    override fun disconnect() {
        socket.close()
        onConnectionClosed()
    }

    abstract override fun onMessageReceived(message: ByteArray)

    abstract override fun onConnectionClosed()

    override fun onError(error: Throwable) {
        error.printStackTrace()
    }

    /**
     * Sends a message to the remote host.
     *
     * This method will automatically frame the message using the MessagePack protocol.
     * This method is not asynchronous.
     */
    final override fun sendMessage(message: ByteArray) {
        outputStream.write(BufferFramer.frame(message).array())
        outputStream.flush()
    }
}

