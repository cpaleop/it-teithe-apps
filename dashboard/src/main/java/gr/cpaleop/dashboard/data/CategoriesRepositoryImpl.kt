package gr.cpaleop.dashboard.data

import gr.cpaleop.core.data.remote.CategoriesApi
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CategoriesRepositoryImpl(
    private val json: Json,
    private val categoriesApi: CategoriesApi,
    private val categoriesRepository: gr.cpaleop.core.domain.repositories.CategoriesRepository
) : CategoriesRepository {

    override suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        categoriesRepository.getRegisteredCategories()
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