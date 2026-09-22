package com.example.data.repository

import com.example.data.local.MemoryDao
import com.example.data.model.MemoryItem
import com.example.data.model.MemorySection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Calendar

class MemoryRepository(private val dao: MemoryDao) {

    fun getAllMemories(): Flow<List<MemoryItem>> = dao.getAllMemories()

    fun getMemoriesBySection(section: String): Flow<List<MemoryItem>> {
        return if (section == MemorySection.PHOTOS.id) {
            dao.getAllPhotoMemories()
        } else if (section == MemorySection.IMPORTANT_DATES.id) {
            dao.getImportantDates()
        } else {
            dao.getMemoriesBySection(section)
        }
    }

    fun getFavoriteMemories(): Flow<List<MemoryItem>> = dao.getFavoriteMemories()

    suspend fun insertMemory(memory: MemoryItem): Long = withContext(Dispatchers.IO) {
        dao.insertMemory(memory)
    }

    suspend fun updateMemory(memory: MemoryItem) = withContext(Dispatchers.IO) {
        dao.updateMemory(memory)
    }

    suspend fun deleteMemory(memory: MemoryItem) = withContext(Dispatchers.IO) {
        dao.deleteMemory(memory)
    }

    suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }

    suspend fun toggleFavorite(memory: MemoryItem) = withContext(Dispatchers.IO) {
        dao.updateMemory(memory.copy(isFavorite = !memory.isFavorite))
    }

    suspend fun ensureInitialSeed() = withContext(Dispatchers.IO) {
        if (dao.getCount() == 0) {
            dao.insertAll(getInitialSeedMemories())
        }
    }

    private fun getInitialSeedMemories(): List<MemoryItem> {
        val now = System.currentTimeMillis()
        val day = 86_400_000L

        return listOf(
            // SPECIAL MOMENTS
            MemoryItem(
                title = "Sunrise at Driftwood Beach",
                section = MemorySection.SPECIAL_MOMENTS.id,
                note = "Woke up at 5:00 AM with hot cinnamon cider in thermoses. The horizon turned shades of peach, lavender, and honey gold that took our breath away.",
                dateString = "August 14, 2025",
                dateMillis = now - (35 * day),
                photoPreset = "friends",
                tapeStyle = "gold",
                stickerText = "★ BEST DAY EVER ★",
                rotationDegrees = -2.2f,
                isFavorite = true,
                peopleOrLocation = "Driftwood Cove, Pacific Coast"
            ),
            MemoryItem(
                title = "Keys to the First Apartment!",
                section = MemorySection.SPECIAL_MOMENTS.id,
                note = "Sitting on the bare wooden floor with takeout cheese pizza and two plastic cups, celebrating the start of an unforgettable new chapter.",
                dateString = "June 3, 2025",
                dateMillis = now - (105 * day),
                photoPreset = "cover",
                tapeStyle = "kraft",
                stickerText = "CHERISHED",
                rotationDegrees = 1.8f,
                isFavorite = false,
                peopleOrLocation = "Apartment 3B, Elm Street"
            ),

            // BIRTHDAY MEMORIES
            MemoryItem(
                title = "Surprise 25th Birthday Bash",
                section = MemorySection.BIRTHDAY_MEMORIES.id,
                note = "Everyone hid behind the living room velvet curtains in total darkness! The glitter confetti cannons took three weeks to sweep, but worth every single second.",
                dateString = "May 19, 2025",
                dateMillis = now - (120 * day),
                photoPreset = "birthday",
                tapeStyle = "coral",
                stickerText = "BIRTHDAY CHEER",
                rotationDegrees = 2.4f,
                isFavorite = true,
                peopleOrLocation = "Home with best friends"
            ),
            MemoryItem(
                title = "Grandpa's 80th Garden Picnic",
                section = MemorySection.BIRTHDAY_MEMORIES.id,
                note = "We hung fairy lights in the old weeping willow. All four generations wore vintage suspenders and sang old tunes around the lemonade stand.",
                dateString = "September 2, 2025",
                dateMillis = now - (18 * day),
                photoPreset = "birthday",
                tapeStyle = "sage",
                stickerText = "PURE JOY",
                rotationDegrees = -1.6f,
                isFavorite = false,
                peopleOrLocation = "Willow Creek Orchard"
            ),

            // COLLEGE MEMORIES
            MemoryItem(
                title = "Graduation Cap Toss on the Quad",
                section = MemorySection.COLLEGE_MEMORIES.id,
                note = "Four years of late-night study marathons, endless diner coffee, and soul connections that will never fade. We threw our caps and cried happy tears.",
                dateString = "May 28, 2025",
                dateMillis = now - (112 * day),
                photoPreset = "friends",
                tapeStyle = "sky",
                stickerText = "CAMPUS DAYS",
                rotationDegrees = -2.0f,
                isFavorite = true,
                peopleOrLocation = "North Quadrangle Lawn"
            ),
            MemoryItem(
                title = "2 AM Diner Runs Before Finals",
                section = MemorySection.COLLEGE_MEMORIES.id,
                note = "Sharing tall stacks of blueberry pancakes smothered in maple syrup while quizzing each other on flashcards. Laughed so hard our sides hurt.",
                dateString = "December 12, 2024",
                dateMillis = now - (280 * day),
                photoPreset = null,
                tapeStyle = "kraft",
                stickerText = "SWEET MEMORIES",
                rotationDegrees = 1.4f,
                isFavorite = false,
                peopleOrLocation = "Silver Moon 24-hr Diner"
            ),

            // FRIENDS
            MemoryItem(
                title = "Blue Ridge Mountain Road Trip",
                section = MemorySection.FRIENDS.id,
                note = "Windows rolled all the way down, retro synthwave playlist on blast, singing until our voices were scratchy while the sun set behind the misty ridges.",
                dateString = "July 22, 2025",
                dateMillis = now - (58 * day),
                photoPreset = "friends",
                tapeStyle = "gold",
                stickerText = "★ BEST FRIENDS ★",
                rotationDegrees = -1.9f,
                isFavorite = true,
                peopleOrLocation = "Skyline Drive, Milepost 42"
            ),
            MemoryItem(
                title = "Epic Friday Game Night & Tacos",
                section = MemorySection.FRIENDS.id,
                note = "Fresh handmade guacamole, five board games stacked high, and a game of Catan that tested the boundaries of friendship until 3 AM.",
                dateString = "August 30, 2025",
                dateMillis = now - (21 * day),
                photoPreset = null,
                tapeStyle = "coral",
                stickerText = "PURE JOY",
                rotationDegrees = 1.7f,
                isFavorite = false,
                peopleOrLocation = "The Cozy Den"
            ),

            // FAMILY
            MemoryItem(
                title = "Sunday Homemade Pasta Tradition",
                section = MemorySection.FAMILY.id,
                note = "Grandma flouring the big wooden counter, teaching the young ones how to cut delicate fettuccine ribbons. Flour on noses, aprons, and warm hearts.",
                dateString = "September 14, 2025",
                dateMillis = now - (6 * day),
                photoPreset = "birthday",
                tapeStyle = "sage",
                stickerText = "FAMILY LOVE",
                rotationDegrees = 1.5f,
                isFavorite = true,
                peopleOrLocation = "Nonna's Kitchen"
            ),
            MemoryItem(
                title = "Living Room Indoor Campfire",
                section = MemorySection.FAMILY.id,
                note = "Power went out during a thunderous autumn rainstorm. We pulled out sleeping bags, lit warm candles, and toasted marshmallows over the hearth.",
                dateString = "November 2, 2024",
                dateMillis = now - (320 * day),
                photoPreset = null,
                tapeStyle = "kraft",
                stickerText = "CHERISHED",
                rotationDegrees = -2.1f,
                isFavorite = false,
                peopleOrLocation = "Hearthside Living Room"
            ),

            // PHOTOS
            MemoryItem(
                title = "Golden Hour Laughter",
                section = MemorySection.PHOTOS.id,
                note = "Candid snapshot captured right as Lucas cracked the funniest joke of the weekend.",
                dateString = "July 24, 2025",
                dateMillis = now - (56 * day),
                photoPreset = "friends",
                tapeStyle = "sky",
                stickerText = "SNAPSHOT",
                rotationDegrees = 2.2f,
                isFavorite = true,
                peopleOrLocation = "Park Meadows"
            ),
            MemoryItem(
                title = "Candlelit Wish",
                section = MemorySection.PHOTOS.id,
                note = "Eyes squeezed shut, whispering a secret birthday wish before blowing out twenty-five candles.",
                dateString = "May 19, 2025",
                dateMillis = now - (120 * day),
                photoPreset = "birthday",
                tapeStyle = "coral",
                stickerText = "SNAPSHOT",
                rotationDegrees = -1.8f,
                isFavorite = false,
                peopleOrLocation = "Dining Room Table"
            ),
            MemoryItem(
                title = "Autumn Campus Walkway",
                section = MemorySection.PHOTOS.id,
                note = "Crisp October morning with vibrant red maple leaves blanketing the cobblestone path.",
                dateString = "October 18, 2024",
                dateMillis = now - (335 * day),
                photoPreset = "cover",
                tapeStyle = "gold",
                stickerText = "SNAPSHOT",
                rotationDegrees = 1.2f,
                isFavorite = false,
                peopleOrLocation = "Old Library Steps"
            ),

            // IMPORTANT DATES
            MemoryItem(
                title = "Mom's Birthday Celebration",
                section = MemorySection.IMPORTANT_DATES.id,
                note = "Surprise spa day retreat and custom strawberry chiffon cake from her favorite French bakery!",
                dateString = "October 12",
                dateMillis = now + (21 * day),
                photoPreset = null,
                tapeStyle = "coral",
                stickerText = "BIRTHDAY CHEER",
                rotationDegrees = -1.0f,
                isFavorite = true,
                peopleOrLocation = "Mom's House",
                isMilestoneDate = true,
                reminderMonth = 10,
                reminderDay = 12
            ),
            MemoryItem(
                title = "College Alumni Homecoming",
                section = MemorySection.IMPORTANT_DATES.id,
                note = "Reunion tailgate with the old dorm crew. Bringing the vintage banner and picnic grill.",
                dateString = "November 8",
                dateMillis = now + (48 * day),
                photoPreset = null,
                tapeStyle = "sky",
                stickerText = "CAMPUS DAYS",
                rotationDegrees = 1.2f,
                isFavorite = false,
                peopleOrLocation = "University Stadium",
                isMilestoneDate = true,
                reminderMonth = 11,
                reminderDay = 8
            ),
            MemoryItem(
                title = "Parents' 30th Pearl Anniversary",
                section = MemorySection.IMPORTANT_DATES.id,
                note = "30 years of unwavering love. Organizing a surprise dinner with all family members and presenting this memory book!",
                dateString = "December 28",
                dateMillis = now + (98 * day),
                photoPreset = null,
                tapeStyle = "gold",
                stickerText = "FAMILY LOVE",
                rotationDegrees = -1.5f,
                isFavorite = true,
                peopleOrLocation = "Grand Vista Hall",
                isMilestoneDate = true,
                reminderMonth = 12,
                reminderDay = 28
            ),
            MemoryItem(
                title = "Annual Friends Cabin Weekend",
                section = MemorySection.IMPORTANT_DATES.id,
                note = "Booking opens for Lake Tahoe cabin! Bring board games, s'mores kit, warm blankets and hot cocoa.",
                dateString = "January 16",
                dateMillis = now + (117 * day),
                photoPreset = null,
                tapeStyle = "sage",
                stickerText = "SAVE THE DATE",
                rotationDegrees = 0.8f,
                isFavorite = false,
                peopleOrLocation = "Pine Crest Cabin",
                isMilestoneDate = true,
                reminderMonth = 1,
                reminderDay = 16
            )
        )
    }
}
