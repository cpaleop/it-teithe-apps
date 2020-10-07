package gr.cpaleop.profile.di

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.profile.data.PreferencesRepositoryImpl
import gr.cpaleop.profile.data.ProfileMapper
import gr.cpaleop.profile.data.ProfileRepositoryImpl
import gr.cpaleop.profile.data.remote.ProfileApi
import gr.cpaleop.profile.domain.repositories.PreferencesRepository
import gr.cpaleop.profile.domain.repositories.ProfileRepository
import gr.cpaleop.profile.domain.usecases.*
import gr.cpaleop.profile.presentation.ProfilePresentationMapper
import gr.cpaleop.profile.presentation.ProfilePresentationMapperImpl
import gr.cpaleop.profile.presentation.ProfileViewModel
import gr.cpaleop.profile.presentation.options.OptionDataMapper
import gr.cpaleop.profile.presentation.options.SelectedSocialOptionMapper
import gr.cpaleop.profile.presentation.options.SelectedSocialOptionMapperImpl
import gr.cpaleop.profile.presentation.settings.LanguageMapper
import gr.cpaleop.profile.presentation.settings.ThemeMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val profileModule = module {
    viewModel {
        ProfileViewModel(
            get(named<MainDispatcher>()),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single { LanguageMapper(get()) }
    single { ThemeMapper() }
    single { OptionDataMapper() }
    single { ProfileMapper(get()) }
    single<SelectedSocialOptionMapper> { SelectedSocialOptionMapperImpl() }
    single<ProfilePresentationMapper> {
        ProfilePresentationMapperImpl(
            get(),
            get(named<DefaultDispatcher>())
        )
    }
    single<GetPreferredLanguageUseCase> { GetPreferredLanguageUseCaseImpl(get()) }
    single<LogoutUseCase> { LogoutUseCaseImpl(get()) }
    single<UpdatePreferredThemeUseCase> { UpdatePreferredThemeUseCaseImpl(get()) }
    single<ObservePreferredThemeUseCase> { ObservePreferredThemeUseCaseImpl(get()) }
    single<UpdatePersonalDetailsUseCase> { UpdatePersonalDetailsUseCaseImpl(get()) }
    single<UpdateSocialUseCase> { UpdateSocialUseCaseImpl(get()) }
    single<GetProfileUseCase> { GetProfileUseCaseImpl(get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get(named<IODispatcher>()), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(named<IODispatcher>()), get(), get()) }
    single { provideProfileApi(get()) }
}

private fun provideProfileApi(retrofit: Retrofit): ProfileApi {
    return retrofit.create(ProfileApi::class.java)
}