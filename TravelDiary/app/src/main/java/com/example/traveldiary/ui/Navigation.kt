package com.example.traveldiary.ui

import com.example.traveldiary.ui.screens.homeMarkDetail.HomeMarkDetailScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.traveldiary.Position
import com.example.traveldiary.data.database.Favorite
import com.example.traveldiary.data.database.Marker
import com.example.traveldiary.data.models.Theme
import com.example.traveldiary.ui.screens.homeAddMark.AddMarkerViewModel
import com.example.traveldiary.ui.screens.homeAddMark.HomeAddMarkScreen
import com.example.traveldiary.ui.screens.login.LogInScreen
import com.example.traveldiary.ui.screens.signin.AddUserScreen
import com.example.traveldiary.ui.screens.signin.AddTravelViewModel
import com.example.traveldiary.ui.screens.login.LoginViewModel
import com.example.traveldiary.ui.screens.homeMap.HomeMapScreen
import com.example.traveldiary.ui.screens.homeMarks.HomeMarksScreen
import com.example.traveldiary.ui.screens.homeProfile.HomeProfileScreen
import com.example.traveldiary.ui.screens.homeSettings.HomeSettingsScreen
import org.koin.androidx.compose.koinViewModel

sealed class TravelDiaryRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object LogIn : TravelDiaryRoute("log-in", "log-in")

    data object HomeMap : TravelDiaryRoute(
        "home/map/{userUsername}/{latitude}/{longitude}",
        "homePage",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("latitude") { type = NavType.FloatType },
            navArgument("longitude") { type = NavType.FloatType }
        )
    ) {
        fun buildRoute(userUsername: String, latitude: Float?, longitude: Float?)
            = "home/map/$userUsername/$latitude/$longitude"
        fun buildWithoutPosition (userUsername: String) = "home/map/$userUsername/0/0"

        fun buildWithoutAnything() = "home/map/null/0/0"
    }

    data object HomeMarks : TravelDiaryRoute(
        "home/mark/{userUsername}",
        "marks",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String) = "home/mark/$userUsername"
    }

    data object HomeSettings : TravelDiaryRoute(
        "home/settings/{userUsername}",
        "Settings",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String) = "home/settings/$userUsername"
    }

    data object HomeProfile : TravelDiaryRoute(
        "home/profile/{userUsername}",
        "Profile",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String) = "home/profile/$userUsername"
    }

    data object HomeFavorites : TravelDiaryRoute(
        "home/mark/favorite/{userUsername}",
        "favoriteMarks",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String) = "home/mark/favorite/$userUsername"
    }

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

    data object HomeMarkDetail : TravelDiaryRoute (
        "home/mark/detail/{userUsername}/{latitude}/{longitude}",
        "detail",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("latitude") { type = NavType.FloatType },
            navArgument("longitude") { type = NavType.FloatType }
        )
    ) {
        fun buildRoute(userUsername: String, latitude: Float, longitude: Float) =
            "home/mark/detail/$userUsername/$latitude/$longitude"
    }

    data object SignIn : TravelDiaryRoute("sign-in", "sign-in")

    companion object {
        val routes = setOf(
            LogIn, HomeMap, SignIn, HomeMarks, HomeAddMark,
            HomeMarkDetail, HomeFavorites, HomeSettings, HomeProfile
        )
    }
}


@Composable
fun TravelDiaryNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onPosition : () -> Unit,
    position: Position,
    themeState: ThemeState,
    onThemeSelected: (Theme) -> Unit,
    onCamera: () -> Unit
) {
    val usersVm = koinViewModel<UsersViewModel>()
    val usersState by usersVm.state.collectAsStateWithLifecycle()

    val markersVm = koinViewModel<MarkersViewModel>()
    val markersState by markersVm.state.collectAsStateWithLifecycle()

    val favoritesVm = koinViewModel<FavoritesViewModel>()
    val favoritesState by favoritesVm.state.collectAsStateWithLifecycle()

    val defaultLatitude by position.latitude.observeAsState()
    val defaultLongitude by position.longitude.observeAsState()

    var userDefault by remember{ mutableStateOf("null")}

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
                var latitude = backStackEntry.arguments?.getFloat("latitude") ?: defaultLatitude
                latitude = if(latitude == 0f) defaultLatitude else latitude
                var longitude = backStackEntry.arguments?.getFloat("longitude") ?: defaultLongitude
                longitude = if (longitude == 0f) defaultLongitude else longitude
                var userName =  backStackEntry.arguments?.getString("userUsername") ?: userDefault
                userName = if (userName == "null") userDefault else userName
                userDefault = userName
                val user = requireNotNull(usersState.users.find {
                    it.username == userName
                })
                HomeMapScreen(
                    user,
                    navController,
                    markersState,
                    latitude!!,
                    longitude!!,
                    onPosition = {onPosition()})
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
            composable(route, arguments) { backStackEntry ->
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername").toString()
                })
                HomeMarksScreen(navController = navController, state = markersState, user = user)
            }
        }
        with(TravelDiaryRoute.HomeSettings) {
            composable(route, arguments) {backStackEntry ->
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername").toString()
                })
                HomeSettingsScreen(
                    navController = navController,
                    user = user,
                    state = themeState,
                    onThemeSelected = onThemeSelected
                    )
            }
        }
        with(TravelDiaryRoute.HomeProfile) {
            composable(route, arguments) { backStackEntry ->
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername").toString()
                })
                HomeProfileScreen(navController = navController, user = user, onCamera = onCamera)
            }
        }
        with(TravelDiaryRoute.HomeFavorites) {
            composable(route, arguments) {backStackEntry ->
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername").toString()
                })
                val listFavorite = favoritesState.favorites.filter { it.userId == user.id }
                val listFavoriteMarker: MutableList<Marker> = mutableListOf()
                listFavorite.forEach{fav ->
                    listFavoriteMarker.add(requireNotNull(markersState.markers.find { fav.markerId == it.id }))
                }
                HomeMarksScreen(
                    navController = navController,
                    state = MarkersState(listFavoriteMarker),
                    user = user
                )
            }
        }
        with(TravelDiaryRoute.HomeMarkDetail) {
            composable(route, arguments) {backStackEntry ->
                val latitude = backStackEntry.arguments?.getFloat("latitude") ?: 0f
                val longitude = backStackEntry.arguments?.getFloat("longitude") ?: 0f
                val marker = requireNotNull(markersState.markers.find {
                    it.latitude == latitude && it.longitude == longitude
                })
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername").toString()
                })
                val favorite = (favoritesState.favorites.find {
                    it.userId == user.id && it.markerId == marker.id
                })


                HomeMarkDetailScreen(
                    navController = navController,
                    marker = marker,
                    user = user,
                    favorite = favorite != null,
                    onFavorite = fun(userId :Int, markerId : Int) {
                        favoritesVm.addFavorite(Favorite(userId, markerId))
                    },
                    onSFavorite = fun(userId :Int, markerId : Int) {
                        val toDelete = requireNotNull(favoritesState.favorites.find {
                            it.userId == userId && it.markerId == markerId
                        })
                        favoritesVm.deleteFavorite(toDelete)
                    }
                )
            }
        }

    }
    LaunchedEffect(defaultLatitude, defaultLongitude) {
        if (defaultLatitude != null && defaultLongitude != null
            && defaultLatitude != 41.9028f && defaultLongitude != 12.4964f) {
            navController.navigate(TravelDiaryRoute.HomeMap.buildRoute(userDefault, defaultLatitude, defaultLongitude))
        }
    }
}
