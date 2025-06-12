package com.example.dam_proyecto_pablo_carbonero.lib.features.loadingScreen.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dam_proyecto_pablo_carbonero.lib.features.loadingScreen.viewsmodels.LoadingVM

@Composable
fun LoadingScreen(
    navController: NavController,
    vm: LoadingVM = hiltViewModel()
){
    val loadComplete by vm.loadComplete.collectAsState()
    val criticalError by vm.criticalError.collectAsState()

    Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        if (!loadComplete){
            CircularProgressIndicator()
        }
        else if (criticalError){
            Text("Critical error. Try restarting the app")
        }
    }

    LaunchedEffect(loadComplete) {
        if (loadComplete == true) {
            navController.navigate("Tuner") {
                popUpTo("Load") { inclusive = true }
            }
        }
    }
}