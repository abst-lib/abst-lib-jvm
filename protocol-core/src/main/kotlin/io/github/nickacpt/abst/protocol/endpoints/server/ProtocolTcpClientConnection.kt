package io.github.nickacpt.abst.protocol.endpoints.server

import io.github.nickacpt.abst.io.sockets.server.TcpClientConnection
import io.github.nickacpt.abst.io.sockets.server.TcpServer
import io.github.nickacpt.abst.protocol.Layer7Protocol
import io.github.nickacpt.abst.protocol.endpoints.ProtocolAwareConnectionEndpoint
import io.github.nickacpt.abst.protocol.utils.ProtocolFramer
import java.net.Socket

open class ProtocolTcpClientConnection(
    server: TcpServer<in ProtocolTcpClientConnection>,
    clientSocket: Socket, disconnectOnError: Boolean
) : TcpClientConnection(server as TcpServer<in TcpClientConnection>, clientSocket, disconnectOnError),
    ProtocolAwareConnectionEndpoint {

    @Deprecated(
        "Use sendMessage(protocol, message) instead",
        ReplaceWith(
            "this.sendMessage(protocol, message)",
        ), DeprecationLevel.ERROR
    )
    override fun sendMessage(message: ByteArray) {
        super.sendMessage(message)
    }

    override fun sendMessage(protocol: Layer7Protocol, message: ByteArray) {
        super.sendMessage(ProtocolFramer.frame(protocol, message))
    }
}