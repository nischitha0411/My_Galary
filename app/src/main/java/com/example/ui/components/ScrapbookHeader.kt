package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun ScrapbookHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    showFavoritesOnly: Boolean,
    onToggleFavoritesFilter: () -> Unit,
    onAddKeepsakeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearchExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3F2212),
                        Color(0xFF5A311A),
                        Color(0xFF4B2815)
                    )
                )
            )
    ) {
        // Brass stitched trim line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF9E7734),
                            Color(0xFFFFD166),
                            Color(0xFFC79836),
                            Color(0xFFFFD166),
                            Color(0xFF9E7734)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Book Title & Embossed Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Mini Scrapbook Album Icon badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(3.dp, CircleShape)
                        .background(Color(0xFF27150B), CircleShape)
                        .border(1.5.dp, Color(0xFFD4A359), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "My Memory Book",
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "My Memory Book",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        letterSpacing = 0.5.sp,
                        color = Color(0xFFFEECC8) // Warm embossed gold
                    )
                    Text(
                        text = "Scrapbook of Cherished Moments",
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 11.5.sp,
                        color = Color(0xFFD4BBA6)
                    )
                }
            }

            // Action icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Search toggle
                IconButton(
                    onClick = {
                        isSearchExpanded = !isSearchExpanded
                        if (!isSearchExpanded) onSearchQueryChange("")
                    },
                    modifier = Modifier
                        .testTag("search_toggle_button")
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = "Search memories",
                        tint = if (searchQuery.isNotEmpty()) Color(0xFFFFD166) else Color(0xFFF7E6D3)
                    )
                }

                // Favorites filter toggle
                IconButton(
                    onClick = onToggleFavoritesFilter,
                    modifier = Modifier
                        .testTag("favorites_filter_button")
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Show favorites",
                        tint = if (showFavoritesOnly) Color(0xFFFF5964) else Color(0xFFD4BBA6)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Tactile Add Keepsake Button
                Button(
                    onClick = onAddKeepsakeClick,
                    modifier = Modifier
                        .testTag("add_keepsake_button")
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE07A5F),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Memory",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }

        // Expandable Search Bar
        AnimatedVisibility(
            visible = isSearchExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = "Search memories, places, friends...",
                            color = Color(0xFFA89484),
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF2C2219),
                        unfocusedTextColor = Color(0xFF2C2219),
                        focusedContainerColor = Color(0xFFFFFBF2),
                        unfocusedContainerColor = Color(0xFFFFFBF2),
                        focusedBorderColor = Color(0xFFD4A359),
                        unfocusedBorderColor = Color(0xFFD9CCA8)
                    )
                )
            }
        }

        // Bottom stitched seam effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color(0xFF231309))
        )
    }
}
