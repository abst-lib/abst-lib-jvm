package io.github.nickacpt.abst.io.sockets.server

import io.github.nickacpt.abst.io.sockets.endpoint.ConnectionEndpoint
import io.github.nickacpt.abst.io.sockets.getNewClientConnectionThread
import io.github.nickacpt.abst.io.utils.BufferFramer
import org.msgpack.core.MessagePack
import org.msgpack.core.MessagePacker
import org.msgpack.core.MessageUnpacker
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.net.SocketAddress

data class TcpClientConnection internal constructor(
    val server: TcpServer, val clientSocket: Socket, override val disconnectOnError: Boolean
) : ConnectionEndpoint {
    /**
     * The remote address of the client
     */
    val remoteAddress: SocketAddress = clientSocket.remoteSocketAddress

    private val connectionThread = getNewClientConnectionThread(this)

    internal fun startReceiving() {
        connectionThread.start()
    }

    override val isConnected: Boolean
        get() = clientSocket.isConnected && !clientSocket.isClosed

    override val inputStream: InputStream
        get() = clientSocket.getInputStream()

    override val outputStream: OutputStream
        get() = clientSocket.getOutputStream()

    override val messagePacker: MessagePacker = MessagePack.newDefaultPacker(outputStream)

    override val messageUnpacker: MessageUnpacker = MessagePack.newDefaultUnpacker(inputStream)

    override fun sendMessage(message: ByteArray) {
        BufferFramer.frame(message).also {
            outputStream.write(it.array())
            outputStream.flush()
        }
    }

    override fun onMessageReceived(message: ByteArray) {
        server.handleClientMessage(this, message)
    }

    override fun onConnectionClosed() {
        server.handleClientDisconnect(this)
    }

    override fun onError(error: Throwable) {
        server.handleClientError(this, error)
    }

    override fun disconnect() {
        clientSocket.close()
    }

}