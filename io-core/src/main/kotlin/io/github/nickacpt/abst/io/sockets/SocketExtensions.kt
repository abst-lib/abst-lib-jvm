package io.github.nickacpt.abst.io.sockets

import io.github.nickacpt.abst.io.sockets.client.TcpClient
import io.github.nickacpt.abst.io.sockets.endpoint.TcpEndpointReceiverThread
import io.github.nickacpt.abst.io.sockets.server.TcpClientConnection
import io.github.nickacpt.abst.io.sockets.server.TcpServer
import io.github.nickacpt.abst.io.sockets.server.TcpServerListenerThread

internal fun getNewClientConnectionThread(client: TcpClient): Thread {
    return Thread(TcpEndpointReceiverThread(client)).also {
        it.name = "abst-lib - Client -> Server Connection Thread ${client.host}:${client.port}"
    }
}

internal fun getNewClientConnectionThread(clientConnection: TcpClientConnection): Thread {
    return Thread(TcpEndpointReceiverThread(clientConnection)).also {
        val addr = clientConnection.clientSocket.inetAddress
        it.name = "abst-lib - Server -> Client Connection Thread ${addr.hostName}:${clientConnection.clientSocket.port}"
    }
}

internal fun <C : TcpClientConnection> getNewServerListenerThread(server: TcpServer<C>): Thread {
    return Thread(TcpServerListenerThread(server)).also {
        it.name = "abst-lib - Server Connection Listener Thread ${server.host}:${server.port}"
    }
}