package io.github.nickacpt.abst.io.utils

import org.msgpack.core.MessagePack
import org.msgpack.core.MessageUnpacker
import org.msgpack.core.buffer.MessageBuffer

/**
 * Helper object for framing and unframing of messages (as a byte array) using MessagePack.
 */
object BufferFramer {

    /**
     * Helper method to frame a Byte Array message into a message buffer.
     *
     * @param bytes The message to frame.
     * @return The framed message as a MessageBuffer.
     */
    fun frame(bytes: ByteArray): MessageBuffer {
        return MessagePack.newDefaultBufferPacker().use {
            it.packBinaryHeader(bytes.size)
            it.writePayload(bytes)
            it.toMessageBuffer()
        }
    }

    /**
     * Helper method to unframe a message from a message buffer.
     *
     * @param buffer The message buffer to unframe.
     * @return The unframed message as a byte array.
     */
    fun unframe(buffer: MessageBuffer): ByteArray {
        return unframeMessage(MessagePack.newDefaultUnpacker(buffer.sliceAsByteBuffer()))
    }

    /**
     * Method to unframe a message from a MessageUnpacker.
     *
     * @param unpacker The message unpacker to use to read the message.
     * @return The unframed message as a byte array.
     */
    fun unframeMessage(unpacker: MessageUnpacker, canClose: Boolean = true): ByteArray {
        val action: (MessageUnpacker) -> ByteArray = {
            val header = it.unpackBinaryHeader()
            it.readPayload(header)
        }

        return if (canClose) unpacker.use(action) else action(unpacker)
    }
}