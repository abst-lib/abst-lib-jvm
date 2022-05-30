package io.github.nickacpt.abst.io.sockets.endpoint

import io.github.nickacpt.abst.io.utils.BufferFramer
import org.msgpack.core.MessagePack
import org.msgpack.core.MessageUnpacker

internal class TcpEndpointReceiverThread(private val endpoint: ConnectionEndpoint) : Runnable {

    private val unpacker: MessageUnpacker = MessagePack.newDefaultUnpacker(endpoint.inputStream)

    override fun run() {
        while (endpoint.isConnected) {
            try {
                if (!unpacker.hasNext()) {
                    continue
                }
                // While the client is connected, read from the socket and give the data to the client for processing
                val message = BufferFramer.unframeMessage(unpacker, false)
                endpoint.onMessageReceived(message)
            } catch (e: Exception) {
                if (true) {//if (endpoint.isConnected) {
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