package io.github.nickacpt.abst.protocol

import io.github.nickacpt.abst.protocol.endpoints.ProtocolAwareConnectionEndpoint

/**
 * Abstract implementation of a protocol that is wrapping another protocol
 */
abstract class AbstractWrapperProtocol(val next: Layer7Protocol) : Layer7Protocol {

    abstract fun wrap(message: ByteArray): ByteArray

    abstract fun unwrap(message: ByteArray): ByteArray

    final override fun sendMessage(message: ByteArray, endpoint: ProtocolAwareConnectionEndpoint) {
        endpoint.sendMessage(this, wrap(message))
    }

    final override fun handleMessage(message: ByteArray, endpoint: ProtocolAwareConnectionEndpoint) {
        next.handleMessage(unwrap(message), endpoint)
    }
}