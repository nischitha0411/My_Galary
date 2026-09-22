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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MemoryItem
import com.example.ui.components.StampSticker
import com.example.ui.components.WashiTape
import java.util.Calendar
import java.util.concurrent.TimeUnit

@Composable
fun ImportantDatesView(
    memories: List<MemoryItem>,
    onSelectMemory: (MemoryItem) -> Unit,
    onToggleFavorite: (MemoryItem) -> Unit,
    onAddDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("important_dates_list"),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // SECTION BANNER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF4EB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDFD4C0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
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
                                .background(Color(0xFFD64045), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Important Dates & Milestones",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF2C2219)
                            )
                            Text(
                                text = "Upcoming birthdays, anniversaries & gatherings",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFF7A6553)
                            )
                        }
                    }

                    Button(
                        onClick = onAddDateClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD64045),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Date", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (memories.isEmpty()) {
            item {
                EmptyScrapbookPage(
                    title = "No Dates Pinned Yet",
                    subtitle = "Mark milestone birthdays, anniversaries, and reunions to count down to.",
                    icon = Icons.Default.Event,
                    onAction = onAddDateClick,
                    actionText = "Pin First Date"
                )
            }
        } else {
            items(memories, key = { it.id }) { memory ->
                DateKeepsakeCard(
                    memory = memory,
                    onClick = { onSelectMemory(memory) },
                    onToggleFavorite = { onToggleFavorite(memory) }
                )
            }
        }
    }
}

@Composable
fun DateKeepsakeCard(
    memory: MemoryItem,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Calculate approximate days remaining from today
    val daysRemaining = calculateDaysUntil(memory.dateMillis)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF9)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2D6C5)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // VINTAGE CALENDAR TICKET BADGE
                Column(
                    modifier = Modifier
                        .width(64.dp)
                        .background(Color(0xFFF4EAD9), RoundedCornerShape(6.dp))
                        .border(1.2.dp, Color(0xFFD64045), RoundedCornerShape(6.dp))
                        .padding(bottom = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFD64045))
                            .padding(vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = memory.dateString.take(3).uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = extractDay(memory.dateString),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color(0xFF332014)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // CONTENT
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = memory.title,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF2C2219),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Favorite button
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                tint = if (memory.isFavorite) Color(0xFFD62828) else Color(0xFFCCC2B4),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = memory.note,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFF5A493B),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Stamp
                        StampSticker(
                            text = memory.stickerText.ifEmpty { "SAVE THE DATE" },
                            color = Color(0xFF8B263E),
                            rotation = 0f
                        )

                        // Countdown Pill
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFBE4D8), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = daysRemaining,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB83A2E)
                            )
                        }
                    }
                }
            }
        }

        // Mini washi tape on top
        WashiTape(
            style = memory.tapeStyle,
            width = 60.dp,
            height = 14.dp,
            modifier = Modifier.offset(y = (-6).dp)
        )
    }
}

private fun extractDay(dateString: String): String {
    val digits = dateString.filter { it.isDigit() }
    return if (digits.isNotEmpty()) {
        digits.take(2)
    } else {
        "★"
    }
}

private fun calculateDaysUntil(targetMillis: Long): String {
    val diff = targetMillis - System.currentTimeMillis()
    if (diff < 0) return "Celebrated"
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        days == 0L -> "Today!"
        days == 1L -> "Tomorrow!"
        days < 30L -> "In $days days"
        else -> "In ${days / 30} mos"
    }
}
