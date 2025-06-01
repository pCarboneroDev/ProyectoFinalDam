package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.Tuning
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongSearchDelegate
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.SortSelectorModal
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.SongWithTuning
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.CreateSongRow
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.SongRow
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels.SongLibraryVM
import androidx.paging.compose.LazyPagingItems


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongLibraryView(navController: NavHostController, vm: SongLibraryVM = hiltViewModel()){
   /* val lifecycleOwner = LocalLifecycleOwner.current

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
    }*/


    val songList by vm.songList.collectAsState()
    val songListPaged = vm.songListPaged.collectAsLazyPagingItems()
    val currentSortOption by vm.selectedSortOption.collectAsState()

   // var searchQuery by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    val searchQuery by vm.query.collectAsState() //vm.searchQuery.collectAsState()
    val searchResult = vm.searchResultsPaged.collectAsLazyPagingItems() //vm.searchResults.collectAsState()

    var loading by remember { mutableStateOf(false) }
    

    LaunchedEffect(Unit) {
        vm.loadViewModel()
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) }
    )
    { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // = Arrangement.Center
        ) {
            if (openAlertDialog){
                SortSelectorModal(
                    currentSortOption = currentSortOption,
                    dismissFunction = {openAlertDialog = false},
                    sortOptionSelected = { vm.sortList(it) }
                )
            }
            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Column {
                        Text("Song library", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text( text = "${songListPaged.itemCount} songs", fontSize = 13.sp)
                        //Text( text = "${songList.size} songs", fontSize = 13.sp)
                    }

                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = {openAlertDialog = true}) {
                        Icon(Icons.Default.Menu, "order", tint = MaterialTheme.colorScheme.primary)
                    }
                }
                HorizontalDivider(thickness = 2.dp)

                Column(Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {

                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        query = searchQuery,
                        onQueryChange = { newQuery ->
                            vm.onQueryChanged(newQuery)
                        },
                        onSearch = { query ->

                        },
                        active = isActive,
                        onActiveChange = { active ->
                            isActive = active
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        trailingIcon = {
                            IconButton(
                                onClick = { vm.clearQuery() }
                            ) { Icon(Icons.Default.Clear, "clear") }
                        },

                        //leadingIcon = @Composable (() -> Unit)? = { Icon(Icons.Default.Search, contentDescription = "Search") }
                    ){
                        LazyColumn {
                            if (searchResult.itemCount > 0){
                                items(searchResult.itemCount) { index ->
                                    val item = searchResult[index]
                                    item?.let {
                                        SongRow(it, navController)
                                    }
                                }
                            }
                        }
                    }




                    LazyColumn(Modifier.fillMaxSize()) {
                        item {
                            CreateSongRow(navController)
                        }

                        item {
                            if(songListPaged.itemCount <= 0) CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }

                        if (songListPaged.itemCount > 0){
                            items(songListPaged.itemCount) { index ->
                                val item = songListPaged[index]
                                item?.let {
                                    SongRow(it, navController)
                                }
                            }
                        }

                        item {
                            when {
                                songListPaged.loadState.append is LoadState.Loading -> {
                                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



