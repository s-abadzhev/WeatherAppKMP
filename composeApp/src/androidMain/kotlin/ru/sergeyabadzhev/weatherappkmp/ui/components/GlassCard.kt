package ru.sergeyabadzhev.weatherappkmp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.15f),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.15f),
        tonalElevation = 0.dp
    ) {
        Box { content() }
    }
}