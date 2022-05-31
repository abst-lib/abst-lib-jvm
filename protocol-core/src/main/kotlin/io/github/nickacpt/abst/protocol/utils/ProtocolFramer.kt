package io.github.nickacpt.abst.protocol.utils

import io.github.nickacpt.abst.protocol.Layer7Protocol
import org.msgpack.core.MessagePack
import org.msgpack.core.MessageUnpacker
import org.msgpack.core.buffer.MessageBuffer

/**
 * Helper class for framing messages in the protocol to be sent to the server
 */
object ProtocolFramer {

    /**
     * Helper method to frame a Byte Array message into a message buffer.
     *
     * @param bytes The message to frame.
     * @param protocol The protocol to use for framing.
     * @return The framed message as a MessageBuffer.
     */
    fun frame(protocol: Layer7Protocol, bytes: ByteArray): ByteArray {
        return MessagePack.newDefaultBufferPacker().use {
            it.packByte(protocol.id.toByte()) // Protocol number
            it.packBinaryHeader(bytes.size)
            it.addPayload(bytes)
            it.toByteArray()
        }
    }


    /**
     * Helper method to unframe a message from a message buffer.
     *
     * @param buffer The message buffer to unframe.
     * @return The unframed message as a byte array.
     */
    fun unframe(buffer: MessageBuffer): Pair<UByte, ByteArray> {
        return unframeMessage(MessagePack.newDefaultUnpacker(buffer.sliceAsByteBuffer()))
    }

    /**
     * Method to unframe a message from a MessageUnpacker.
     *
     * @param unpacker The message unpacker to use to read the message.
     * @return The unframed message as a byte array.
     */
    fun unframeMessage(unpacker: MessageUnpacker, canClose: Boolean = true): Pair<UByte, ByteArray> {
        val action: (MessageUnpacker) -> Pair<UByte, ByteArray> = {
            val protocolId = it.unpackByte().toUByte()
            val header = it.unpackBinaryHeader()
            protocolId to it.readPayload(header)
        }

        return (if (canClose) unpacker.use(action) else action(unpacker))
    }


}