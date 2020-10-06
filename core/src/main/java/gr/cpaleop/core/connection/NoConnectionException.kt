package gr.cpaleop.core.connection

import java.io.IOException

/**
 * This class defines a no internet connection exception.
 */
class NoConnectionException(message: String = "") : IOException(message)