package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.FileOptionType

interface GetFileOptionsUseCase {

    operator fun invoke(): List<FileOptionType>
}