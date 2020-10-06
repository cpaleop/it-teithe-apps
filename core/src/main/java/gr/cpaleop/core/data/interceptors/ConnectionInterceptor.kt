package gr.cpaleop.core.data.interceptors

import gr.cpaleop.core.connection.Connection
import gr.cpaleop.core.connection.NoConnectionException
import gr.cpaleop.core.connection.types.Internet
import okhttp3.Interceptor
import okhttp3.Response

class ConnectionInterceptor(@Internet private val connection: Connection) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connection.isConnected) throw NoConnectionException("No internet connection")
        return chain.proceed(chain.request())
    }
}