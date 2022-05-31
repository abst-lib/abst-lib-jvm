package io.github.nickacpt.abst.io

import io.github.nickacpt.abst.io.sockets.server.TcpClientConnection
import io.github.nickacpt.abst.io.sockets.server.TcpServer
import java.net.Socket
import kotlin.test.assertContentEquals
import kotlin.test.fail

class TestTcpServer(private val expectedClients: Int, private val expectedMessages: MutableList<ByteArray>) :
    TcpServer<TcpClientConnection>("localhost", 0u, true) {

    var clientCount = 0

    val actualPort get() = serverSocket.localPort

    override fun handleClientMessage(connection: TcpClientConnection, message: ByteArray) {
        if (expectedMessages.isEmpty()) {
            fail("Unexpected message received")
        }

        val expectedMessage = expectedMessages.removeFirst()

        assertContentEquals(expectedMessage, message)
    }

    override fun createClientConnection(socket: Socket): TcpClientConnection {
        clientCount++
        if (clientCount > expectedClients) {
            fail("Too many clients connected")
        }
        return TcpClientConnection(this, socket, true)
    }

    override fun handleClientError(connection: TcpClientConnection, error: Throwable) {
        fail("Client disconnected with error", error)
    }

    override fun onError(e: Exception) {
        super.onError(e)
        fail("Server disconnected with error", e)
    }

    override fun handleClientDisconnect(connection: TcpClientConnection) {
        clientCount--
        if (clientCount < 0) {
            fail("Too many clients disconnected")
        }
    }
}