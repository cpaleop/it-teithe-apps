package gr.cpaleop.core.connection

import android.content.Context
import android.net.ConnectivityManager

/**
 * Concrete implementation of [Connection]. It defines a WiFi connection.
 */
class WifiConnection(private val context: Context) : Connection {

    override val isConnected: Boolean
        get() {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI
        }
}