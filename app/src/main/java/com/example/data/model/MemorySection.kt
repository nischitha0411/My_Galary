package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class MemorySection(
    val id: String,
    val title: String,
    val shortName: String,
    val tabColorHex: Long,
    val defaultSticker: String,
    val description: String
) {
    SPECIAL_MOMENTS(
        id = "special_moments",
        title = "Special Moments",
        shortName = "Special",
        tabColorHex = 0xFFC86D51,
        defaultSticker = "CHERISHED",
        description = "Milestones, surprises, and golden memories to hold dear."
    ),
    BIRTHDAY_MEMORIES(
        id = "birthday_memories",
        title = "Birthday Memories",
        shortName = "Birthdays",
        tabColorHex = 0xFFE07A5F,
        defaultSticker = "BIRTHDAY CHEER",
        description = "Candles, wishes, parties, and another year around the sun."
    ),
    COLLEGE_MEMORIES(
        id = "college_memories",
        title = "College Memories",
        shortName = "College",
        tabColorHex = 0xFF3D5A80,
        defaultSticker = "CAMPUS DAYS",
        description = "Dorm nights, campus strolls, study sessions, and graduations."
    ),
    FRIENDS(
        id = "friends",
        title = "Friends",
        shortName = "Friends",
        tabColorHex = 0xFFD4A359,
        defaultSticker = "★ BEST FRIENDS ★",
        description = "Road trips, inside jokes, and friends who feel like home."
    ),
    FAMILY(
        id = "family",
        title = "Family",
        shortName = "Family",
        tabColorHex = 0xFF2A9D8F,
        defaultSticker = "FAMILY LOVE",
        description = "Heartwarming gatherings, traditions, and family roots."
    ),
    PHOTOS(
        id = "photos",
        title = "Photos",
        shortName = "Photos",
        tabColorHex = 0xFF8338EC,
        defaultSticker = "SNAPSHOT",
        description = "A visual collage of polaroids, candid frames, and prints."
    ),
    IMPORTANT_DATES(
        id = "important_dates",
        title = "Important Dates",
        shortName = "Dates",
        tabColorHex = 0xFFD64045,
        defaultSticker = "SAVE THE DATE",
        description = "Anniversaries, milestone birthdays, and upcoming celebrations."
    );

    fun tabColor(): Color = Color(tabColorHex)

    companion object {
        fun fromId(id: String): MemorySection {
            return entries.firstOrNull { it.id == id } ?: SPECIAL_MOMENTS
        }
    }
}
