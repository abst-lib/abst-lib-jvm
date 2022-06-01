package io.github.nickacpt.abst.protocol.endpoints

import io.github.nickacpt.abst.protocol.Layer7Protocol

/**
 * Represents a protocol aware connection endpoint,
 * meaning something that can connect to something or accept connections from something.
 */
interface ProtocolAwareConnectionEndpoint {
    /**
     * Sends a message to this endpoint.
     *
     * The message is sent asynchronously and will be framed with MessagePack.
     * @param message The message to send.
     */
    fun sendMessage(protocol: Layer7Protocol, message: ByteArray)
}