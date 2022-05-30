package io.github.nickacpt.abst.io.sockets.endpoint

import io.github.nickacpt.abst.io.utils.BufferFramer
import org.msgpack.core.MessagePack
import org.msgpack.core.MessageUnpacker
import org.msgpack.core.buffer.InputStreamBufferInput

internal class TcpEndpointReceiverThread(private val endpoint: ConnectionEndpoint) : Runnable {

    private val inputStreamBuffer = InputStreamBufferInput(endpoint.inputStream)
    private val unpacker: MessageUnpacker = MessagePack.newDefaultUnpacker(inputStreamBuffer)

    override fun run() {
        while (endpoint.isConnected) {
            try {
                if (!unpacker.hasNext()) {
                    Thread.sleep(1)
                    continue
                }
                // While the endpoint is connected, read from the socket and give the data to it for processing
                val message = BufferFramer.unframeMessage(unpacker, false)
                endpoint.onMessageReceived(message)
                unpacker.reset(inputStreamBuffer)

            } catch (e: Exception) {
                if (endpoint.isConnected) {
                    endpoint.onError(e)
                    if (endpoint.disconnectOnError) {
                        // If an exception is thrown, the client is disconnected if the disconnectOnError flag is set
                        endpoint.disconnect()
                    }
                }
            }
        }

        unpacker.close()
        endpoint.onConnectionClosed()
    }
}