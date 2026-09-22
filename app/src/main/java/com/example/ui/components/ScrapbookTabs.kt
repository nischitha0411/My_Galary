package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MemorySection

@Composable
fun ScrapbookTabs(
    sections: List<MemorySection>,
    selectedSection: MemorySection,
    onSelectSection: (MemorySection) -> Unit,
    itemCounts: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        sections.forEach { section ->
            val isSelected = section == selectedSection
            val count = itemCounts[section.id] ?: 0

            val tabHeight by animateDpAsState(
                targetValue = if (isSelected) 46.dp else 38.dp,
                label = "tabHeight"
            )

            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) section.tabColor() else section.tabColor().copy(alpha = 0.72f),
                label = "tabBg"
            )

            Box(
                modifier = Modifier
                    .testTag("tab_${section.id}")
                    .height(tabHeight)
                    .shadow(
                        elevation = if (isSelected) 6.dp else 2.dp,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
                    .border(
                        width = if (isSelected) 1.5.dp else 0.8.dp,
                        color = if (isSelected) Color(0xFFFDEFD7) else Color(0x33000000),
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
                    .clickable { onSelectSection(section) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = section.shortName,
                        color = Color.White,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        fontSize = if (isSelected) 13.5.sp else 12.sp,
                        letterSpacing = 0.3.sp,
                        fontFamily = FontFamily.Serif
                    )

                    if (count > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(
                                    if (isSelected) Color(0xFFFFF7EA) else Color.White.copy(alpha = 0.8f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$count",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = section.tabColor()
                            )
                        }
                    }
                }
            }
        }
    }
}
