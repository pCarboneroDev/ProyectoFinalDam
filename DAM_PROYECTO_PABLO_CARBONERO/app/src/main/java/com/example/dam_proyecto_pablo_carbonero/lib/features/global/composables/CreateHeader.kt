package com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CreateHeader(
    title: String,
    subtitle: String = "",
    saveMethod: (() -> Unit)? = null,
    navController: NavHostController,
    enabledSave: Boolean = true)
{
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,) {
        IconButton(
            onClick = {navController.popBackStack()},
            enabled = enabledSave
        ) {
            Icon(Icons.Default.Close, "close", tint = MaterialTheme.colorScheme.primary)
        }
        if(saveMethod != null){
            IconButton(onClick = saveMethod) {
                Icon(Icons.Default.Save, "save", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
    Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    if(subtitle.isNotEmpty()){
        Text(subtitle, fontSize = 13.sp)
    }
    HorizontalDivider(thickness = 2.dp)
}