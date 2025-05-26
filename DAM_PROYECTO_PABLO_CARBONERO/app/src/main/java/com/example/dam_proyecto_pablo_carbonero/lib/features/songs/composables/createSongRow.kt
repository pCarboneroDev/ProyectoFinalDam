package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CreateSongRow(navController: NavHostController){

    Column(Modifier.clickable(onClick = {navController.navigate("CreateSong")})) {
        Row(Modifier.fillMaxWidth().padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(Modifier.size(40.dp).background(color = Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, "add song", tint = Color.Black)
            }

            Text("Create song", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
        }

        HorizontalDivider(Modifier.padding(top = 12.dp))
    }
}