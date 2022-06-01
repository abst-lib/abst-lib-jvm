package io.github.nickacpt.abst.io

import io.github.nickacpt.abst.io.sockets.server.TcpClientConnection
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertTimeout
import java.time.Duration
import java.util.*
import kotlin.test.Test

class ServerTests {

    @Test
    fun `Server can listen for connections`() {
        assertDoesNotThrow {
            TestTcpServer(0, Stack()).also {
                it.start()
                it.stop()
            }
        }
    }

    @Test
    fun `Server can listen for connections and accept them`() {
        val server = TestTcpServer(1, mutableListOf())
        assertDoesNotThrow {
            server.also {
                it.start()

                val client = TestTcpClient(server, mutableListOf())
                client.connect()

                while (!client.isConnected) {
                    Thread.sleep(1)
                }

                client.disconnect()

                it.stop()
            }
        }
    }

    @Test
    fun `Server can listen for connections, accept them and receive them`() {
        val expectedMessage = "Hello".toByteArray()

        val server = TestTcpServer(1, mutableListOf(expectedMessage))
        assertDoesNotThrow {
            server.also {
                it.start()

                val client = TestTcpClient(server, mutableListOf())
                client.connect()

                while (!client.isConnected) {
                    Thread.sleep(1)
                }

                client.sendMessage(expectedMessage)

                assertTimeout(Duration.ofSeconds(5)) {
                    server.waitForExpectedMessages()
                }

                client.disconnect()

                it.stop()
            }
        }
    }

    @Test
    fun `Server can echo all messages it receives`() {
        val expectedMessage = "1234567890".toByteArray()

        val server = object: TestTcpServer(1, mutableListOf(expectedMessage)) {
            override fun handleClientMessage(connection: TcpClientConnection, message: ByteArray) {
                super.handleClientMessage(connection, message)
                connection.sendMessage(message)
            }
        }
        assertDoesNotThrow {
            server.also {
                it.start()

                val client = TestTcpClient(server, mutableListOf(expectedMessage))
                client.connect()

                while (!client.isConnected) {
                    Thread.sleep(1)
                }

                client.sendMessage(expectedMessage)

                assertTimeout(Duration.ofSeconds(5)) {
                    server.waitForExpectedMessages()
                }

                assertTimeout(Duration.ofSeconds(5)) {
                    client.waitForExpectedMessages()
                }

                client.disconnect()

                it.stop()
            }
        }
    }

}