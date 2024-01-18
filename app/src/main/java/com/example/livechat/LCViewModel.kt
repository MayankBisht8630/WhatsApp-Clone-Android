package com.example.livechat

import android.net.Uri
import android.service.autofill.UserData
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.livechat.data.CHATS
import com.example.livechat.data.Event
import com.example.livechat.data.USER_NODE
import com.example.livechat.data.chatData
import com.example.livechat.data.chatUser
import com.example.livechat.data.userData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {
    var inProcessChats = mutableStateOf(false)
    var inProgress = mutableStateOf(false)
    var eventMutbleState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    var userData = mutableStateOf<userData?>(null)
    val chats = mutableStateOf<List<chatData>>(listOf())

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun populatsChats() {
        inProcessChats.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handelException(error)
            }
            if (value != null) {
                chats.value = value.documents.mapNotNull {
                    it.toObject<chatData>()
                }
                inProcessChats.value = false
            }
        }
    }

    fun signUp(name: String, number: String, email: String, password: String) {

        if (name.isEmpty() or email.isEmpty() or number.isEmpty() or password.isEmpty()) {
            handelException(customMessage = "Please Fill All The Fields !!")
            return
        }

        inProgress.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn.value = true
                        createOrUpdateProfile(name, number, imageUrl = null)
                    } else {
                        handelException(it.exception, customMessage = "Sign Up Failed")
                    }
                }
            } else {
                handelException(customMessage = "Number Already Exist")
                inProgress.value = false
            }
        }
    }

    fun handelException(exception: Exception? = null, customMessage: String = "") {
        Log.d("LiveChatApp", " Live Chat Exception ", exception)
        exception?.printStackTrace()

        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMessage else customMessage
        eventMutbleState.value = Event(message)
        inProgress.value = false
    }

    fun logIn(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handelException(customMessage = "Please Fill All The Fields")
            return
        } else {
            inProgress.value = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    signIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                } else {
                    handelException(exception = it.exception, customMessage = "Login Failed")
                }
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageUrl = it.toString())
        }
    }

    fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProgress.value = false
        }
            .addOnFailureListener {
                handelException(it)
            }
    }

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        var uid = auth.currentUser?.uid
        val userData = userData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )
        uid?.let {
            inProgress.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
//                    update user data
                    inProgress.value = false
                } else {
                    db.collection(USER_NODE).document(uid).set(userData)
                    inProgress.value = false
                    getUserData(uid)
                }
            }
                .addOnFailureListener {
                    handelException(it, "Cannot Retrieve User")
                }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->

            if (error != null) {
                handelException(error, "Cannot Retrieve User")
            }

            if (value != null) {
                var user = value.toObject<userData>()
                userData.value = user
                inProgress.value = false

                populatsChats()
            }
        }
    }

    fun logOut() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        eventMutbleState.value = Event("Logged Out")
    }

    fun onAddChat(number: String) {
        if (number.isEmpty() or !number.isDigitsOnly()) {
            handelException(customMessage = "Number Must Contain Digits Only")
        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user1.number", userData.value?.number),
                        Filter.equalTo("user2.number", number)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                handelException(customMessage = "Number Not Found")
                            } else {
                                val chatPatners = it.toObjects<userData>()[0]
                                val id = db.collection(CHATS).document().id
                                val chat = chatData(
                                    chatId = id,
                                    chatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.imageUrl,
                                        userData.value?.number
                                    ),
                                    chatUser(
                                        chatPatners.userId,
                                        chatPatners.name,
                                        chatPatners.imageUrl,
                                        chatPatners.number
                                    )
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                        }
                        .addOnFailureListener {
                            handelException(it)
                        }
                } else {
                    handelException(customMessage = "Chat Already Exist !!")
                }
            }
        }
    }
}
