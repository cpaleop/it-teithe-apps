package gr.cpaleop.network.connection

import java.io.IOException

/**
 * This class defines a no internet connection exception.
 */
class NoConnectionException(message: String = "") : IOException(message)