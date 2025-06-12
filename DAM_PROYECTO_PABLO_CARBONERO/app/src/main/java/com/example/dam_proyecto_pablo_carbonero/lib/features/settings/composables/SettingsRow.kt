package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun SettingsRow(
    title: String,
    composable: @Composable () -> Unit,
    onClick: () -> Unit = {},
    noDivider: Boolean = false
) {
    Column(Modifier.clickable(onClick = onClick)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(title)
            Spacer(Modifier.weight(1f))
            composable()
        }
        if (!noDivider)
            HorizontalDivider(
                Modifier
                    .padding(top = 12.dp)
                    .alpha(0.1f),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
    }
}