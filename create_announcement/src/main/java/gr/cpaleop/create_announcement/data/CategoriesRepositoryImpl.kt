package gr.cpaleop.create_announcement.data

import gr.cpaleop.core.data.datasources.CategoriesDataSource
import gr.cpaleop.core.data.mappers.CategoryMapper
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.create_announcement.domain.repositories.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CategoriesRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val categoriesDataSource: CategoriesDataSource,
    private val categoryMapper: CategoryMapper
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(ioDispatcher) {
        categoriesDataSource.fetchCategories().map(categoryMapper::invoke)
    }

    override suspend fun getCategoryById(id: String): Category = withContext(ioDispatcher) {
        categoryMapper(categoriesDataSource.fetchCategoryById(id))
    }
}