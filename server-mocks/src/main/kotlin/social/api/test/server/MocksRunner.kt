package social.api.test.server

import java.io.IOException

val DEFAULT_BASE_PATH = "http://127.0.0.1:8080"

@Throws(IOException::class)
fun main(args: Array<String>) {
    val server = MocksServer(DEFAULT_BASE_PATH).start()
    System.`in`.read()
    server.shutdown()
}
