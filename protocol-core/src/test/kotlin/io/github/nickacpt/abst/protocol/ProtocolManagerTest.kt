package io.github.nickacpt.abst.protocol

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ProtocolManagerTest {

    @Test
    fun registerProtocol() {
        assert(ProtocolManager.protocols.isEmpty())

        ProtocolManager.registerProtocol(TestProtocol(0, "Test"))

        assert(ProtocolManager.protocols.size == 1)
    }

    @Test
    fun getProtocolById() {
        ProtocolManager.registerProtocol(TestProtocol(0, "Test"))

        assert(ProtocolManager.getProtocolById(0) is TestProtocol)
    }

    @Test
    fun getProtocolByIdOrThrow() {
        ProtocolManager.registerProtocol(TestProtocol(0, "Test"))

        assert(ProtocolManager.getProtocolByIdOrThrow(0) is TestProtocol)

        assertThrows<IllegalArgumentException> {
            ProtocolManager.getProtocolByIdOrThrow(1)
        }
    }
}