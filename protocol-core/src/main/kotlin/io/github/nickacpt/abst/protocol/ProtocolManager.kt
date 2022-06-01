package io.github.nickacpt.abst.protocol

/**
 * Helper object to manage registered layer 7 protocols
 */
object ProtocolManager {

    private val protocolMap = mutableMapOf<Byte, Layer7Protocol>()

    val protocols: Collection<Layer7Protocol> get() = protocolMap.values

    /**
     * Register layer 7 protocol
     */
    fun registerProtocol(protocol: Layer7Protocol) {
        protocolMap[protocol.id] = protocol
    }

    /**
     * Get layer 7 protocol by id
     */
    fun getProtocolById(id: Byte): Layer7Protocol? {
        return protocolMap[id]
    }

    fun getProtocolByIdOrThrow(id: Byte): Layer7Protocol {
        return protocolMap[id] ?: throw IllegalArgumentException("No protocol with id $id")
    }

}