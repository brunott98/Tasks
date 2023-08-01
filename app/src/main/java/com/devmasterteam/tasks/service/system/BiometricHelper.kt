package com.devmasterteam.tasks.service.system

import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.os.Build
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG

class BiometricHelper{

    companion object {
        fun isBiometricAvaible(context: Context): Boolean{

            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){

                return false
            }

            val biometricManager = androidx.biometric.BiometricManager.
            from(context)

            when(biometricManager.canAuthenticate(BIOMETRIC_STRONG)){

                BiometricManager.BIOMETRIC_SUCCESS -> return true
                androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> return false
                androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> return false
                androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> return false

            }

            return false

        }
    }

}