package io.github.nickacpt.abst.io.sockets.server

class TcpServerListenerThread<C : TcpClientConnection>(private val server: TcpServer<C>) : Runnable {
    override fun run() {
        while (server.isRunning) {
            try {
                val client = server.serverSocket.accept()
                val clientConnection = server.createClientConnection(client)
                server.handleClientConnection(clientConnection)
            } catch (e: Exception) {
                if (server.isRunning) server.onError(e)
            }
        }
    }
}