package com.devmasterteam.tasks.view

import android.app.Person
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.service.system.BiometricHelper
import com.devmasterteam.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)


        viewModel.verifyAuthentication()

        // Observadores
        observe()
    }

    override fun onClick(v: View) {
        if(v.id == R.id.button_login){
            handleLogin()
        } else if(v.id == R.id.text_register){
            startRegister()
        }

    }

    private fun observe() {

        viewModel.login.observe(this){

            if(it.showStatus()){

                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()

            } else {
                Toast.makeText(applicationContext,it.showMessage(),Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.loggedUser.observe(this){

            if(it){

                biometricAuthentication()




            }

        }


    }

    private fun handleLogin(){
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        viewModel.doLogin(email,password)
    }

    private fun biometricAuthentication(){

        if(BiometricHelper.isBiometricAvaible(this)){

            val executor = ContextCompat.getMainExecutor(this)

            val bioPrompt = BiometricPrompt(this, executor,
                object: BiometricPrompt.AuthenticationCallback(){

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }

                } )

            val info = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticação")
                .setSubtitle("autenticação de login")
                .setNegativeButtonText("Cancelar")
                .build()

            bioPrompt.authenticate(info)
        }

    }

    private fun startRegister(){
        startActivity(Intent(applicationContext, RegisterActivity::class.java))
        finish()
    }

}