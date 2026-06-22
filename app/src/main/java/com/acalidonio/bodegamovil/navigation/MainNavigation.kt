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
import com.acalidonio.bodegamovil.screen.detail.ProductDetailScreen
import com.acalidonio.bodegamovil.screen.home.HomeScreen
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
                        backStack = listOf(Routes.Home)
                    }
                )
            }
            entry<Routes.Home> {
                HomeScreen(
                    onNavigateToProductDetail = { sku -> backStack = backStack + Routes.ProductDetail(sku) }
                )
            }
            entry<Routes.ProductDetail> { route ->
                ProductDetailScreen(
                    sku = route.sku,
                    onBackClick = { backStack = backStack.dropLast(1) }
                )
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


