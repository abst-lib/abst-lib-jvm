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

    override fun disconnect() {
        super.disconnect()
        if (expectedMessages.isNotEmpty()) {
            fail("Expected messages not received")
        }
    }

    fun waitForExpectedMessages() {
        while (expectedMessages.isNotEmpty()) {
            Thread.sleep(10)
        }
    }
}