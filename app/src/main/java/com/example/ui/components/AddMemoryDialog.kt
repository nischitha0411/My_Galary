package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun AddMemoryDialog(
    initialSection: MemorySection,
    onDismiss: () -> Unit,
    onSaveMemory: (MemoryItem) -> Unit
) {
    var selectedSection by remember { mutableStateOf(initialSection) }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var peopleOrLocation by remember { mutableStateOf("") }

    val defaultDateString = remember {
        SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
    }
    var dateString by remember { mutableStateOf(defaultDateString) }

    var selectedPhotoUri by remember { mutableStateOf<String?>(null) }
    var selectedPhotoPreset by remember {
        mutableStateOf<String?>(
            if (initialSection == MemorySection.PHOTOS) "friends" else null
        )
    }

    var selectedTapeStyle by remember { mutableStateOf("kraft") }
    var selectedStickerText by remember { mutableStateOf(initialSection.defaultSticker) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri.toString()
            selectedPhotoPreset = null
        }
    }

    val tapeOptions = listOf(
        "kraft" to "Kraft Tape",
        "gold" to "Golden Shimmer",
        "coral" to "Coral Stripes",
        "sage" to "Sage Leaves",
        "sky" to "Sky Grid"
    )

    val stickerOptions = listOf(
        "★ BEST DAY EVER ★",
        "CHERISHED",
        "BIRTHDAY CHEER",
        "CAMPUS DAYS",
        "★ BEST FRIENDS ★",
        "FAMILY LOVE",
        "PURE JOY",
        "SNAPSHOT",
        "SAVE THE DATE"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .shadow(16.dp, RoundedCornerShape(16.dp))
                .background(Color(0xFFF9F5EC), RoundedCornerShape(16.dp))
                .border(2.dp, Color(0xFFD4A359), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // DIALOG HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Add to Memory Book",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF332014)
                        )
                        Text(
                            text = "Create a tangible scrapbook keepsake",
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF7A6553)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFEADBCE), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF4A3525),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SECTION SELECTOR CHIPS
                Text(
                    text = "SELECT SECTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFF8B5E3C)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MemorySection.entries.forEach { section ->
                        val isSelected = section == selectedSection
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) section.tabColor() else Color(0xFFEDE4D5)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFF332014) else Color(0xFFD1C3B2),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedSection = section
                                    selectedStickerText = section.defaultSticker
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = section.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF47372B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // TITLE
                Text(
                    text = "TITLE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFF8B5E3C)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = {
                        Text(
                            "e.g. Grandma's Surprise Party, Dorm Room Decorating...",
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF9E8B7A)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("memory_title_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF2C2219),
                        unfocusedTextColor = Color(0xFF2C2219),
                        focusedContainerColor = Color(0xFFFFFDF8),
                        unfocusedContainerColor = Color(0xFFFFFDF8),
                        focusedBorderColor = Color(0xFFD4A359),
                        unfocusedBorderColor = Color(0xFFD9CCA8)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // DATE & LOCATION ROW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DATE / TIME",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color(0xFF8B5E3C)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = dateString,
                            onValueChange = { dateString = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("memory_date_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF2C2219),
                                unfocusedTextColor = Color(0xFF2C2219),
                                focusedContainerColor = Color(0xFFFFFDF8),
                                unfocusedContainerColor = Color(0xFFFFFDF8),
                                focusedBorderColor = Color(0xFFD4A359),
                                unfocusedBorderColor = Color(0xFFD9CCA8)
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PEOPLE / PLACE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color(0xFF8B5E3C)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = peopleOrLocation,
                            onValueChange = { peopleOrLocation = it },
                            placeholder = { Text("e.g. Emma, Campus Lawn", fontSize = 12.sp, color = Color(0xFF9E8B7A)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("memory_people_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF2C2219),
                                unfocusedTextColor = Color(0xFF2C2219),
                                focusedContainerColor = Color(0xFFFFFDF8),
                                unfocusedContainerColor = Color(0xFFFFFDF8),
                                focusedBorderColor = Color(0xFFD4A359),
                                unfocusedBorderColor = Color(0xFFD9CCA8)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // STORY / JOURNAL NOTE
                Text(
                    text = "HANDWRITTEN JOURNAL NOTE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFF8B5E3C)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = {
                        Text(
                            "Write your heartfelt thoughts, favorite quotes, or funny stories to preserve forever...",
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF9E8B7A)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("memory_note_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF2C2219),
                        unfocusedTextColor = Color(0xFF2C2219),
                        focusedContainerColor = Color(0xFFFFFDF8),
                        unfocusedContainerColor = Color(0xFFFFFDF8),
                        focusedBorderColor = Color(0xFFD4A359),
                        unfocusedBorderColor = Color(0xFFD9CCA8)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // PHOTO ATTACHMENT SECTION
                Text(
                    text = "PHOTO SNAPSHOT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFF8B5E3C)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pick from phone gallery
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("pick_photo_button"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF5A311A)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pick from Gallery", fontSize = 12.sp)
                    }

                    // Or clear photo
                    if (selectedPhotoUri != null || selectedPhotoPreset != null) {
                        Button(
                            onClick = {
                                selectedPhotoUri = null
                                selectedPhotoPreset = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD1C4B5),
                                contentColor = Color(0xFF332014)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Text note only", fontSize = 12.sp)
                        }
                    }
                }

                // Preset options thumbnails
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Or Vintage Presets:",
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF7A6553)
                    )

                    val presets = listOf(
                        "friends" to R.drawable.img_sample_friends,
                        "birthday" to R.drawable.img_sample_birthday,
                        "cover" to R.drawable.img_scrapbook_cover
                    )

                    presets.forEach { (presetKey, drawableId) ->
                        val isPresetSelected = selectedPhotoPreset == presetKey && selectedPhotoUri == null
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .border(
                                    2.dp,
                                    if (isPresetSelected) Color(0xFFD4A359) else Color.Transparent,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    selectedPhotoPreset = presetKey
                                    selectedPhotoUri = null
                                }
                        ) {
                            Image(
                                painter = painterResource(id = drawableId),
                                contentDescription = presetKey,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                            if (isPresetSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(Color(0xFFD4A359), CircleShape)
                                        .align(Alignment.TopEnd),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Photo Preview if selected
                if (selectedPhotoUri != null || selectedPhotoPreset != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color.White)
                            .padding(4.dp)
                            .border(1.dp, Color(0xFFD6CBBB))
                    ) {
                        if (selectedPhotoUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(Uri.parse(selectedPhotoUri))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Selected Photo",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            val res = when (selectedPhotoPreset) {
                                "friends" -> R.drawable.img_sample_friends
                                "birthday" -> R.drawable.img_sample_birthday
                                else -> R.drawable.img_scrapbook_cover
                            }
                            Image(
                                painter = painterResource(id = res),
                                contentDescription = "Selected Preset",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // WASHI TAPE STYLE PICKER
                Text(
                    text = "WASHI TAPE STYLE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFF8B5E3C)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    tapeOptions.forEach { (style, name) ->
                        val isSelected = selectedTapeStyle == style
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFFEADCCB) else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFF9E7734) else Color.Transparent,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { selectedTapeStyle = style }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            WashiTape(style = style, width = 64.dp, height = 18.dp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = name, fontSize = 10.5.sp, color = Color(0xFF4A3525))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // STICKER STAMP PICKER
                Text(
                    text = "SCRAPBOOK RUBBER STAMP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color(0xFF8B5E3C)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stickerOptions.forEach { sticker ->
                        val isSelected = selectedStickerText == sticker
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFFFFE8B3) else Color(0xFFEFE8DA))
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFFC79836) else Color(0xFFD6C8B8),
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { selectedStickerText = sticker }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            StampSticker(
                                text = sticker,
                                color = if (isSelected) Color(0xFF9E2A2B) else Color(0xFF7A4A3B),
                                rotation = 0f
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ACTION BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6B5341)
                        )
                    ) {
                        Text("Cancel", fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            val finalTitle = title.trim().ifEmpty { "Treasured Keepsake" }
                            val finalNote = note.trim().ifEmpty { "A memory etched forever in this book." }
                            val randRotation = (Random.nextFloat() * 4f) - 2f

                            val memory = MemoryItem(
                                title = finalTitle,
                                section = selectedSection.id,
                                note = finalNote,
                                dateString = dateString.trim().ifEmpty { defaultDateString },
                                dateMillis = System.currentTimeMillis(),
                                photoUri = selectedPhotoUri,
                                photoPreset = selectedPhotoPreset,
                                tapeStyle = selectedTapeStyle,
                                stickerText = selectedStickerText,
                                rotationDegrees = randRotation,
                                peopleOrLocation = peopleOrLocation.trim().ifEmpty { null }
                            )
                            onSaveMemory(memory)
                        },
                        modifier = Modifier.testTag("save_memory_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE07A5F),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save to Scrapbook", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }
                }
            }
        }
    }
}
