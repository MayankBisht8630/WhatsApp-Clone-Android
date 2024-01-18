package com.example.livechat.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.livechat.LCViewModel

@Composable
fun single_Chat_Screen(navController: NavController, vm : LCViewModel, chatId :String) {
    Text(text = chatId)
}