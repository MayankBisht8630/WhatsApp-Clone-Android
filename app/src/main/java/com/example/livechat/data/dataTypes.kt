package com.example.livechat.data

data class userData(
    var userId: String? = "",
    var name: String? = "",
    var number: String? = "",
    var imageUrl: String? = ""
) {
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl,
    )
}

data class chatData(
    val chatId : String? = "",
    val user1 : chatUser = chatUser(),
    val user2 : chatUser = chatUser(),
)

data class chatUser(
    val userId: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val number: String? = "",
)

data class message(
    var sendBy :String? = "",
    var message :String? = "",
    var timestamp: String? = ""
)