package gr.cpaleop.core.data.model.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration(startVersion: Int = 2, endVersion: Int = 3) : Migration(startVersion, endVersion) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE 'document' ('uri' TEXT NOT NULL, " +
                    "'absolutePath' TEXT NOT NULL, " +
                    "'name' TEXT NOT NULL, " +
                    "'type' TEXT NOT NULL, " +
                    "'size' INTEGER NOT NULL, " +
                    "'previewUri' TEXT NOT NULL, " +
                    "'lastModified' INTEGER NOT NULL, " +
                    "PRIMARY KEY(`uri`))"
        )
    }
}