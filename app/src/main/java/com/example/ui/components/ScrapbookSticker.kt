package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StampSticker(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF9E2A2B),
    rotation: Float = -4f
) {
    Box(
        modifier = modifier
            .rotate(rotation)
            .border(
                width = 1.5.dp,
                color = color.copy(alpha = 0.85f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 0.8.dp,
                    color = color.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(2.dp)
                )
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = text.uppercase(),
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                fontFamily = FontFamily.Serif
            )
        }
    }
}

@Composable
fun PushPinIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFD62828)
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .shadow(4.dp, shape = CircleShape)
            .background(color, CircleShape)
            .border(1.5.dp, Color.White.copy(alpha = 0.8f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color.White.copy(alpha = 0.5f), CircleShape)
        )
    }
}

@Composable
fun HeartFavoriteSticker(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(elevation = if (isFavorite) 4.dp else 1.dp, shape = CircleShape)
            .background(
                if (isFavorite) Color(0xFFFFD166) else Color(0xFFFFFDF8),
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = if (isFavorite) Color(0xFFD4A359) else Color(0xFFD9D4C7),
                shape = CircleShape
            )
            .padding(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = if (isFavorite) "Favorited keepsake" else "Mark as favorite",
            tint = if (isFavorite) Color(0xFFD62828) else Color(0xFFB5ADA4),
            modifier = Modifier.size(16.dp)
        )
    }
}
