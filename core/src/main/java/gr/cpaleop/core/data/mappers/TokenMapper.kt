package gr.cpaleop.core.data.mappers

import gr.cpaleop.core.data.model.response.RemoteToken
import gr.cpaleop.core.domain.entities.Token

class TokenMapper {

    operator fun invoke(remoteToken: RemoteToken): Token {
        return Token(
            accessToken = remoteToken.accessToken
                ?: throw IllegalArgumentException("Access token is null"),
            refreshToken = remoteToken.refreshToken
                ?: throw IllegalArgumentException("Access token is null")
        )
    }
}