package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.create_announcement.domain.entities.Attachment

interface GetSelectedAttachmentsUseCase {

    operator fun invoke(): List<Attachment>
}