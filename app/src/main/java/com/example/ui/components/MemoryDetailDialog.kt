package com.example.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.MemoryItem
import com.example.data.model.MemorySection

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MemoryDetailDialog(
    memory: MemoryItem,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    var showBackOfPhoto by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val section = MemorySection.fromId(memory.section)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .shadow(20.dp, RoundedCornerShape(12.dp))
                .background(Color(0xFFFBF7EE), RoundedCornerShape(12.dp))
                .border(2.dp, Color(0xFFD4A359), RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // TOP BAR: SECTION CHIP + ACTION BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Section badge
                    Box(
                        modifier = Modifier
                            .background(section.tabColor(), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = section.title.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Flip Button
                        IconButton(
                            onClick = { showBackOfPhoto = !showBackOfPhoto },
                            modifier = Modifier
                                .testTag("flip_photo_button")
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlipCameraAndroid,
                                contentDescription = "Flip memory card",
                                tint = Color(0xFF5A311A)
                            )
                        }

                        // Favorite Heart
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Toggle favorite",
                                tint = if (memory.isFavorite) Color(0xFFD62828) else Color(0xFFB5ADA4)
                            )
                        }

                        // Delete Keepsake
                        IconButton(
                            onClick = { showDeleteConfirmation = true },
                            modifier = Modifier
                                .testTag("delete_memory_button")
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove memory",
                                tint = Color(0xFF9E4738)
                            )
                        }

                        // Close Dialog
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF4A3525)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // FLIPPABLE CONTENT: FRONT (POLAROID) OR BACK (VINTAGE POSTCARD / JOURNAL BACK)
                AnimatedContent(
                    targetState = showBackOfPhoto,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "flipCard"
                ) { isBack ->
                    if (!isBack) {
                        // FRONT: POLAROID VIEW
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFFDF8), RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFFE2DCD1), RoundedCornerShape(4.dp))
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Top Washi Tape
                            WashiTape(
                                style = memory.tapeStyle,
                                width = 110.dp,
                                height = 24.dp,
                                modifier = Modifier
                                    .offset(y = (-6).dp)
                                    .rotate(if (memory.rotationDegrees > 0) -2f else 2f)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // PHOTO
                            val hasPhoto = !memory.photoUri.isNullOrEmpty() || !memory.photoPreset.isNullOrEmpty()

                            if (hasPhoto) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1.1f)
                                        .background(Color(0xFF1E1A17))
                                        .border(1.dp, Color(0xFF332014)),
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

                                    // Stamp
                                    if (memory.stickerText.isNotEmpty()) {
                                        StampSticker(
                                            text = memory.stickerText,
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .offset(x = (-10).dp, y = (-10).dp),
                                            color = Color(0xFFFFF7EA),
                                            rotation = -4f
                                        )
                                    }
                                }
                            } else {
                                // Keepsake Parchment Plate
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .background(Color(0xFFF5EFE3))
                                        .border(1.dp, Color(0xFFD9CCA8))
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        StampSticker(
                                            text = memory.stickerText.ifEmpty { "KEEPSAKE" },
                                            color = Color(0xFF8B263E),
                                            rotation = 0f
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = memory.title,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif,
                                            color = Color(0xFF332014)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // POLAROID CAPTION
                            Text(
                                text = memory.title,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                                color = Color(0xFF2C2219)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = Color(0xFF8B5E3C),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = memory.dateString,
                                    fontSize = 12.5.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF7A6553)
                                )

                                if (!memory.peopleOrLocation.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("•", color = Color(0xFF8B5E3C))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF8B5E3C),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = memory.peopleOrLocation,
                                        fontSize = 12.5.sp,
                                        color = Color(0xFF7A6553)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Hint to flip
                            TextButton(
                                onClick = { showBackOfPhoto = true },
                                modifier = Modifier.testTag("tap_to_read_note_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlipCameraAndroid,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFF8B5E3C)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Flip over to read handwritten journal note",
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF8B5E3C)
                                )
                            }
                        }
                    } else {
                        // BACK OF POLAROID / POSTCARD
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(4.dp))
                                .background(Color(0xFFF7F1E5), RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFFD6C8B8), RoundedCornerShape(4.dp))
                                .padding(18.dp)
                        ) {
                            // Postal stamp and divider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Text(
                                        text = "JOURNAL ENTRY",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.5.sp,
                                        color = Color(0xFF8B5E3C)
                                    )
                                    Text(
                                        text = memory.dateString,
                                        fontSize = 12.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = Color(0xFF6B5341)
                                    )
                                }

                                // Vintage faux postage stamp
                                Box(
                                    modifier = Modifier
                                        .size(46.dp, 54.dp)
                                        .background(Color(0xFFFFFDF8))
                                        .border(1.2.dp, Color(0xFFB5704D))
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.PushPin,
                                            contentDescription = null,
                                            tint = Color(0xFFB5704D),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "AIR MAIL",
                                            fontSize = 7.5.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFB5704D)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Separator line
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFFD1C3B2))
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // HANDWRITTEN JOURNAL NOTE BODY
                            Text(
                                text = memory.note,
                                fontSize = 15.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Normal,
                                color = Color(0xFF2C2219)
                            )

                            if (!memory.peopleOrLocation.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(18.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Cherished with: ",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF8B5E3C)
                                    )
                                    Text(
                                        text = memory.peopleOrLocation,
                                        fontSize = 12.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = Color(0xFF332014)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Flip back button
                            Button(
                                onClick = { showBackOfPhoto = false },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .testTag("flip_to_front_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF8B5E3C),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlipCameraAndroid,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Flip back to photo", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = {
                Text(
                    text = "Remove Keepsake?",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF332014)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove '${memory.title}' from your memory book?",
                    color = Color(0xFF5A4434)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmation = false
                        onDelete()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB53D35))
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Keep", color = Color(0xFF5A311A))
                }
            },
            containerColor = Color(0xFFFFFBF2)
        )
    }
}
