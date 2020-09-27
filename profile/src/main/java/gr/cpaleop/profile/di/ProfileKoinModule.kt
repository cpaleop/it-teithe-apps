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
import gr.cpaleop.profile.domain.usecases.GetProfileUseCase
import gr.cpaleop.profile.domain.usecases.GetProfileUseCaseImpl
import gr.cpaleop.profile.domain.usecases.UpdateSocialUseCase
import gr.cpaleop.profile.domain.usecases.UpdateSocialUseCaseImpl
import gr.cpaleop.profile.presentation.ProfilePresentationMapper
import gr.cpaleop.profile.presentation.ProfilePresentationMapperImpl
import gr.cpaleop.profile.presentation.ProfileViewModel
import gr.cpaleop.profile.presentation.options.SelectedSocialOptionMapper
import gr.cpaleop.profile.presentation.options.SelectedSocialOptionMapperImpl
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
            get()
        )
    }
    single { ProfileMapper(get()) }
    single<SelectedSocialOptionMapper> { SelectedSocialOptionMapperImpl() }
    single<ProfilePresentationMapper> {
        ProfilePresentationMapperImpl(
            get(),
            get(named<DefaultDispatcher>())
        )
    }
    single<UpdateSocialUseCase> { UpdateSocialUseCaseImpl(get()) }
    single<GetProfileUseCase> { GetProfileUseCaseImpl(get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get(named<IODispatcher>()), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single { provideProfileApi(get()) }
}

private fun provideProfileApi(retrofit: Retrofit): ProfileApi {
    return retrofit.create(ProfileApi::class.java)
}