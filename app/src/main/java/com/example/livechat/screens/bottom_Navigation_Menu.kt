package com.example.livechat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.DestinationScreen
import com.example.livechat.R
import com.example.livechat.ui.theme.navigateTo

enum class bottomNavigationItems(val icon: Int, val destinationScreen: DestinationScreen) {
    CHATLIST(R.drawable.chats, DestinationScreen.Chat),
    STATUSLIST(R.drawable.status, DestinationScreen.Status),
    PROFILELIST(R.drawable.account, DestinationScreen.Profile)
}

@Composable
fun bottom_Navigation_Menu(
    selectedItem: bottomNavigationItems, navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(Color.White)
    ) {
        for (item in bottomNavigationItems.values()) {
            Image(
                painter = painterResource(id = item.icon), contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .weight(1f)
                    .clickable {
                        navigateTo(navController, item.destinationScreen.route)
                    },
                colorFilter = if (item == selectedItem)
                    ColorFilter.tint(color = Color.Black)
                else
                ColorFilter.tint(color = Color.Gray)
            )
        }
    }
}