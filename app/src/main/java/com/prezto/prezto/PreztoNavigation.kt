package com.prezto.prezto

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prezto.prezto.core.data.session.AuthState
import com.prezto.prezto.feature_auth.presentation.forgot.ForgotPasswordScreen
import com.prezto.prezto.feature_auth.presentation.login.LoginScreen
import com.prezto.prezto.feature_auth.presentation.register.RegisterScreen
import com.prezto.prezto.feature_auth.presentation.verification.VerificationScreen
import com.prezto.prezto.feature_chat.presentation.ChatScreen
import com.prezto.prezto.feature_chat.presentation.inbox.InboxScreen
import com.prezto.prezto.feature_support.presentation.SupportCenterScreen
import com.prezto.prezto.feature_support.presentation.report.ReportIncidentScreen
import com.prezto.prezto.feature_profile.presentation.edit.EditProfileScreen
import com.prezto.prezto.feature_explore.presentation.home.HomeScreen
import com.prezto.prezto.feature_explore.presentation.item_detail.ItemDetailScreen
import com.prezto.prezto.feature_auth.presentation.splash.SplashScreen
import com.prezto.prezto.feature_explore.presentation.publish.PublishScreen
import com.prezto.prezto.feature_explore.presentation.publish.PublishSuccessScreen
import com.prezto.prezto.feature_profile.presentation.profile.ProfileScreen
import com.prezto.prezto.feautre_rental.presentation.checkout.CheckoutScreen
import com.prezto.prezto.feautre_rental.presentation.checkout.CheckoutSuccessScreen

@Composable
fun PreztoNavigation(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    // AuthCoordinator: reacciona a cambios de sesión a nivel de app. Si la sesión cae a
    // Unauthenticated estando previamente autenticada (logout o token expirado), fuerza el
    // regreso a Login limpiando todo el backstack. El arranque en frío lo decide el Splash.
    val authState by mainViewModel.authState.collectAsState()
    val hasNotifications by mainViewModel.hasNotifications.collectAsState()
    val previousState = remember { mutableStateOf<AuthState>(AuthState.Unknown) }
    LaunchedEffect(authState) {
        if (authState == AuthState.Unauthenticated && previousState.value == AuthState.Authenticated) {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
        previousState.value = authState
    }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot")
                }
            )
        }

        composable(
            route = "register",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
            }
        ) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToVerification = { phone ->
                    navController.navigate("verification/$phone")
                }
            )
        }

        composable(
            route = "verification/{phone}",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
            }
        ) {
            VerificationScreen(
                onNavigateBack = { navController.popBackStack() },
                onVerified = {
                    // Sesión emitida; limpia todo el flujo de auth y entra a Home.
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "forgot",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
            }
        ) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNavigateToDetail = { itemId ->
                    navController.navigate("item_detail/$itemId")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToPublish = {
                    navController.navigate("publish")
                },
                onNavigateToInbox = {
                    mainViewModel.onNotificationsSeen()
                    navController.navigate("inbox")
                },
                hasNotifications = hasNotifications
            )
        }
        composable(
            route = "inbox",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
            }
        ) {
            InboxScreen(
                onNavigateBack = { navController.popBackStack() },
                onConversationClick = { navController.navigate("chat") }
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId").orEmpty()
            ItemDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRequestRental = { days ->
                    navController.navigate("checkout/$itemId/$days")
                }
            )
        }
        composable("checkout/{itemId}/{days}") {
            CheckoutScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPaymentSuccess = {
                    // Limpia item_detail + checkout; deja [home, checkout_success].
                    navController.navigate("checkout_success") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }
        composable("checkout_success") {
            CheckoutSuccessScreen(
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToChat = {
                    navController.navigate("chat")
                }
            )
        }
        composable(
            route = "chat",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
            }
        ) {
            ChatScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditProfile = { navController.navigate("edit_profile") },
                onNavigateToSupport = { navController.navigate("support") }
                // El logout limpia la sesión; el AuthCoordinator navega al login.
            )
        }
        composable(
            route = "support",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
            }
        ) {
            SupportCenterScreen(
                onNavigateBack = { navController.popBackStack() },
                onContactAgent = { navController.navigate("chat") },
                onReportIncident = { navController.navigate("report_incident") }
            )
        }
        composable(
            route = "report_incident",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
            }
        ) {
            ReportIncidentScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "edit_profile",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
            }
        ) {
            EditProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("publish") {
            PublishScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPublishSuccess = {
                    // Saca el formulario del back stack; deja [home, publish_success].
                    navController.navigate("publish_success") {
                        popUpTo("publish") { inclusive = true }
                    }
                }
            )
        }
        composable("publish_success") {
            PublishSuccessScreen(
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

    }
}