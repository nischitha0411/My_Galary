package com.example.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.MemoryItem

@Composable
fun PolaroidCard(
    memory: MemoryItem,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
    showJournalPreview: Boolean = true
) {
    val rotation = memory.rotationDegrees.coerceIn(-3.5f, 3.5f)

    Box(
        modifier = modifier
            .rotate(rotation)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // The polaroid photo / keepsake card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(2.dp),
                    spotColor = Color(0x33352213)
                )
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFBF8F2) // authentic polaroid photo paper
            ),
            shape = RoundedCornerShape(2.dp),
            border = androidx.compose.foundation.BorderStroke(0.7.dp, Color(0xFFE4DDD2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, start = 14.dp, end = 14.dp, bottom = 16.dp)
            ) {
                // PHOTO / GRAPHIC AREA
                val hasPhoto = !memory.photoUri.isNullOrEmpty() || !memory.photoPreset.isNullOrEmpty()
                
                if (hasPhoto) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.05f)
                            .background(Color(0xFF221F1C))
                            .border(0.5.dp, Color(0x22000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!memory.photoUri.isNullOrEmpty()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(Uri.parse(memory.photoUri))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = memory.title,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            val drawableRes = when (memory.photoPreset) {
                                "friends" -> R.drawable.img_sample_friends
                                "birthday" -> R.drawable.img_sample_birthday
                                "cover" -> R.drawable.img_scrapbook_cover
                                else -> R.drawable.img_app_icon
                            }
                            Image(
                                painter = painterResource(id = drawableRes),
                                contentDescription = memory.title,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Stamp sticker overlay on bottom-right of photo
                        if (memory.stickerText.isNotEmpty()) {
                            StampSticker(
                                text = memory.stickerText,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-8).dp, y = (-8).dp),
                                color = Color(0xFFFFF7EA),
                                rotation = -3f
                            )
                        }
                    }
                } else {
                    // Journal Note Slip style (Lined craft paper)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(Color(0xFFF7F1E5))
                            .border(0.5.dp, Color(0xFFDFD7C7))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = Color(0xFF8D5B3A),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Handwritten Keepsake",
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF8D5B3A)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = memory.note,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = Color(0xFF3B332B),
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = FontFamily.Serif
                            )
                        }

                        if (memory.stickerText.isNotEmpty()) {
                            StampSticker(
                                text = memory.stickerText,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = 0.dp, y = 0.dp),
                                color = Color(0xFF9E2A2B),
                                rotation = 3f
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // POLAROID CHIN (Handwritten caption & metadata)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = memory.title,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF2C241D),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFF8E7A6B),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = memory.dateString,
                                fontSize = 11.sp,
                                color = Color(0xFF7A6A5C),
                                fontStyle = FontStyle.Italic
                            )

                            if (!memory.peopleOrLocation.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "•", fontSize = 11.sp, color = Color(0xFF7A6A5C))
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF8E7A6B),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = memory.peopleOrLocation,
                                    fontSize = 11.sp,
                                    color = Color(0xFF7A6A5C),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Favorite Heart Sticker
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(36.dp)
                    ) {
                        HeartFavoriteSticker(
                            isFavorite = memory.isFavorite,
                            onClick = onToggleFavorite
                        )
                    }
                }

                // If showJournalPreview and there is a photo, give a 1-line hint of the handwritten memory
                if (showJournalPreview && hasPhoto && memory.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“${memory.note}”",
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = Color(0xFF5C4E43),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // TACTILE WASHI TAPE at top center
        WashiTape(
            style = memory.tapeStyle,
            modifier = Modifier
                .offset(y = (-10).dp)
                .rotate(if (rotation > 0) -2f else 3f),
            width = 90.dp,
            height = 20.dp
        )
    }
}
