package io.github.nickacpt.abst.io

import io.github.nickacpt.abst.io.sockets.client.TcpClient
import kotlin.test.assertContentEquals
import kotlin.test.fail

class TestTcpClient(server: TestTcpServer, private val expectedMessages: MutableList<ByteArray>) : TcpClient("localhost", server.actualPort.toUShort(), true) {
    override fun onMessageReceived(message: ByteArray) {
        if (expectedMessages.isEmpty()) {
            fail("Unexpected message received")
        }

        val expectedMessage = expectedMessages.removeFirst()

        assertContentEquals(expectedMessage, message)
    }
}