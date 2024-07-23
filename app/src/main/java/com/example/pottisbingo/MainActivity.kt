package com.example.pottisbingo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pottisbingo.model.Member
import com.example.pottisbingo.navigation.GameNavHost
import com.example.pottisbingo.ui.screens.MainScreenViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

const val TAG = "Gamebb"

class MainActivity : ComponentActivity(), FirebaseAuth.AuthStateListener {
    private lateinit var navHostController: NavHostController
    private lateinit var viewModel: MainScreenViewModel
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        Log.d(TAG, "AuthUI result: $result")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]
        setContent {
            viewModel.member.value?.let {
                navHostController = rememberNavController()
                GameNavHost(navHostController = navHostController, viewModel = viewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        auth.currentUser?.also { firebaseUser ->
            Log.d(TAG, "currentUser: ${firebaseUser.uid}")
            Log.d(TAG, "currentUser: ${firebaseUser.displayName}")
            firebaseUser.displayName?.run {
                FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(firebaseUser.uid)
                    .child("displayName")
                    .setValue(this)
                    .addOnCompleteListener {
                        Log.d(TAG, "done")
                    }
            }

            // Member listening
            FirebaseDatabase.getInstance().getReference("users")
                .child(firebaseUser.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        viewModel.member.value = snapshot.getValue(Member::class.java)
                        viewModel.member.value?.uid = firebaseUser.uid
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        } ?: run {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
            signInLauncher.launch(signInIntent)
        }
    }
}
