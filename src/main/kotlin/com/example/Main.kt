package com.example

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

fun main() {
    val port = System.getenv("SERVER_PORT")?.toInt() ?: DEFAULT_PORT
    val server = HttpServer.create(InetSocketAddress(port), SOCKET_BACKLOG)
    server.createContext("/", DefaultHandler())
    server.executor = null // creates a default executor
    println("Serving on port $port..")
    server.start()
}

class DefaultHandler : HttpHandler {
    override fun handle(exchange: HttpExchange) {
        val response = "Hello World"

        // Sending response
        exchange.sendResponseHeaders(HTTP_STATUS_OK, response.toByteArray().size.toLong())
        exchange.responseBody.use { it.write(response.toByteArray()) }
    }
}

const val HTTP_STATUS_OK = 200
const val DEFAULT_PORT = 9000
const val SOCKET_BACKLOG = 0 // Zero means that the system default value is used.
