package gr.cpaleop.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gr.cpaleop.core.data.local.converters.RemoteAttachmentsConverter
import gr.cpaleop.core.data.local.converters.RemotePublisherConverter
import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteCategory

@Database(
    entities = [RemoteAnnouncement::class, RemoteCategory::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    RemotePublisherConverter::class,
    RemoteAttachmentsConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun remoteAnnouncementsDao(): RemoteAnnouncementsDao

    abstract fun remoteCategoryDao(): RemoteCategoryDao
}