package com.example.ui.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MemoryItem
import com.example.data.model.MemorySection
import com.example.ui.components.PolaroidCard

@Composable
fun SectionScrapbookView(
    section: MemorySection,
    memories: List<MemoryItem>,
    onSelectMemory: (MemoryItem) -> Unit,
    onToggleFavorite: (MemoryItem) -> Unit,
    onAddMemoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("all") }

    val filteredMemories = when (selectedFilter) {
        "photos" -> memories.filter { !it.photoUri.isNullOrEmpty() || !it.photoPreset.isNullOrEmpty() }
        "notes" -> memories.filter { it.photoUri.isNullOrEmpty() && it.photoPreset.isNullOrEmpty() }
        else -> memories
    }

    val iconVector = when (section) {
        MemorySection.SPECIAL_MOMENTS -> Icons.Default.Star
        MemorySection.BIRTHDAY_MEMORIES -> Icons.Default.Cake
        MemorySection.COLLEGE_MEMORIES -> Icons.Default.School
        MemorySection.FRIENDS -> Icons.Default.Diversity1
        MemorySection.FAMILY -> Icons.Default.Diversity3
        else -> Icons.Default.AutoAwesome
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("section_list_${section.id}"),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // SECTION HEADER BANNER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF4EB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDFD4C0))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(section.tabColor(), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = iconVector,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = section.title,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color(0xFF2C2219)
                                )
                                Text(
                                    text = section.description,
                                    fontSize = 11.5.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF7A6553),
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick filter pills & Add memory button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                label = "All (${memories.size})",
                                isSelected = selectedFilter == "all",
                                onClick = { selectedFilter = "all" },
                                activeColor = section.tabColor()
                            )
                            FilterChip(
                                label = "Photos",
                                isSelected = selectedFilter == "photos",
                                onClick = { selectedFilter = "photos" },
                                activeColor = section.tabColor()
                            )
                            FilterChip(
                                label = "Notes",
                                isSelected = selectedFilter == "notes",
                                onClick = { selectedFilter = "notes" },
                                activeColor = section.tabColor()
                            )
                        }

                        Button(
                            onClick = onAddMemoryClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = section.tabColor(),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NoteAdd,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tape Here", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (filteredMemories.isEmpty()) {
            item {
                EmptyScrapbookPage(
                    title = "Page Empty",
                    subtitle = "Be the first to tape a keepsake, photo, or handwritten story into ${section.title}.",
                    icon = iconVector,
                    onAction = onAddMemoryClick,
                    actionText = "Tape First Memory"
                )
            }
        } else {
            items(filteredMemories, key = { it.id }) { memory ->
                PolaroidCard(
                    memory = memory,
                    onClick = { onSelectMemory(memory) },
                    onToggleFavorite = { onToggleFavorite(memory) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    activeColor: Color
) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) activeColor else Color(0xFFECE4D5),
                RoundedCornerShape(12.dp)
            )
            .border(
                0.8.dp,
                if (isSelected) Color(0x33000000) else Color(0xFFD4C8B5),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color(0xFF4A382A)
        )
    }
}
