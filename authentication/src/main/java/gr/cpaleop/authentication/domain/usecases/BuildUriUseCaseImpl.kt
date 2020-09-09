package gr.cpaleop.authentication.domain.usecases

import gr.cpaleop.authentication.BuildConfig
import gr.cpaleop.core.domain.Scope

class BuildUriUseCaseImpl : BuildUriUseCase {

    override fun invoke(loginUrl: String, clientId: String): String {
        return loginUrl +
                "authorization/" +
                "?client_id=${clientId}" +
                "&response_type=code" +
                "&scope=${Scope.ANNOUNCEMENTS}," +
                "${Scope.EDIT_MAIL}," +
                "${Scope.EDIT_PASSWORD}," +
                "${Scope.EDIT_PROFILE}," +
                "${Scope.NOTIFICATIONS}," +
                "${Scope.PROFILE}," +
                Scope.REFRESH_TOKEN +
                "&redirect_uri=${BuildConfig.CALLBACK_URL}"
    }
}