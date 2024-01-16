package com.example.livechat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.Provides

@Module
@InstallIn(ViewModelComponent::class)
class hiltModule {
    @Provides
    fun provideAuthentiction() : FirebaseAuth = Firebase.auth

    @Provides
    fun provideFireStore() : FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideStorage() : FirebaseStorage = Firebase.storage
}