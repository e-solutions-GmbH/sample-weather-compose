package de.eso.weather.ui.shared.compose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.floor

@Composable
fun GridBackground(modifier: Modifier = Modifier) {
    val strokeColor = MaterialTheme.colors.onBackground

    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 1f

        val squareEdgeLength = 160f.dp.toPx()
        val crossLineLength = 10f.dp.toPx()

        val width = size.width
        val height = size.height

        val verticalSquares = floor(height / squareEdgeLength).toInt()
        val horizontalSquares = floor(width / squareEdgeLength).toInt()

        val horizontalLines = verticalSquares + 1
        val verticalLines = horizontalSquares + 1

        val offsetFromStart = (width - squareEdgeLength * horizontalSquares) / 2
        val offsetFromTop = (height - squareEdgeLength * verticalSquares) / 2

        // Draw vertical lines
        for (x in 0..verticalLines) {
            val verticalLineX = x * squareEdgeLength + offsetFromStart

            // Don't draw lines at the horizontal edge of the grid
            if (verticalLineX == 0f || verticalLineX == height) {
                continue
            }

            drawLine(
                color = strokeColor.copy(alpha = 0.3f),
                start = Offset(verticalLineX, 0f),
                end = Offset(verticalLineX, height),
                strokeWidth = strokeWidth
            )
        }

        // Draw horizontal lines
        for (y in 0..horizontalLines) {
            val horizontalLineY = y * squareEdgeLength + offsetFromTop

            // Don't draw lines at the vertical edge of the grid
            if (horizontalLineY == 0f || horizontalLineY == height) {
                continue
            }

            drawLine(
                color = strokeColor.copy(alpha = 0.3f),
                start = Offset(0f, horizontalLineY),
                end = Offset(width, horizontalLineY),
                strokeWidth = strokeWidth
            )
        }

        // Draw crosses for grid line intersections
        for (x in 0..verticalLines) {
            for (y in 0..horizontalLines) {
                val crossCenterX = x * squareEdgeLength + offsetFromStart
                val crossCenterY = y * squareEdgeLength + offsetFromTop

                // Don't draw crosses at any edges of the grid
                if (crossCenterX == 0f || crossCenterY == 0f || crossCenterX == width || crossCenterY == height) {
                    continue
                }

                drawLine(
                    color = strokeColor,
                    start = Offset(crossCenterX - crossLineLength, crossCenterY),
                    end = Offset(crossCenterX + crossLineLength, crossCenterY),
                    strokeWidth = strokeWidth
                )

                drawLine(
                    color = strokeColor,
                    start = Offset(crossCenterX, crossCenterY - crossLineLength),
                    end = Offset(crossCenterX, crossCenterY + crossLineLength),
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}

@Preview
@Composable
fun GridBackgroundPreview480x320() {
    GridBackground(modifier = Modifier.size(width = 480.dp, height = 320.dp))
}

@Preview
@Composable
fun GridBackgroundPreview320x480() {
    GridBackground(modifier = Modifier.size(width = 320.dp, height = 480.dp))
}

@Preview
@Composable
fun GridBackgroundPreview640x640() {
    GridBackground(modifier = Modifier.size(width = 640.dp, height = 640.dp))
}

@Preview
@Composable
fun GridBackgroundPreview500x400() {
    GridBackground(modifier = Modifier.size(width = 500.dp, height = 400.dp))
}
