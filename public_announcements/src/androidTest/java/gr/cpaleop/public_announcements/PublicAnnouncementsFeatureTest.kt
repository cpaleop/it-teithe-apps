package gr.cpaleop.public_announcements

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.public_announcements.di.PublicAnnouncementsScope
import gr.cpaleop.public_announcements.di.publicAnnouncementsModule
import gr.cpaleop.public_announcements.domain.usecases.FakeObservePublicAnnouncementsUseCaseImpl
import gr.cpaleop.public_announcements.domain.usecases.ObservePublicAnnouncementsUseCase
import gr.cpaleop.public_announcements.presentation.PublicAnnouncementsFragment
import gr.cpaleop.teithe_apps.di.coreModule
import gr.cpaleop.teithe_apps.di.networkModule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
@MediumTest
class PublicAnnouncementsFeatureTest : KoinTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var observePublicAnnouncementsUseCase: ObservePublicAnnouncementsUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        observePublicAnnouncementsUseCase = mockk()
        stopKoin()
        startKoin {
            androidContext(context)
            modules(publicAnnouncementsModule, coreModule, networkModule, module(override = true) {
                scope(named<PublicAnnouncementsScope>()) {
                    scoped(override = true) { observePublicAnnouncementsUseCase }
                }
            })
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun showsCorrectValues_whenQueryIsProvided() {
        //Given
        val announcementsFlow = flow {
            emit(FakeObservePublicAnnouncementsUseCaseImpl.announcementList)
        }
        val givenQuery = "2"
        val givenFlow =
            announcementsFlow.combine(MutableStateFlow(givenQuery)) { announcementList, query ->
                if (query.isEmpty()) return@combine announcementList
                return@combine announcementList.filter { announcement ->
                    announcement.title.contains(query, ignoreCase = true) ||
                            announcement.text.contains(query, ignoreCase = true)
                }
            }
        every { observePublicAnnouncementsUseCase.filter(givenQuery) } returns Unit
        coEvery { observePublicAnnouncementsUseCase() } returns givenFlow

        launchFragmentInContainer<PublicAnnouncementsFragment>(themeResId = appR.style.Theme_Itteitheapps)

        val actionsRecyclerViewMatcher =
            RecyclerViewMatcher(R.id.publicAnnouncementsRecyclerView)

        //When
        onView(withId(R.id.publicAnnnouncementsSearchTextView)).perform(replaceText(givenQuery))
        runBlocking { delay(500) }

        //Then
        onView(actionsRecyclerViewMatcher.atPositionOnView(0, R.id.publicAnnouncementTitle))
            .check(matches(withText("title2")))
        onView(actionsRecyclerViewMatcher.atPositionOnView(0, R.id.publicAnnouncementContent))
            .check(matches(withText("text2")))
        onView(actionsRecyclerViewMatcher.atPositionOnView(0, R.id.publicAnnouncementCategory))
            .check(matches(withText("category_name2")))
        onView(actionsRecyclerViewMatcher.atPositionOnView(0, R.id.publicAnnouncementPublisher))
            .check(matches(withText("publisher_name2")))
    }

    @Test
    fun showsCorrectValues_whenListIsEmpty() {
        //Given
        //When
        coEvery { observePublicAnnouncementsUseCase() } returns flow { emit(emptyList<Announcement>()) }

        //Then
        launchFragmentInContainer<PublicAnnouncementsFragment>(themeResId = appR.style.Theme_Itteitheapps)
        onView(withId(R.id.publicAnnouncementsEmptyTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun showsGenericErrorMessage_whenUsecaseThrowsThrowable() {
        //Given
        //When
        coEvery { observePublicAnnouncementsUseCase() } throws Throwable()

        //Given
        //When
        launchFragmentInContainer<PublicAnnouncementsFragment>(themeResId = appR.style.Theme_Itteitheapps)
        coEvery { observePublicAnnouncementsUseCase() } throws Throwable()

        //Then
        onView(withText(appR.string.error_generic)).check(matches(isDisplayed()))
    }

    @Test
    fun showsNoInternetMessage_whenNoInternetConnection() {
        //Given
        //When
        coEvery { observePublicAnnouncementsUseCase() } throws NoConnectionException()

        //Then
        launchFragmentInContainer<PublicAnnouncementsFragment>(themeResId = appR.style.Theme_Itteitheapps)
        onView(withText(appR.string.error_no_internet_connection)).check(matches(isDisplayed()))
    }
}