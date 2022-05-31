package io.github.nickacpt.abst.io

import org.junit.jupiter.api.assertDoesNotThrow
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

}