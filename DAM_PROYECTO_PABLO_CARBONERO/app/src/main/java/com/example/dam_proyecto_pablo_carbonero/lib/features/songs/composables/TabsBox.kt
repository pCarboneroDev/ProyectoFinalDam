package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun TabsBox(tabsText: String){
    val horizontalScroll = rememberScrollState()
    val verticalScroll = rememberScrollState()

    Box(
        modifier = Modifier
            .horizontalScroll(horizontalScroll)
            .verticalScroll(verticalScroll)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = tabsText,
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )
        )
    }
}