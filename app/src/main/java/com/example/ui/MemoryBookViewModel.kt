package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MemoryDatabase
import com.example.data.model.MemoryItem
import com.example.data.model.MemorySection
import com.example.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MemoryBookUiState(
    val selectedSection: MemorySection = MemorySection.SPECIAL_MOMENTS,
    val searchQuery: String = "",
    val showFavoritesOnly: Boolean = false,
    val activeDetailMemory: MemoryItem? = null,
    val showAddDialog: Boolean = false,
    val itemCounts: Map<String, Int> = emptyMap(),
    val displayedMemories: List<MemoryItem> = emptyList()
)

class MemoryBookViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MemoryRepository

    private val _selectedSection = MutableStateFlow(MemorySection.SPECIAL_MOMENTS)
    val selectedSection: StateFlow<MemorySection> = _selectedSection.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    private val _activeDetailMemory = MutableStateFlow<MemoryItem?>(null)
    val activeDetailMemory: StateFlow<MemoryItem?> = _activeDetailMemory.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    init {
        val db = MemoryDatabase.getInstance(application)
        repository = MemoryRepository(db.memoryDao())

        viewModelScope.launch {
            repository.ensureInitialSeed()
        }
    }

    private val allMemoriesFlow = repository.getAllMemories()

    private data class FilterState(
        val section: MemorySection,
        val query: String,
        val favoritesOnly: Boolean,
        val detailMemory: MemoryItem?,
        val showAdd: Boolean
    )

    private val filterStateFlow = combine(
        _selectedSection,
        _searchQuery,
        _showFavoritesOnly,
        _activeDetailMemory,
        _showAddDialog
    ) { section, query, favoritesOnly, detailMemory, showAdd ->
        FilterState(section, query, favoritesOnly, detailMemory, showAdd)
    }

    val uiState: StateFlow<MemoryBookUiState> = combine(
        allMemoriesFlow,
        filterStateFlow
    ) { allMemories, filter ->
        val section = filter.section
        val query = filter.query
        val favoritesOnly = filter.favoritesOnly

        // Compute item counts per section
        val counts = mutableMapOf<String, Int>()
        MemorySection.entries.forEach { s ->
            counts[s.id] = when (s) {
                MemorySection.PHOTOS -> allMemories.count { !it.photoUri.isNullOrEmpty() || !it.photoPreset.isNullOrEmpty() || it.section == "photos" }
                MemorySection.IMPORTANT_DATES -> allMemories.count { it.section == MemorySection.IMPORTANT_DATES.id }
                else -> allMemories.count { it.section == s.id }
            }
        }

        // Filter memories by section
        val sectionFiltered = when (section) {
            MemorySection.PHOTOS -> allMemories.filter {
                !it.photoUri.isNullOrEmpty() || !it.photoPreset.isNullOrEmpty() || it.section == "photos"
            }
            MemorySection.IMPORTANT_DATES -> allMemories.filter {
                it.section == MemorySection.IMPORTANT_DATES.id
            }
            else -> allMemories.filter { it.section == section.id }
        }

        // Filter by favorites if enabled
        val favFiltered = if (favoritesOnly) {
            sectionFiltered.filter { it.isFavorite }
        } else {
            sectionFiltered
        }

        // Filter by search query
        val searchFiltered = if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            favFiltered.filter { memory ->
                memory.title.lowercase().contains(q) ||
                    memory.note.lowercase().contains(q) ||
                    (memory.peopleOrLocation?.lowercase()?.contains(q) == true) ||
                    memory.stickerText.lowercase().contains(q) ||
                    memory.dateString.lowercase().contains(q)
            }
        } else {
            favFiltered
        }

        MemoryBookUiState(
            selectedSection = section,
            searchQuery = query,
            showFavoritesOnly = favoritesOnly,
            activeDetailMemory = filter.detailMemory,
            showAddDialog = filter.showAdd,
            itemCounts = counts,
            displayedMemories = searchFiltered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MemoryBookUiState()
    )

    fun selectSection(section: MemorySection) {
        _selectedSection.value = section
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavoritesFilter() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun openDetail(memory: MemoryItem) {
        _activeDetailMemory.value = memory
    }

    fun dismissDetail() {
        _activeDetailMemory.value = null
    }

    fun openAddDialog() {
        _showAddDialog.value = true
    }

    fun dismissAddDialog() {
        _showAddDialog.value = false
    }

    fun saveMemory(memory: MemoryItem) {
        viewModelScope.launch {
            repository.insertMemory(memory)
            _showAddDialog.value = false
        }
    }

    fun deleteMemory(memory: MemoryItem) {
        viewModelScope.launch {
            repository.deleteMemory(memory)
            if (_activeDetailMemory.value?.id == memory.id) {
                _activeDetailMemory.value = null
            }
        }
    }

    fun toggleFavorite(memory: MemoryItem) {
        viewModelScope.launch {
            repository.toggleFavorite(memory)
            // Update active detail if it's currently open
            if (_activeDetailMemory.value?.id == memory.id) {
                _activeDetailMemory.value = memory.copy(isFavorite = !memory.isFavorite)
            }
        }
    }
}
