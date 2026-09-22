package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MemorySection
import com.example.ui.components.AddMemoryDialog
import com.example.ui.components.MemoryDetailDialog
import com.example.ui.components.ScrapbookHeader
import com.example.ui.components.ScrapbookTabs
import com.example.ui.components.WashiTape
import com.example.ui.sections.ImportantDatesView
import com.example.ui.sections.PhotosWallView
import com.example.ui.sections.SectionScrapbookView

@Composable
fun MemoryBookScreen(
    viewModel: MemoryBookViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("memory_book_root"),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            // Tactile Keepsake FAB styled like a vintage luggage tag with washi tape
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp, end = 4.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                FloatingActionButton(
                    onClick = { viewModel.openAddDialog() },
                    containerColor = Color(0xFFE07A5F),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .testTag("fab_add_memory")
                        .shadow(8.dp, RoundedCornerShape(18.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Tape a new memory",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tape Keepsake",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Mini washi tape attached to FAB top
                WashiTape(
                    style = "gold",
                    width = 54.dp,
                    height = 12.dp,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    ) { paddingValues ->
        // VINTAGE PARCHMENT SCRAPBOOK BACKGROUND
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF6EFE3)) // Warm cream craft album paper
        ) {
            // Subtle spiral binding / craft texture line along left edge
            Canvas(modifier = Modifier.fillMaxSize()) {
                val spiralMargin = 8.dp.toPx()
                // Spine shadow
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0x33301D11),
                            Color(0x11301D11),
                            Color.Transparent
                        )
                    ),
                    topLeft = Offset(0f, 0f),
                    size = androidx.compose.ui.geometry.Size(20.dp.toPx(), size.height)
                )

                // Binder rings holes along left margin
                var y = 80.dp.toPx()
                while (y < size.height - 40.dp.toPx()) {
                    drawCircle(
                        color = Color(0xFFD6C8B8),
                        radius = 4.dp.toPx(),
                        center = Offset(spiralMargin, y)
                    )
                    drawCircle(
                        color = Color(0xFF422818),
                        radius = 2.5.dp.toPx(),
                        center = Offset(spiralMargin, y)
                    )
                    y += 44.dp.toPx()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // TOP STITCHED LEATHER HEADER
                ScrapbookHeader(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    showFavoritesOnly = uiState.showFavoritesOnly,
                    onToggleFavoritesFilter = { viewModel.toggleFavoritesFilter() },
                    onAddKeepsakeClick = { viewModel.openAddDialog() }
                )

                // PHYSICAL SCRAPBOOK SECTION DIVIDER TABS
                ScrapbookTabs(
                    sections = MemorySection.entries,
                    selectedSection = uiState.selectedSection,
                    onSelectSection = { viewModel.selectSection(it) },
                    itemCounts = uiState.itemCounts
                )

                // SCRAPBOOK PAPER PAGE BODY (Left margin accommodates binder rings)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp)
                ) {
                    when (uiState.selectedSection) {
                        MemorySection.PHOTOS -> {
                            PhotosWallView(
                                memories = uiState.displayedMemories,
                                onSelectMemory = { viewModel.openDetail(it) },
                                onToggleFavorite = { viewModel.toggleFavorite(it) },
                                onAddPhotoClick = { viewModel.openAddDialog() }
                            )
                        }
                        MemorySection.IMPORTANT_DATES -> {
                            ImportantDatesView(
                                memories = uiState.displayedMemories,
                                onSelectMemory = { viewModel.openDetail(it) },
                                onToggleFavorite = { viewModel.toggleFavorite(it) },
                                onAddDateClick = { viewModel.openAddDialog() }
                            )
                        }
                        else -> {
                            SectionScrapbookView(
                                section = uiState.selectedSection,
                                memories = uiState.displayedMemories,
                                onSelectMemory = { viewModel.openDetail(it) },
                                onToggleFavorite = { viewModel.toggleFavorite(it) },
                                onAddMemoryClick = { viewModel.openAddDialog() }
                            )
                        }
                    }
                }
            }
        }

        // ADD MEMORY MODAL DIALOG
        if (uiState.showAddDialog) {
            AddMemoryDialog(
                initialSection = uiState.selectedSection,
                onDismiss = { viewModel.dismissAddDialog() },
                onSaveMemory = { newMemory ->
                    viewModel.saveMemory(newMemory)
                }
            )
        }

        // MEMORY DETAIL / FLIP CARD DIALOG
        uiState.activeDetailMemory?.let { memory ->
            MemoryDetailDialog(
                memory = memory,
                onDismiss = { viewModel.dismissDetail() },
                onDelete = { viewModel.deleteMemory(memory) },
                onToggleFavorite = { viewModel.toggleFavorite(memory) }
            )
        }
    }
}
