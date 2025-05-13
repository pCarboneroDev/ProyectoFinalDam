package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.room.util.query
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewsmodels.SongDetailsVM

@Composable
fun SongDetailsView(navController: NavHostController, vm: SongDetailsVM = hiltViewModel()){
    val selectedSong by vm.selectedSong.collectAsState()

    Column(Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(horizontal = 10.dp)) {
        DetailsHeader(
            title = selectedSong?.song?.name ?: "",
            navController = navController,
            subtitle = selectedSong?.song?.bandName ?: "",
            editMethod = { navController.navigate("EditSong/${selectedSong!!.song.id}") }
        )

        Button(onClick = {}) { Text("Load in tuner") }

        Text(selectedSong?.tuning?.name ?: "")
        Row {
            selectedSong?.noteList?.forEach { note ->
                Text("${note.englishName} ")
            }
        }
    }
}

@Composable
fun DetailsHeader(title: String, subtitle: String = "", editMethod: (() -> Unit)? = null, navController: NavHostController){
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,) {
        IconButton(onClick = {navController.popBackStack()}) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "back", tint = MaterialTheme.colorScheme.primary)
        }
        if(editMethod != null){
            IconButton(onClick = editMethod) {
                Icon(Icons.Default.Edit, "edit", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
    Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    if(subtitle.isNotEmpty()){
        Text(subtitle, fontSize = 13.sp)
    }
    HorizontalDivider(thickness = 5.dp)
}