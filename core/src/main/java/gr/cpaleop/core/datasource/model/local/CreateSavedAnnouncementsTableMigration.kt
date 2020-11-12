package gr.cpaleop.core.datasource.model.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class CreateSavedAnnouncementsTableMigration(startVersion: Int = 5, endVersion: Int = 6) :
    Migration(startVersion, endVersion) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `SavedAnnouncement` (announcementId TEXT NOT NULL, dateAdded INTEGER NOT NULL, PRIMARY KEY(`announcementId`))"
        )
    }
}