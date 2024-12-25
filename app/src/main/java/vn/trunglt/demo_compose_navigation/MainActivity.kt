package vn.trunglt.demo_compose_navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import vn.trunglt.demo_compose_navigation.ui.screens.ConfirmDialog
import vn.trunglt.demo_compose_navigation.ui.screens.HomeScreen
import vn.trunglt.demo_compose_navigation.ui.screens.ProfileScreen
import vn.trunglt.demo_compose_navigation.ui.screens.RunOneLaunchedEffect
import vn.trunglt.demo_compose_navigation.ui.theme.DemocomposenavigationTheme

@Serializable
object Home

@Serializable
object ConfirmDialog

@Serializable
class Profile(
    val shortFullName: String, val company: String
)

@Serializable
data class Profile2(
    val shortFullName: String, val company: String
)

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        val routes = controller
            .currentBackStack.value
            .map { it.destination.route }
            .joinToString(", ")

        println("BackStack: $routes")
    }
    val topLevelRoutes =
        listOf(
            TopLevelRoute(
                name = "Home",
                route = Home,
                icon = ImageVector.vectorResource(id = R.drawable.ic_launcher_background)
            ),
            TopLevelRoute(
                name = "Profile",
                route = Profile("trunglt", "vnpay"),
                icon = ImageVector.vectorResource(id = R.drawable.ic_launcher_background)
            ),
            TopLevelRoute(
                name = "Profile 2",
                route = Profile2("lttrung", "bosch"),
                icon = ImageVector.vectorResource(id = R.drawable.ic_launcher_background)
            ),
        )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.navigationBarsPadding()
            ) {
                val navBackStackEntry = navController.currentBackStackEntry
                val currentDestination = navBackStackEntry?.destination
                topLevelRoutes.forEach { topLevelRoute ->
                    BottomNavigationItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(route = topLevelRoute.route::class)
                        } == true, onClick = {
                            navController.navigate(topLevelRoute.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }, icon = {
                            Icon(topLevelRoute.icon, topLevelRoute.name)
                        }, label = {
                            Text(text = topLevelRoute.name)
                        })
                }
            }
        }
    ) { innerPadding ->
        var currentMillis by remember {
            mutableLongStateOf(System.currentTimeMillis())
        }
        RunOneLaunchedEffect {
            while (true) {
                delay(1000)
                currentMillis = System.currentTimeMillis()
            }
        }
        val navGraph = navController.createGraph(
            startDestination = Home,
            builder = {
                composable<Home> { backstackEntry ->
                    HomeScreen(
                        onSeeProfileClick = {
                            navController.navigate(
                                route = createProfile()
                            )
                        })
                }
                composable<Profile> { backstackEntry ->
                    val profile: Profile = backstackEntry.toRoute()
                    ProfileScreen(
                        profile = profile,
                        currentMillis = currentMillis,
                        onProfileClick = {
                            navController.navigate(
                                route = ConfirmDialog
                            )
                        }
                    )
                }
                composable<Profile2> { backstackEntry ->
                    val profile2 = backstackEntry.toRoute<Profile2>()
                    val profile = Profile(profile2.shortFullName, profile2.company)
                    ProfileScreen(
                        profile = profile,
                        currentMillis = currentMillis,
                        onProfileClick = {

                        }
                    )
                }
                dialog<ConfirmDialog> {
                    ConfirmDialog(
                        title = "Notification",
                        content = "Are you sure you want to do this?",
                        btnConfirm = "Agree",
                        btnCancel = "Cancel",
                        onCancel = {
                            navController.popBackStack()
                        },
                        onConfirm = {
                            navController.popBackStack()
                        })
                }
            }
        )
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            graph = navGraph,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            })
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemocomposenavigationTheme {
                App()
            }
        }
    }
}

fun createProfile(): Profile = Profile(
    shortFullName = "trunglt",
    company = "vnpay"
)
