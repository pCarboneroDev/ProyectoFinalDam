package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Card(
        Modifier.padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(Modifier.padding(15.dp)) {
            Text(title, style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold))
            content()
        }
    }
}