package gr.cpaleop.dashboard.data.mappers

import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.data.model.response.RemoteRegisteredCategory
import gr.cpaleop.dashboard.domain.entities.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryMapper {

    suspend operator fun invoke(
        remoteCategory: RemoteCategory,
        remoteRegisteredCategories: List<RemoteRegisteredCategory>
    ): Category? = withContext(Dispatchers.Default) {
        val isRegistered =
            remoteRegisteredCategories.find { it.id == remoteCategory.id }?.registered?.contains("true")
                ?: return@withContext null

        return@withContext Category(
            id = remoteCategory.id,
            name = remoteCategory.name ?: "",
            isRegistered = isRegistered
        )
    }
}