package com.cb.myshowcase.presentation.home

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cb.myshowcase.common.Screen
import com.cb.myshowcase.presentation.ai_art.ImageFullScreen
import com.cb.myshowcase.presentation.ai_art.ImageScreen
import com.cb.myshowcase.presentation.ai_art.ImageViewModel

@Composable
fun HomeScreen(activity: Activity) {

    val navController = rememberNavController()
    val startDestination = Screen.ImageScreen.route

    HomeNavHost(
        activity = activity,
        navController = navController,
        startDestination = startDestination
    )
}

@Composable
fun HomeNavHost(
    activity: Activity,
    navController: NavHostController,
    startDestination: String,
) {
    val imageViewModel: ImageViewModel = viewModel()

    NavHost(
        navController = navController, startDestination = startDestination
    ) {

        composable(route = Screen.ImageFullScreen.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            arguments =
            listOf(
                navArgument("position") { defaultValue = 0 }
            )
        ) {
            val position = it.arguments?.getInt("position")
            ImageFullScreen(
                state = imageViewModel.state,
                onEvent = {
                    imageViewModel.onEvent(it)
                },
                position = position!!,
                application = activity.application
            ) {
                navController.popBackStack()
            }
        }

        composable(route = Screen.ImageScreen.route, enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            )
        },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }) {

            ImageScreen(
                state = imageViewModel.state,
                onEvent = {
                    imageViewModel.onEvent(it)
                }
            ) {
                navController.navigate(it)
            }
        }
    }
}