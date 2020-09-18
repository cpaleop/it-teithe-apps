package gr.cpaleop.dashboard.data

import androidx.paging.PagingSource
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.dashboard.data.mappers.AnnouncementMapper
import timber.log.Timber

class AnnouncementsPagingSource(
    private val announcementsApi: AnnouncementsApi,
    private val categoriesApi: CategoriesApi,
    private val appDatabase: AppDatabase,
    private val announcementMapper: AnnouncementMapper
) : PagingSource<Int, Announcement>() {

    private var categoryList = listOf<RemoteCategory>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Announcement> {
        val page = params.key ?: STARTING_PAGE
        return try {
            val remoteAnnouncementList = announcementsApi.fetchAnnouncements(PAGE_SIZE, page)
            if (categoryList.isEmpty()) {
                categoryList = categoriesApi.fetchCategories()
                appDatabase.remoteCategoryDao().insert(categoryList)
            }

            appDatabase.remoteAnnouncementsDao().insert(remoteAnnouncementList)
            val announcements =
                remoteAnnouncementList.mapAsyncSuspended { announcementMapper(it, categoryList) }
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

    companion object {

        const val PAGE_SIZE = 35
        private const val STARTING_PAGE = 1
    }
}