package gr.cpaleop.announcements.data

import gr.cpaleop.announcements.domain.repositories.CategoriesRepository
import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CategoriesRepositoryImpl(
    private val json: Json,
    private val categoriesApi: CategoriesApi,
    private val categoriesRepository: gr.cpaleop.core.domain.repositories.CategoriesRepository
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        categoriesRepository.getCategories()
    }

    override suspend fun getCategoriesFlow(): Flow<List<Category>> = withContext(Dispatchers.IO) {
        categoriesRepository.getCategoriesFlow()
    }

    override suspend fun updateRegisteredCategories(
        registeredCategories: List<String>,
        nonRegisteredCategories: List<String>
    ) = withContext(Dispatchers.IO) {
        val registeredCategoriesString = json.encodeToString(registeredCategories)
        val nonegisteredCategoriesString = json.encodeToString(nonRegisteredCategories)
        categoriesApi.updateRegisteredCategories(
            registeredCategoriesString,
            nonegisteredCategoriesString
        )
    }
}