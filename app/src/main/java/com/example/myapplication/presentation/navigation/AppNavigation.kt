package com.example.myapplication.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.ui.screens.about.AboutScreen
import com.example.myapplication.presentation.ui.screens.detail.IdeaDetailScreen
import com.example.myapplication.presentation.ui.screens.favorites.FavoritesScreen
import com.example.myapplication.presentation.ui.screens.home.HomeScreen
import com.example.myapplication.presentation.ui.screens.idea.IdeaScreen
import com.example.myapplication.presentation.ui.screens.login.AccountScreen
import com.example.myapplication.presentation.ui.screens.login.LoginScreen
import com.example.myapplication.presentation.ui.screens.login.SignUpScreen
import com.example.myapplication.presentation.ui.screens.login.WelcomeScreen
import com.example.myapplication.presentation.viewModel.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()

    val navLabels = listOf("Home", "Submit", "Favorites", "About", "Account")
    val navIcons = listOf(
        Icons.Default.Home,
        Icons.Default.Add,
        Icons.Default.Favorite,
        Icons.Default.Info,
        Icons.Default.AccountCircle
    )
    val topLevelRoutes = listOf("home", "submit", "favorites", "about", "account")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute?.startsWith("detail") == false
            && currentRoute?.startsWith("signup") == false
            && currentRoute?.startsWith("welcome") == false
            && currentRoute?.startsWith("login") == false

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
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onLoginClick = { navController.navigate("login") },
                    onSignUpClick = { navController.navigate("signup") }
                )
            }
            composable("home") {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = viewModel,
                    onIdeaClick = { idea ->
                        navController.navigate("detail/${idea.ideaId}")
                    }
                )
            }
            composable("submit") {
                val viewModel: SubmitViewModel = hiltViewModel()
                IdeaScreen(viewModel = viewModel)
            }
            composable("favorites") {
                val viewModel: FavoritesViewModel = hiltViewModel()
                FavoritesScreen(
                    viewModel = viewModel,
                    onIdeaClick = { idea ->
                        navController.navigate("detail/${idea.ideaId}")
                    }
                )
            }
            composable("about") {
                val viewModel: AboutViewModel = hiltViewModel()
                AboutScreen(viewModel = viewModel)
            }
            composable("account") {
                AccountScreen(
                    viewModel = loginViewModel,
                    onLogout = {
                        navController.navigate("welcome") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("login") {
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onSignUp = {
                        navController.navigate("signup")
                    }
                )
            }
            composable("signup") {
                SignUpScreen(
                    viewModel = loginViewModel,
                    onSignUpSuccess = {
                        navController.navigate("home") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }
            composable("detail/{ideaId}") { backStackEntry ->
                val ideaId = backStackEntry.arguments?.getString("ideaId")?.toLongOrNull() ?: 0L
                val viewModel: DetailViewModel = hiltViewModel()
                IdeaDetailScreen(
                    ideaId = ideaId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}