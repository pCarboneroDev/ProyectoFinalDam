package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Song
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.SortOption
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.SortSelectorModal
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewsmodels.SongLibraryVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongLibraryView(navController: NavHostController, vm: SongLibraryVM = hiltViewModel()){
    val lifecycleOwner = LocalLifecycleOwner.current

    // TODO probar cambiar esto por launched effect
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.loadViewModel()
            }
        }

        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }


    val songList by vm.songList.collectAsState()
    val currentSortOption by vm.selectedSortOption.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) }
    )
    { innerPadding ->
        Column(
            Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            // = Arrangement.Center
        ) {
            if (openAlertDialog){
                SortSelectorModal(
                    currentSortOption = currentSortOption,
                    dismissFunction = {openAlertDialog = false},
                    sortOptionSelected = vm::sortList
                )
            }
            Column(Modifier.fillMaxSize().padding(10.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = {openAlertDialog = true}) {
                        Icon(Icons.Default.Menu, "order", tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Text("Song library", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text( text = "${songList.size} songs", fontSize = 13.sp)
                HorizontalDivider(thickness = 5.dp)

                Column(Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { newQuery ->
                            searchQuery = newQuery
                        },
                        onSearch = { query ->

                            println("Search triggered with query: $query")
                        },
                        active = isActive,
                        onActiveChange = { active ->
                            isActive = active
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                        //leadingIcon = @Composable (() -> Unit)? = { Icon(Icons.Default.Search, contentDescription = "Search") }
                    ){}

                    LazyColumn(Modifier.fillMaxSize()) {
                        item {
                            CreateSongRow(navController)
                            //HorizontalDivider(Modifier.padding(top = 12.dp))
                        }
                        if (songList.isNotEmpty()){
                            items(songList.size) { index ->
                                SongRow(songList[index], navController)
                            }
                        }

                    }
                }
            }
        }
    }
}

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
                    Modifier.background(color = MaterialTheme.colorScheme.secondary)
                ) {
                    DropdownMenuItem(
                        text = {DropdownMenuItem(icon = Icons.Default.Tune, text = "Load in tuner")},// {Row { Icon(Icons.Default.Tune, ""); Text(text = "Load in tuner") }},
                        onClick = {}
                    )
                    DropdownMenuItem(
                        text = {DropdownMenuItem(icon = Icons.Default.MusicNote, text = "Edit")}, //{Row { Icon(Icons.Default.MusicNote, ""); Text(text = "Edit") }},
                        onClick = { navController.navigate("EditSong/${song.song.id}")}
                    )
                    DropdownMenuItem(
                        text = {DropdownMenuItem(icon = Icons.Default.Delete, text = "Delete")},
                        onClick = {}
                    )
                }
            }
        }

        HorizontalDivider(Modifier.padding(top = 12.dp))
    }
}

@Composable
fun DropdownMenuItem(icon: ImageVector, text: String){
    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        Icon(icon, "")
        Text(text = text)
    }
}
