package gr.cpaleop.teithe_apps.di

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.upload.data.AnnouncementsRepositoryImpl
import gr.cpaleop.upload.domain.behavior.UploadProgressNotifier
import gr.cpaleop.upload.domain.repositories.AnnouncementsRepository
import gr.cpaleop.upload.domain.usecases.CreateAnnouncementUseCase
import gr.cpaleop.upload.domain.usecases.CreateAnnouncementUseCaseImpl
import gr.cpaleop.upload.presentation.UploadNotificationManager
import gr.cpaleop.upload.presentation.UploadNotificationManagerImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.qualifier.named
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val uploadKoinModule = module {
    single { UploadProgressNotifier() }
    single<UploadNotificationManager> { UploadNotificationManagerImpl(get()) }
    single<CreateAnnouncementUseCase> { CreateAnnouncementUseCaseImpl(get()) }
    single<AnnouncementsRepository> {
        AnnouncementsRepositoryImpl(
            get(named<IODispatcher>()),
            get(),
            get()
        )
    }
}