package com.example.marvel_app.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseRepository {
    private var auth: FirebaseAuth = Firebase.auth

    private var _user: FirebaseUser? = null
    val user: FirebaseUser?
        get() = _user

    init {
        _user = auth.currentUser
    }

    fun signInWithEmail(email: String, password: String): Task<AuthResult> {
        val result = auth.signInWithEmailAndPassword(email, password)
        _user = auth.currentUser
        return result
    }

    fun signUpWithEmail(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun signOut() {
        auth.signOut()
        _user = null
    }
}