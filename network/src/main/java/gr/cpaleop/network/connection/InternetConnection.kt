package gr.cpaleop.network.connection

import gr.cpaleop.network.connection.types.Mobile
import gr.cpaleop.network.connection.types.Wifi

/**
 * Concrete implementation of [Connection]. It defines an Internet connection, either from
 * mobile data or WiFi.
 */
class InternetConnection(
    @Wifi private val wifiConnection: Connection,
    @Mobile private val mobileConnection: Connection
) : Connection {

    override val isConnected: Boolean
        get() = wifiConnection.isConnected || mobileConnection.isConnected
}
