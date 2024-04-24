package com.example.traveldiary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.traveldiary.ui.screens.homeAddMark.AddMarkerViewModel
import com.example.traveldiary.ui.screens.homeAddMark.HomeAddMarkScreen
import com.example.traveldiary.ui.screens.login.LogInScreen
import com.example.traveldiary.ui.screens.signin.AddUserScreen
import com.example.traveldiary.ui.screens.signin.AddTravelViewModel
import com.example.traveldiary.ui.screens.login.LoginViewModel
import com.example.traveldiary.ui.screens.homeMap.HomeMapScreen
import com.example.traveldiary.ui.screens.homeMarks.HomeMarksScreen
import com.example.traveldiary.ui.screens.login.HomeScreen
import org.koin.androidx.compose.koinViewModel

sealed class TravelDiaryRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object LogIn : TravelDiaryRoute("log-in", "log-in")

    data object HomeMap : TravelDiaryRoute(
        "home/map/{userUsername}",
        "homePage",
        listOf(navArgument("userUsername") { type = NavType.StringType })
    ) {
        fun buildRoute(userUsername: String) = "home/map/$userUsername"
    }

    data object HomeMarks : TravelDiaryRoute(
        "home/mark/",
        "marks",
    )

    data object HomeAddMark : TravelDiaryRoute(
        "home/add/{userUsername}/{latitude}/{longitude}",
        "addMark",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("latitude") { type = NavType.FloatType },
            navArgument("longitude") { type = NavType.FloatType }
        )
    ) {
        fun buildRoute(userUsername: String, latitude: Float, longitude: Float) =
            "home/add/$userUsername/$latitude/$longitude"
    }

    data object SignIn : TravelDiaryRoute("sign-in", "sign-in")

    companion object {
        val routes = setOf(LogIn, HomeMap, SignIn, HomeMarks, HomeAddMark)
    }
}


@Composable
fun TravelDiaryNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val usersVm = koinViewModel<UsersViewModel>()
    val usersState by usersVm.state.collectAsStateWithLifecycle()

    val markersVm = koinViewModel<MarkersViewModel>()
    val markersState by markersVm.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = TravelDiaryRoute.LogIn.route,
        modifier = modifier
    ) {
        with(TravelDiaryRoute.LogIn) {
            composable(route) {
                usersVm.resetValues()
                val loginVm = koinViewModel<LoginViewModel>()
                val loginState by loginVm.state.collectAsStateWithLifecycle()
                LogInScreen(
                    state = loginState,
                    actions = loginVm.actions,
                    onSubmit = {usersVm.login(loginState.toUser())},
                    navController = navController,
                    usersVm
                )
            }
        }
        with(TravelDiaryRoute.HomeMap) {
            composable(route, arguments) { backStackEntry ->
                usersVm.resetValues()
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername").toString()
                })
                HomeMapScreen(user, navController, markersState)
            }
        }
        with(TravelDiaryRoute.SignIn) {
            composable(route) {
                usersVm.resetValues()
                val addUserVm = koinViewModel<AddTravelViewModel>()
                val addUserState by addUserVm.state.collectAsStateWithLifecycle()
                AddUserScreen(
                    state = addUserState,
                    actions = addUserVm.actions,
                    onSubmit = { usersVm.addUser(addUserState.toUser()) },
                    navController = navController,
                    usersVm
                )
            }
        }
        with(TravelDiaryRoute.HomeAddMark) {
            composable(route, arguments) { backStackEntry ->
                // Estrai i parametri dalla back stack entry
                val userUsername = backStackEntry.arguments?.getString("userUsername").orEmpty()
                val latitude = backStackEntry.arguments?.getFloat("latitude") ?: 0f
                val longitude = backStackEntry.arguments?.getFloat("longitude") ?: 0f
                val addMarkerVm = koinViewModel<AddMarkerViewModel>()
                val addMarkerState by addMarkerVm.state.collectAsStateWithLifecycle()

                // Trova l'utente corrispondente allo username, se presente
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername").toString()
                })

                // Qui puoi passare i parametri al tuo schermo o ViewModel se necessario
                HomeAddMarkScreen(
                    user = user,
                    latitude = latitude,
                    longitude = longitude,
                    navController = navController,
                    state = addMarkerState,
                    actions = addMarkerVm.actions,
                    onSubmit = {markersVm.addMarker(addMarkerState.toMarker())}
                )
            }
        }
        with(TravelDiaryRoute.HomeMarks) {
            composable(route) {
                HomeMarksScreen(navController = navController, state = markersState)
            }
        }
    }
}
