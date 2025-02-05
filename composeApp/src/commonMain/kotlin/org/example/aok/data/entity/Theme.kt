package org.example.aok.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theme_preference")
data class ThemePreference(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val theme: String,
    var active: Boolean,
    var dark: Boolean,
    val system: Boolean
)