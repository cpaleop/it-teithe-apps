package gr.cpaleop.core.data.mappers

import gr.cpaleop.core.datasource.model.response.RemoteCategory
import gr.cpaleop.core.domain.entities.Category

class CategoryMapper {

    operator fun invoke(remoteCategory: RemoteCategory): Category {
        return Category(
            id = remoteCategory.id,
            name = remoteCategory.name ?: "",
            isRegistered = false
        )
    }
}