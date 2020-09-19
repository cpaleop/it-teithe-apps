package gr.cpaleop.core.data.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gr.cpaleop.core.data.model.local.converters.RemoteAttachmentsConverter
import gr.cpaleop.core.data.model.local.converters.RemotePublisherConverter
import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.domain.entities.Document

@Database(
    entities = [RemoteAnnouncement::class, RemoteCategory::class, Document::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    RemotePublisherConverter::class,
    RemoteAttachmentsConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun remoteAnnouncementsDao(): RemoteAnnouncementsDao

    abstract fun remoteCategoryDao(): RemoteCategoryDao

    abstract fun documentDao(): DocumentDao
}