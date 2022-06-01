package io.github.nickacpt.abst.protocol

/**
 * Helper object to manage registered layer 7 protocols
 */
object ProtocolManager {

    private val protocols = mutableMapOf<Byte, Layer7Protocol>()

    /**
     * Register layer 7 protocol
     */
    fun registerProtocol(protocol: Layer7Protocol) {
        protocols[protocol.id] = protocol
    }

    /**
     * Get layer 7 protocol by id
     */
    fun getProtocolById(id: Byte): Layer7Protocol? {
        return protocols[id]
    }

    fun getProtocolByIdOrThrow(id: Byte): Layer7Protocol {
        return protocols[id] ?: throw IllegalArgumentException("No protocol with id $id")
    }

}