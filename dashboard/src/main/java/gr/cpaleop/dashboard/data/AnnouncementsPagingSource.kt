package gr.cpaleop.dashboard.data

import androidx.paging.PagingSource
import com.google.gson.Gson
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.data.mappers.AnnouncementMapper
import gr.cpaleop.dashboard.data.model.remote.RemoteAnnouncementTextFilter
import gr.cpaleop.dashboard.data.model.remote.RemoteAnnouncementTitleFilter
import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import gr.cpaleop.dashboard.domain.entities.AnnouncementSortType
import timber.log.Timber

class AnnouncementsPagingSource(
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper,
    private val filterQuery: String?,
    private val announcementsSort: AnnouncementSort?,
    private val gson: Gson
) : PagingSource<Int, Announcement>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Announcement> {
        val page = params.key ?: STARTING_PAGE
        return try {
            val sort = getSort(announcementsSort)
            val remoteAnnouncementList =
                if (filterQuery?.isNotEmpty() == true || filterQuery?.isBlank() == false) {
                    val textFilter = parseTextFilter(filterQuery)
                    val titleFilter = parseTitleFilter(filterQuery)

                    val announcementTextFiltered =
                        announcementsApi.fetchAnnouncementsFiltered(
                            textFilter,
                            PAGE_SIZE,
                            page,
                            sort
                        )
                    val announcementTitleFiltered =
                        announcementsApi.fetchAnnouncementsFiltered(
                            titleFilter,
                            PAGE_SIZE,
                            page,
                            sort
                        )

                    listOf(announcementTextFiltered, announcementTitleFiltered).flatten()
                } else {
                    announcementsApi.fetchAnnouncements(PAGE_SIZE, page, sort)
                }

            appDatabase.remoteAnnouncementsDao().insert(remoteAnnouncementList)

            var localCategories = appDatabase.remoteCategoryDao().getAll()
            if (localCategories.isEmpty()) {
                localCategories = categoriesApi.fetchCategories()
                appDatabase.remoteCategoryDao().insert(localCategories)
            }

            val announcements =
                remoteAnnouncementList.mapAsyncSuspended { announcementMapper(it, localCategories) }
            LoadResult.Page(
                data = announcements,
                prevKey = if (page == STARTING_PAGE) null else page - 1,
                nextKey = if (announcements.isEmpty()) null else page + 1
            )
        } catch (t: Throwable) {
            Timber.e(t)
            LoadResult.Error(t)
        }
    }

    private fun getSort(announcementsSort: AnnouncementSort?): String {
        if (announcementsSort == null) return "-date"
        val field = when (announcementsSort.type) {
            AnnouncementSortType.DATE -> "date"
            AnnouncementSortType.TITLE -> "title"
            else -> throw java.lang.IllegalArgumentException("No sort type found with value ${announcementsSort.type}")
        }

        val descendingPrefix = when (announcementsSort.descending) {
            true -> "-"
            false -> "+"
        }

        return "$descendingPrefix$field"
    }

    private fun parseTextFilter(filterQuery: String): String {
        val remoteAnnouncementTextFilter = RemoteAnnouncementTextFilter(filterQuery)
        return gson.toJson(remoteAnnouncementTextFilter)
    }

    private fun parseTitleFilter(filterQuery: String): String {
        val remoteAnnouncementTitleFilter = RemoteAnnouncementTitleFilter(filterQuery)
        return gson.toJson(remoteAnnouncementTitleFilter)
    }

    companion object {

        const val PAGE_SIZE = 35
        private const val STARTING_PAGE = 0
    }
}