package io.github.nickacpt.abst.protocol.endpoints.client

import io.github.nickacpt.abst.io.sockets.client.TcpClient
import io.github.nickacpt.abst.protocol.Layer7Protocol
import io.github.nickacpt.abst.protocol.ProtocolManager
import io.github.nickacpt.abst.protocol.utils.ProtocolFramer
import org.msgpack.core.buffer.MessageBuffer

abstract class ProtocolTcpClient(
    host: String,
    port: UShort,
    disconnectOnError: Boolean,
    timeout: Int = 1000
) : TcpClient(host, port, disconnectOnError = disconnectOnError, timeout = timeout) {

    abstract fun onMessageReceived(protocol: Layer7Protocol, message: ByteArray)

    /**
     * Sends a message to the remote host.
     *
     * This method will automatically frame the message using the MessagePack protocol.
     * This method is not asynchronous.
     */
    fun sendMessage(protocol: Layer7Protocol, message: ByteArray) {
        sendMessage(ProtocolFramer.frame(protocol, message))
    }

    final override fun onMessageReceived(message: ByteArray) {
        val (protocolId, framedMessage) = ProtocolFramer.unframe(MessageBuffer.wrap(message))
        val protocol = ProtocolManager.getProtocolByIdOrThrow(protocolId)

        onMessageReceived(protocol, framedMessage)
    }
}