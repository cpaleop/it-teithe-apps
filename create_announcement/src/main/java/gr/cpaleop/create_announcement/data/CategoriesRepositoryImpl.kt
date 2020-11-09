package gr.cpaleop.create_announcement.data

import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.mappers.CategoryMapper
import gr.cpaleop.core.data.model.local.RemoteCategoryDao
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.create_announcement.domain.repositories.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CategoriesRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val remoteCategoryDao: RemoteCategoryDao,
    private val categoryMapper: CategoryMapper
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(ioDispatcher) {
        remoteCategoryDao.fetchAll().mapAsyncSuspended(categoryMapper::invoke)
    }

    override suspend fun getCategoryById(id: String): Category = withContext(ioDispatcher) {
        categoryMapper(remoteCategoryDao.fetchFromId(id))
    }
}