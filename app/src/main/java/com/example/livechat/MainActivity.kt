package com.example.livechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.livechat.screens.chat_Screen
import com.example.livechat.screens.login_Screen
import com.example.livechat.screens.profile_Screen
import com.example.livechat.screens.signup_Screen
import com.example.livechat.screens.status_Screen
import com.example.livechat.ui.theme.LiveChatTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

//All Available Screens
sealed class DestinationScreen(var route: String) {
    object Signup : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object Chat : DestinationScreen("chat")
    object SingleChat : DestinationScreen("singlechat/{chatId}") {
        fun createRoute(id : String) = "SingleChat/$id"
    }
    object Status : DestinationScreen("status")
    object SingleStatus : DestinationScreen("singlestatus/{userId}") {
        fun createRoute(userid : String) = "SingleStatus/$userid"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    chatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun chatAppNavigation(){

        var navController = rememberNavController()
        var vm = hiltViewModel<LCViewModel>()
        NavHost(navController = navController, startDestination = DestinationScreen.Signup.route ){
            composable(DestinationScreen.Signup.route){
                signup_Screen(navController,vm)
            }

            composable(DestinationScreen.Login.route){
                login_Screen(navController = navController,vm = vm)
            }

            composable(DestinationScreen.Chat.route){
                chat_Screen(navController = navController,vm = vm)
            }

            composable(DestinationScreen.Status.route){
                status_Screen(navController = navController,vm = vm)
            }

            composable(DestinationScreen.Profile.route){
                profile_Screen(navController = navController,vm = vm)
            }
        }
    }
}
