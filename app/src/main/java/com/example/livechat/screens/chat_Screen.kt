package com.example.livechat.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.livechat.LCViewModel

@Composable
fun chat_Screen(navController: NavController,vm:LCViewModel) {
    Text(text = "This Is A Chat Screen")
    bottom_Navigation_Menu(selectedItem = bottomNavigationItems.CHATLIST, navController =navController )
}