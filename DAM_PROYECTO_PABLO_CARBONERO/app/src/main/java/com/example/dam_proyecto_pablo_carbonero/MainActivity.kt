package com.example.dam_proyecto_pablo_carbonero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.views.SettingsView
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views.CreateTuningView
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views.EditTuningView
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views.LoadingScreen
//import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.views.SettingsView
//import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels.SettingsVM
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views.CreateSongView
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views.EditSongView
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views.SongDetailsView
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views.SongLibraryView
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views.TunerView
//import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.UserPreferencesRepositoryImpl
import com.example.dam_proyecto_pablo_carbonero.ui.theme.DAM_PROYECTO_PABLO_CARBONEROTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /*companion object {
        lateinit var database: TuningDatabase
        val Context.dataStore by preferencesDataStore(name = "user_prefs")
    }*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*database = Room.databaseBuilder(
            applicationContext,
            TuningDatabase::class.java,
            "tuner-db"
        ).fallbackToDestructiveMigration().build()*/

        enableEdgeToEdge()

        setContent {
            DAM_PROYECTO_PABLO_CARBONEROTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        /*enterTransition = {
                            slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
                        },
                        popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
                        },
                        popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
                        },*/
                        startDestination = "Load"
                    ){
                        composable("Load") {
                            LoadingScreen(navController)
                        }

                        composable("Tuner") {
                            TunerView(navController)
                        }

                        composable("CreateTuning") {
                            CreateTuningView(navController)
                        }

                        composable(
                            route = "EditTuning/{selectedTuningId}",
                            arguments = listOf(navArgument("selectedTuningId") { type = NavType.StringType }
                            )) { backStackEntry ->
                                EditTuningView(
                                    navController = navController
                                )
                            }

                        composable("SongTuning") {
                            SongLibraryView(navController)
                        }

                        composable("CreateSong") {
                            CreateSongView(navController)
                        }

                        composable(
                            route = "SongDetails/{songId}/{tuningId}",
                            arguments = listOf(
                                navArgument("songId") { type = NavType.StringType },
                                navArgument("tuningId") { type = NavType.StringType }
                            )) { backStackEntry ->
                            SongDetailsView(
                                navController = navController
                            )
                        }

                        composable(
                            route = "EditSong/{songId}",
                            arguments = listOf(
                                navArgument("songId") { type = NavType.StringType },
                            )) { backStackEntry ->
                            EditSongView(
                                navController = navController
                            )
                        }

                        composable("Settings") {
                            SettingsView(navController)
                        }

                    }
                }
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/
            }
        }
    }
}
