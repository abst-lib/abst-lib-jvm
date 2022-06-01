package io.github.nickacpt.abst.protocol

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

}