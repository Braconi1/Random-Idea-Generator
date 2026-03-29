package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.screens.about.AboutScreen
import com.example.myapplication.screens.favorites.FavoritesScreen
import com.example.myapplication.screens.home.HomeScreen
import com.example.myapplication.screens.idea.IdeaScreen
import com.example.myapplication.screens.login.LoginScreen
import com.example.myapplication.screens.IdeaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val viewModel: IdeaViewModel = viewModel()
    var currentScreen by remember { mutableStateOf("Home") }
    // val tempVariable - ovo cu kasnije dodati

    val navLabels = listOf("Home", "Submit", "Favorites", "About", "Login")
    val navIcons = listOf(
        Icons.Default.Home,
        Icons.Default.Add,
        Icons.Default.Favorite,
        Icons.Default.Info,
        Icons.Default.Person
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                for (i in 0 until navLabels.size) {
                    val screenName = navLabels[i]
                    val icon = navIcons[i]
                    val isSelected = currentScreen == screenName

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            currentScreen = screenName
                        },
                        icon = { Icon(icon, contentDescription = screenName) },
                        label = { Text(screenName) }
                    )
                }
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)

        when (currentScreen) {
            "Home" -> HomeScreen(viewModel, contentModifier)
            "Submit" -> IdeaScreen(viewModel, contentModifier)
            "Favorites" -> FavoritesScreen(viewModel, contentModifier)
            "About" -> AboutScreen(contentModifier)
            "Login" -> LoginScreen(
                modifier = contentModifier,
                onLoginSuccess = {
                    currentScreen = "Home"
                }
            )
            else -> {
                HomeScreen(viewModel, contentModifier)
            }
        }
    }
}