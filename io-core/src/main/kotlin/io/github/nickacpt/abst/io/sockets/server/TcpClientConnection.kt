package io.github.nickacpt.abst.io.sockets.server

import io.github.nickacpt.abst.io.sockets.endpoint.ConnectionEndpoint
import io.github.nickacpt.abst.io.sockets.endpoint.InternalConnectionEndpoint
import io.github.nickacpt.abst.io.sockets.getNewClientConnectionThread
import io.github.nickacpt.abst.io.utils.BufferFramer
import org.msgpack.core.MessagePack
import org.msgpack.core.MessagePacker
import org.msgpack.core.MessageUnpacker
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.net.SocketAddress

open class TcpClientConnection(
    val server: TcpServer<in TcpClientConnection>, internal val clientSocket: Socket, override val disconnectOnError: Boolean
) : ConnectionEndpoint, InternalConnectionEndpoint {
    /**
     * The remote address of the client
     */
    val remoteAddress: SocketAddress = clientSocket.remoteSocketAddress

    private val connectionThread = getNewClientConnectionThread(this)

    final override val isConnected: Boolean
        get() = clientSocket.isConnected && !clientSocket.isClosed

    final override val inputStream: InputStream
        get() = clientSocket.getInputStream()

    final override val outputStream: OutputStream
        get() = clientSocket.getOutputStream()

    final override val messagePacker: MessagePacker = MessagePack.newDefaultPacker(outputStream)

    final override val messageUnpacker: MessageUnpacker = MessagePack.newDefaultUnpacker(inputStream)

    internal fun startReceiving() {
        connectionThread.start()
    }

    override fun sendMessage(message: ByteArray) {
        BufferFramer.frame(message).also {
            outputStream.write(it)
            outputStream.flush()
        }
    }

    override fun onMessageReceived(message: ByteArray) {
        server.handleClientMessage(this, message)
    }

    override fun onConnectionClosed() {
        server.onClientDisconnected(this)
    }

    override fun onError(error: Throwable) {
        server.handleClientError(this, error)
    }

    final override fun disconnect() {
        clientSocket.close()
    }

}