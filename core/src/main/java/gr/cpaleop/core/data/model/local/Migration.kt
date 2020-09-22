package gr.cpaleop.core.data.model.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration(startVersion: Int = 4, endVersion: Int = 5) : Migration(startVersion, endVersion) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE remoteannouncement ADD COLUMN text TEXT"
        )
    }
}