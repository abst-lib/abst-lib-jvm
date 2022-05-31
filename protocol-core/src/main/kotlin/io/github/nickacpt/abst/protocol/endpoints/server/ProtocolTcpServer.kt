package io.github.nickacpt.abst.protocol.endpoints.server

import io.github.nickacpt.abst.io.sockets.server.TcpServer
import io.github.nickacpt.abst.protocol.Layer7Protocol
import io.github.nickacpt.abst.protocol.ProtocolManager
import io.github.nickacpt.abst.protocol.utils.ProtocolFramer
import org.msgpack.core.buffer.MessageBuffer

abstract class ProtocolTcpServer<C : ProtocolTcpClientConnection>(host: String, port: UShort, disconnectOnError: Boolean) :
    TcpServer<C>(
        host, port,
        disconnectOnError = disconnectOnError
    ) {
    abstract fun handleClientMessage(connection: C, protocol: Layer7Protocol, message: ByteArray)

    final override fun handleClientMessage(connection: C, message: ByteArray) {
        val (protocolId, framedMessage) = ProtocolFramer.unframe(MessageBuffer.wrap(message))
        val protocol = ProtocolManager.getProtocolByIdOrThrow(protocolId)

        handleClientMessage(connection, protocol, framedMessage)
    }
}