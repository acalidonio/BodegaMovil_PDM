package com.acalidonio.bodegamovil.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.acalidonio.bodegamovil.screen.dashboard.DashboardScreen
import com.acalidonio.bodegamovil.screen.login.LoginScreen

@Composable
fun BodegaMovilApp() {
    var backStack by remember { mutableStateOf(listOf<Any>(Routes.Login)) }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack = backStack.dropLast(1) },
        entryProvider = entryProvider{
            entry<Routes.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack = listOf(Routes.Dashboard) // Ir al dashboard (login propio no implementado)
                    }
                )
            }
            entry<Routes.Dashboard> {
                DashboardScreen()
            }
        },
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(250)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(250)
            )
        }
    )
}


