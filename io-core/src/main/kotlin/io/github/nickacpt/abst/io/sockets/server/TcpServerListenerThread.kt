package io.github.nickacpt.abst.io.sockets.server

class TcpServerListenerThread(private val server: TcpServer) : Runnable {
    override fun run() {
        while (server.isRunning) {
            try {
                val client = server.serverSocket.accept()
                val clientConnection = TcpClientConnection(server, client, server.disconnectOnError)
                server.handleClientConnection(clientConnection)
            } catch (e: Exception) {
                if (server.isRunning) server.onError(e)
            }
        }
    }
}