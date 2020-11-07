package gr.cpaleop.create_announcement.domain.entities

data class EmptyTitleException(override val message: String? = "Announcement title is empty") :
    Throwable()