package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val section: String,
    val note: String,
    val dateString: String,
    val dateMillis: Long,
    val photoUri: String? = null,
    val photoPreset: String? = null,
    val tapeStyle: String = "kraft",
    val stickerText: String = "CHERISHED",
    val rotationDegrees: Float = 0f,
    val isFavorite: Boolean = false,
    val peopleOrLocation: String? = null,
    val isMilestoneDate: Boolean = false,
    val reminderMonth: Int = 1,
    val reminderDay: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
