package gr.cpaleop.network.interceptors

import gr.cpaleop.network.connection.Connection
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.network.connection.types.Internet
import okhttp3.Interceptor
import okhttp3.Response

class ConnectionInterceptor(@Internet private val connection: Connection) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connection.isConnected) throw NoConnectionException("No internet connection")
        return chain.proceed(chain.request())
    }
}