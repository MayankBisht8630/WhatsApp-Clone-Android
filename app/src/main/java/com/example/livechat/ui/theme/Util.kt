package com.example.livechat.ui.theme


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel
import androidx.compose.foundation.shape.CircleShape as CircleShape

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}

@Composable
fun commonProgressbar() {
    Row(modifier = Modifier
        .alpha(0.5f)
        .background(Color.LightGray)
        .clickable(enabled = false) {}
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun commonDivider() {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .alpha(.3f)
            .padding(top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun commonImage(
    data: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val painter = rememberImagePainter(data = data)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = contentScale
    )
}

@Composable
fun checkSignedIn(vm: LCViewModel, navController: NavController) {
    val alreadySignIn = remember {
        mutableStateOf(false)
    }
    val signIn = vm.signIn.value
    if (signIn && !alreadySignIn.value) {
        alreadySignIn.value = true
        navController.navigate(DestinationScreen.Chat.route) {
            popUpTo(0)
        }
    }
}

@Composable
fun titleText(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun commonRow(
    imageUrl: String?,
    name: String?,
    onItemClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable { onItemClick.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        val painter = rememberImagePainter(data = imageUrl)
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(8.dp).size(50.dp).clip(CircleShape).background(Color.Gray)
        )

//        imageUrl?.let {
//            commonImage(
//                data = it,
//                modifier = Modifier
//                    .padding(8.dp)
//                    .size(50.dp)
//                    .clip(CircleShape)
//                    .background(
//                        Color.Red
//                    )
//            )
//        }

        Text(
            text = name ?: "---",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

