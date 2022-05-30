package io.github.nickacpt.abst.io.sockets.endpoint

import org.msgpack.core.MessagePacker
import org.msgpack.core.MessageUnpacker
import java.io.InputStream
import java.io.OutputStream

internal interface InternalConnectionEndpoint {

    /**
     * The input stream of this endpoint.
     * This is used to read data from the endpoint.
     */
    val inputStream: InputStream

    /**
     * The output stream of this endpoint.
     * This is used to write data to the endpoint.
     */
    val outputStream: OutputStream

    /**
     * The message packer of this endpoint.
     */
    val messagePacker: MessagePacker

    /**
     * The message unpacker of this endpoint.
     */
    val messageUnpacker: MessageUnpacker
}