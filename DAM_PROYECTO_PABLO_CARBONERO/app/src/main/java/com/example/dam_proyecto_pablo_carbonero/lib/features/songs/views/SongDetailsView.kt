package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.toString
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.DetailsHeader
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.TabsBox
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels.SongDetailsVM

@Composable
fun SongDetailsView(navController: NavHostController, vm: SongDetailsVM = hiltViewModel()) {
    val selectedSong by vm.selectedSong.collectAsState()
    val latinNotes by vm.latinNotes.collectAsState()

    var showTabs by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (showTabs) 180f else 0f,
        label = "Icon Rotation"
    )

    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 10.dp)
    ) {
        DetailsHeader(
            title = selectedSong?.song?.name ?: "",
            navController = navController,
            subtitle = selectedSong?.song?.bandName ?: "",
            editMethod = { navController.navigate("EditSong/${selectedSong!!.song.id}") }
        )

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                navController.navigate("Tuner?selectedTuningId=${selectedSong?.tuning?.id}") {
                    popUpTo("Tuner") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) { Text("LOAD IN TUNER", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )) }

        Spacer(Modifier.height(10.dp))

        Text(selectedSong?.tuning?.name ?: "",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ))
        Text(selectedSong?.noteList?.toString(latinNotes) ?: "")

        HorizontalDivider(Modifier.padding(vertical = 5.dp), thickness = 2.dp)

        Text("BPM",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ))
        Text(selectedSong?.song?.bpm ?: "")

        Spacer(Modifier.height(10.dp))

        if (selectedSong?.song?.tabs?.isNotEmpty() == true) {
            Row(Modifier.clickable(onClick = {
                showTabs = !showTabs
            })) {
                Text("Tabs", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp))
                Icon(
                    Icons.Default.ExpandMore, "more",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
        AnimatedVisibility(showTabs) {
            TabsBox(selectedSong?.song?.tabs ?: "")
        }

    }
}

