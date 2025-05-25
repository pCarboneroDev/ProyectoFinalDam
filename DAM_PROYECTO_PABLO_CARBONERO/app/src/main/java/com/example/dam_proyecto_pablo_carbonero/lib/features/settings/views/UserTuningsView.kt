package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels.UserTuningsVM
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views.DetailsHeader
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views.DropdownMenuOption
import com.example.dam_proyecto_pablo_carbonero.lib.utils.NoteList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserTuningsView(navController: NavHostController, vm: UserTuningsVM = hiltViewModel()){

    val tunings by vm.tuningList.collectAsState()

    Column(Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(horizontal = 12.dp)
    ) {
        DetailsHeader(title = "My tunings", subtitle = "${tunings.size}", navController = navController)
        LazyColumn {
            items(tunings.size) { index ->
                TuningRow(
                    tuning = tunings[index],
                    navController,
                    onDelete = {},
                    onFav = { vm.setTuningAsFavourite(it) }
                )
            }
        }
    }

}

@Composable
fun TuningRow(tuning: TuningWithNotesModel, navController: NavHostController, onDelete: () -> Unit, onFav: suspend (TuningWithNotesModel) -> Boolean) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.clickable(onClick = {
        navController.navigate("EditTuning/${tuning.tuning.id}")

    })) {
        Row(Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(tuning.tuning.name, fontWeight = FontWeight.Bold)
                Text(NoteList(tuning.noteList, false), fontSize = 15.sp)
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.MoreVert, "moreInfo")
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Mark as favourite")
                        },
                        leadingIcon = { Icon(if (tuning.tuning.favourite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "fav") },
                        onClick ={
                            CoroutineScope(Dispatchers.Main).launch {
                                onFav(tuning)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = {Text("Edit")},
                        leadingIcon = { Icon(Icons.Default.Tune, "") },
                        onClick = { navController.navigate("EditTuning/${tuning.tuning.id}")}
                    )
                    DropdownMenuItem(
                        text = {Text("Delete")},
                        leadingIcon = { Icon(Icons.Default.Delete, "delete", tint = Color.Red) },
                        onClick = {}
                    )
                }
            }
        }

        HorizontalDivider(Modifier.padding(top = 12.dp))
    }
}