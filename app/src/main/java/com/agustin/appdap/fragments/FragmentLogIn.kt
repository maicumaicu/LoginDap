package com.agustin.appdap.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.agustin.appdap.viewModel.FragmentLogInViewModel
import com.agustin.appdap.R
import com.agustin.appdap.clases.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FragmentLogIn : Fragment() {


    lateinit var v: View
    lateinit var EdtTxtEmail: EditText
    lateinit var EdtTxtPassword: EditText
    lateinit var BtnContinuar: Button
    private lateinit var auth: FirebaseAuth


    override fun onStart() {
        super.onStart()
        BtnContinuar.setOnClickListener {
            LogIn()
        }
    }

    companion object {
        fun newInstance() = FragmentLogIn()
    }

    private lateinit var viewModel: FragmentLogInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_log_in_fragment, container, false)
        EdtTxtEmail = v.findViewById(R.id.EdtTxtEmail)
        EdtTxtPassword = v.findViewById(R.id.EdtTxtPassword)
        BtnContinuar = v.findViewById<Button>(R.id.Btn)
        auth = Firebase.auth
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentLogInViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun LogIn() {
        var Email = EdtTxtEmail.text.toString()
        var Password = EdtTxtPassword.text.toString()
        if (Email.isNotEmpty() && Password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var user = auth.currentUser
                    var email = user?.email ?: ""
                    var uid = user?.uid ?: ""
                    if (email.isNotEmpty() && uid.isNotEmpty()){
                        var action = FragmentLogInDirections.actionFragmentLogInToFragmentHub(
                            Usuario(email, uid)
                        )
                        v.findNavController().navigate(action)
                    }
                } else {
                    Snackbar.make(v, "Usuario Incorrecto", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}