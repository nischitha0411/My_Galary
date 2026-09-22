package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WashiTape(
    style: String,
    modifier: Modifier = Modifier,
    width: Dp = 80.dp,
    height: Dp = 22.dp
) {
    val (baseColor, accentColor) = when (style.lowercase()) {
        "gold" -> Color(0xE6E8C26E) to Color(0xFFC79836)
        "coral" -> Color(0xE6F28E7D) to Color(0xFFD45D4B)
        "sage" -> Color(0xE694BA9B) to Color(0xFF6B9B74)
        "sky" -> Color(0xE67EA6D1) to Color(0xFF4A7AA8)
        else -> Color(0xE6D9BFA2) to Color(0xFFAC8B66) // kraft paper default
    }

    Canvas(
        modifier = modifier
            .width(width)
            .height(height)
    ) {
        val w = size.width
        val h = size.height
        val teeth = 6
        val toothH = h / teeth

        val path = Path().apply {
            moveTo(0f, 0f)
            // Left jagged edge
            for (i in 0 until teeth) {
                val y1 = i * toothH + toothH * 0.5f
                val y2 = (i + 1) * toothH
                lineTo(if (i % 2 == 0) 3.dp.toPx() else 0f, y1)
                lineTo(0f, y2)
            }
            // Bottom edge
            lineTo(w, h)
            // Right jagged edge
            for (i in teeth downTo 1) {
                val y1 = (i - 0.5f) * toothH
                val y2 = (i - 1) * toothH
                lineTo(w - if (i % 2 == 0) 3.dp.toPx() else 0f, y1)
                lineTo(w, y2)
            }
            close()
        }

        // Draw translucent tape base
        drawPath(path = path, color = baseColor)

        // Draw decorative tape patterns (stripes or dots)
        when (style.lowercase()) {
            "coral", "sky" -> {
                // Diagonal stripes
                var x = -h
                while (x < w + h) {
                    drawLine(
                        color = accentColor.copy(alpha = 0.45f),
                        start = Offset(x, 0f),
                        end = Offset(x + h, h),
                        strokeWidth = 2.dp.toPx()
                    )
                    x += 10.dp.toPx()
                }
            }
            "gold" -> {
                // Shimmer dashes
                var x = 8.dp.toPx()
                while (x < w - 8.dp.toPx()) {
                    drawCircle(
                        color = accentColor.copy(alpha = 0.5f),
                        radius = 2.dp.toPx(),
                        center = Offset(x, h / 2)
                    )
                    x += 10.dp.toPx()
                }
            }
            "sage" -> {
                // Little botanical leaf dashes
                var x = 10.dp.toPx()
                while (x < w - 10.dp.toPx()) {
                    drawLine(
                        color = accentColor.copy(alpha = 0.5f),
                        start = Offset(x, h * 0.3f),
                        end = Offset(x + 4.dp.toPx(), h * 0.7f),
                        strokeWidth = 1.5.dp.toPx()
                    )
                    x += 12.dp.toPx()
                }
            }
            else -> {
                // Kraft fiber lines
                drawLine(
                    color = accentColor.copy(alpha = 0.35f),
                    start = Offset(4.dp.toPx(), h * 0.4f),
                    end = Offset(w - 4.dp.toPx(), h * 0.4f),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = accentColor.copy(alpha = 0.35f),
                    start = Offset(6.dp.toPx(), h * 0.65f),
                    end = Offset(w - 6.dp.toPx(), h * 0.65f),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    }
}
