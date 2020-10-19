package gr.cpaleop.public_announcements.domain.usecases

import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class FakeObservePublicAnnouncementsUseCaseImpl : ObservePublicAnnouncementsUseCase {

    var emptyList = false

    private val filterStateFlow = MutableStateFlow("")

    override suspend fun invoke(): Flow<List<Announcement>> {
        return flow {
            val list = if (emptyList) announcementList
            else announcementList
            emit(list)
        }.combine(filterStateFlow) { announcementList, query ->
            if (query.isEmpty()) return@combine announcementList
            return@combine announcementList.filter { announcement ->
                announcement.title.contains(query, ignoreCase = true) ||
                        announcement.text.contains(query, true)
            }
        }
    }

    override fun filter(filterQuery: String) {
        filterStateFlow.value = filterQuery
    }

    companion object {

        val announcementListEmpty = emptyList<Announcement>()
        val announcementList = listOf(
            Announcement(
                id = "id1",
                category = Category(
                    id = "category_id1",
                    name = "category_name1",
                    isRegistered = false
                ),
                publisherName = "publisher_name1",
                title = "title1",
                text = "text1",
                date = "2018-10-04T18:52:22.529Z",
                attachments = listOf("attachment11", "attachment12")
            ),
            Announcement(
                id = "id2",
                category = Category(
                    id = "category_id2",
                    name = "category_name2",
                    isRegistered = false
                ),
                publisherName = "publisher_name2",
                title = "title2",
                text = "text2",
                date = "2018-10-04T18:52:22.529Z",
                attachments = listOf("attachment21", "attachment22")
            )
        )
    }
}