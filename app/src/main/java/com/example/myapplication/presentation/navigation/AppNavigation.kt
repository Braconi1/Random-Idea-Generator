package com.example.myapplication.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.uii.screens.about.AboutScreen
import com.example.myapplication.presentation.uii.screens.detail.IdeaDetailScreen
import com.example.myapplication.presentation.uii.screens.favorites.FavoritesScreen
import com.example.myapplication.presentation.uii.screens.home.HomeScreen
import com.example.myapplication.presentation.uii.screens.idea.IdeaScreen
import com.example.myapplication.presentation.uii.screens.login.LoginScreen
import com.example.myapplication.presentation.viewModel.IdeaViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: IdeaViewModel = viewModel()

    val navLabels = listOf("Home", "Submit", "Favorites", "About", "Login")
    val navIcons = listOf(
        Icons.Default.Home,
        Icons.Default.Add,
        Icons.Default.Favorite,
        Icons.Default.Info,
        Icons.Default.Person
    )
    val topLevelRoutes = listOf("home", "submit", "favorites", "about", "login")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute?.startsWith("detail") == false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    navLabels.forEachIndexed { index, label ->
                        val route = topLevelRoutes[index]
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(navIcons[index], contentDescription = label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onIdeaClick = { idea ->
                        navController.navigate("detail/${idea.id}/${idea.title}")
                    }
                )
            }
            composable("submit") {
                IdeaScreen(viewModel = viewModel)
            }
            composable("favorites") {
                FavoritesScreen(
                    viewModel = viewModel,
                    onIdeaClick = { idea ->
                        navController.navigate("detail/${idea.id}/${idea.title}")
                    }
                )
            }
            composable("about") {
                AboutScreen()
            }
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("detail/{ideaId}/{ideaTitle}") { backStackEntry ->
                val ideaId = backStackEntry.arguments?.getString("ideaId")?.toIntOrNull() ?: 0
                val ideaTitle = backStackEntry.arguments?.getString("ideaTitle") ?: ""
                IdeaDetailScreen(
                    ideaId = ideaId,
                    ideaTitle = ideaTitle,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}