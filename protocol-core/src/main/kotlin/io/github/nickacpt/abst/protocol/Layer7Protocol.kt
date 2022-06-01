package io.github.nickacpt.abst.protocol

import io.github.nickacpt.abst.protocol.endpoints.ProtocolAwareConnectionEndpoint

/**
 * Interface representing a networking protocol on top of abst-lib
 */
interface Layer7Protocol {

    /**
     * The id of the protocol.
     * This is used to identify the protocol at the layer 7
     */
    val id: Byte

    /**
     * The friendly name of the protocol.
     */
    val name: String

    /**
     * Handle a packet received from the network
     */
    fun handleMessage(message: ByteArray, endpoint: ProtocolAwareConnectionEndpoint) {}

    /**
     * Sends a packet to this endpoint
     */
    fun sendMessage(message: ByteArray, endpoint: ProtocolAwareConnectionEndpoint) {
        endpoint.sendMessage(this, message)
    }

}