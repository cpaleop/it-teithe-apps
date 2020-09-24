package gr.cpaleop.authentication.domain.usecases

import gr.cpaleop.authentication.BuildConfig
import gr.cpaleop.core.domain.Scope

class BuildUriUseCaseImpl : BuildUriUseCase {

    override fun invoke(): String {
        return BuildConfig.LOGIN_URL +
                "authorization/" +
                "?client_id=${BuildConfig.CLIENT_ID}" +
                "&response_type=code" +
                "&scope=${Scope.ANNOUNCEMENTS}," +
                "${Scope.EDIT_MAIL}," +
                "${Scope.EDIT_PASSWORD}," +
                "${Scope.EDIT_PROFILE}," +
                "${Scope.EDIT_NOTIFICATIONS}," +
                "${Scope.NOTIFICATIONS}," +
                "${Scope.PROFILE}," +
                Scope.REFRESH_TOKEN +
                "&redirect_uri=${BuildConfig.CALLBACK_URL}"
    }
}