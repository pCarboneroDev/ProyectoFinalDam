package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning

@Composable
fun SongRow(song: SongWithTuning, navController: NavHostController){
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.clickable(onClick = {
        navController.navigate("SongDetails/${song.song.id}/${song.tuning.id}")

    })) {
        Row(Modifier.fillMaxWidth().padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(song.song.name, fontWeight = FontWeight.Bold)
                Text(song.song.bandName, fontSize = 15.sp)
                Text("${song.tuning.name} | ${song.song.bpm} | ${song.song.key}", fontSize = 12.sp)
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.MoreVert, "moreInfo")
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Load in tuner") },
                        leadingIcon = {Icon(Icons.Default.Tune, "Load in tuner")},
                        onClick = {
                            navController.navigate("Tuner?selectedTuningId=${song.tuning.id}"){
                                popUpTo("Tuner") { inclusive = true }
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        leadingIcon = {Icon(Icons.Default.MusicNote, "Edit song")},
                        onClick = { navController.navigate("EditSong/${song.song.id}")}
                    )
                    DropdownMenuItem(
                        text = {Text("Delete")},
                        leadingIcon = {Icon(Icons.Default.Delete, "Edit song", tint = Color.Red)},
                        onClick = {}
                    )
                }
            }
        }

        HorizontalDivider(Modifier.padding(top = 12.dp))
    }
}