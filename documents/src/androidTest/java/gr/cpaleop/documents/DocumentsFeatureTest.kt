package gr.cpaleop.documents

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import gr.cpaleop.documents.di.documentsModule
import gr.cpaleop.documents.domain.usecases.ObserveDocumentsAnnouncementFoldersUseCase
import gr.cpaleop.documents.domain.usecases.ObserveDocumentsUseCase
import gr.cpaleop.teithe_apps.di.coreModule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class DocumentsFeatureTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @MockK
    private lateinit var observeDocumentsUseCase: ObserveDocumentsUseCase

    @MockK
    private lateinit var observeDocumentsAnnouncementFoldersUseCase: ObserveDocumentsAnnouncementFoldersUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        observeDocumentsUseCase = mockk()
        observeDocumentsAnnouncementFoldersUseCase = mockk()
        startKoin {
            androidContext(context)
            modules(documentsModule, coreModule, module(override = true) {
                single { observeDocumentsUseCase }
                single { observeDocumentsAnnouncementFoldersUseCase }
            })
        }
    }
}