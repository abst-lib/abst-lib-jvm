package io.github.nickacpt.abst

import io.github.nickacpt.abst.io.sockets.client.TcpClient
import io.github.nickacpt.abst.io.sockets.server.TcpClientConnection
import io.github.nickacpt.abst.io.sockets.server.TcpServer
import kotlin.concurrent.thread
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun main() {
    val port: UShort = 12301u
    val server = object : TcpServer("localhost", port) {
        override fun handleClientMessage(connection: TcpClientConnection, message: ByteArray) {
            println("Received message from ${connection.remoteAddress}: ${String(message)}")
        }

        override fun handleClientDisconnect(connection: TcpClientConnection) {
            println("Client disconnected from ${connection.remoteAddress}")
        }
    }

    server.start()
    println("Server started on port $port")

    val client = object : TcpClient("localhost", port) {
        override fun onMessageReceived(message: ByteArray) {
            println("Received message: ${String(message)}")
        }

        override fun onConnectionClosed() {
            println("Connection closed")
        }
    }

    client.connect()
    while (!client.isConnected) {
        Thread.sleep(5)
    }
    println("Connected to server")

    repeat(5) {
        client.sendMessage("Hello World - $it!".toByteArray())
    }

    readLine()

    client.disconnect()
    server.stop()

}