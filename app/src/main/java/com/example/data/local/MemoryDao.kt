package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MemoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories ORDER BY isFavorite DESC, dateMillis DESC, id DESC")
    fun getAllMemories(): Flow<List<MemoryItem>>

    @Query("SELECT * FROM memories WHERE section = :section ORDER BY isFavorite DESC, dateMillis DESC, id DESC")
    fun getMemoriesBySection(section: String): Flow<List<MemoryItem>>

    @Query("SELECT * FROM memories WHERE isFavorite = 1 ORDER BY dateMillis DESC")
    fun getFavoriteMemories(): Flow<List<MemoryItem>>

    @Query("SELECT * FROM memories WHERE photoUri IS NOT NULL OR photoPreset IS NOT NULL OR section = 'photos' ORDER BY dateMillis DESC")
    fun getAllPhotoMemories(): Flow<List<MemoryItem>>

    @Query("SELECT * FROM memories WHERE section = 'important_dates' ORDER BY reminderMonth ASC, reminderDay ASC, dateMillis ASC")
    fun getImportantDates(): Flow<List<MemoryItem>>

    @Query("SELECT COUNT(*) FROM memories")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(memories: List<MemoryItem>)

    @Update
    suspend fun updateMemory(memory: MemoryItem)

    @Delete
    suspend fun deleteMemory(memory: MemoryItem)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteById(id: Long)
}
