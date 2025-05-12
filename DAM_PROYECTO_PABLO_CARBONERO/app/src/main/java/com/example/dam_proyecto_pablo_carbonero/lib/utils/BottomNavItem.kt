package com.example.dam_proyecto_pablo_carbonero.lib.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Tuner : BottomNavItem("Tuner", "Tuner", Icons.Default.Tune)
    object Songs : BottomNavItem("SongTuning", "Songs", Icons.Default.Audiotrack)
    object Settings : BottomNavItem("Settings", "Settings", Icons.Default.Settings)
}