package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.FileOptionType

class GetFileOptionsUseCaseImpl : GetFileOptionsUseCase {

    override fun invoke(): List<FileOptionType> {
        return listOf(
            FileOptionType.ANNOUNCEMENT,
            FileOptionType.RENAME,
            FileOptionType.DELETE,
            FileOptionType.SHARE,
            FileOptionType.INFO
        )
    }
}