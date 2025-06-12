package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.SortSelectorModal
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.CreateSongRow
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.SongRow
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels.SongLibraryVM
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongLibraryView(navController: NavHostController, vm: SongLibraryVM = hiltViewModel()){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val songListPaged = vm.songListPaged.collectAsLazyPagingItems()
    val currentSortOption by vm.selectedSortOption.collectAsState()
    val sortModal by vm.sortModal.collectAsState()
    val searchBarOpen by vm.isSearchBarOpen.collectAsState()
    val searchQuery by vm.query.collectAsState()
    val searchResult = vm.searchResultsPaged.collectAsLazyPagingItems()
    val message by vm.messageManager.collectAsState()


    LaunchedEffect(message) {
        if (message.isSuccess == false){
            Toast.makeText(context, message.message, Toast.LENGTH_SHORT).show()
            vm.resetMessageManager()
        }
    }
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
            if (sortModal){
                SortSelectorModal(
                    currentSortOption = currentSortOption,
                    dismissFunction = { vm.setSortModal(false) },
                    sortOptionSelected = { vm.sortList(it) }
                )
            }


            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Column {
                        Text("Song library", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text( text = "${songListPaged.itemCount} songs", fontSize = 13.sp)
                    }

                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { vm.setSortModal(true) }) {
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
                        active = searchBarOpen,
                        onActiveChange = { active ->
                            vm.setSearchbar(active)
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        trailingIcon = {
                            IconButton(
                                onClick = { vm.clearQuery() }
                            ) { Icon(Icons.Default.Clear, "clear") }
                        },

                    ){
                        LazyColumn {
                            if (searchResult.itemCount > 0){
                                items(searchResult.itemCount) { index ->
                                    val item = searchResult[index]
                                    item?.let {
                                        SongRow(it, navController, {
                                            coroutineScope.launch {
                                                vm.deleteSong(it)
                                            }
                                        })
                                    }
                                }
                            }
                        }
                    }

                    LazyColumn(Modifier.fillMaxSize()) {
                        item {
                            CreateSongRow(navController)
                        }

                        if (songListPaged.itemCount > 0){
                            items(songListPaged.itemCount) { index ->
                                val item = songListPaged[index]
                                item?.let {
                                    SongRow(it, navController, {
                                        coroutineScope.launch {
                                            vm.deleteSong(it)
                                        }
                                    })
                                }
                            }
                        }


                        item {
                            when {
                                songListPaged.loadState.append is LoadState.Loading -> {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
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
}





