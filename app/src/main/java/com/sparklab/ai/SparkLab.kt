package com.sparklab.ai

import android.app.Application
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import dagger.hilt.android.HiltAndroidApp
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@HiltAndroidApp
class SparkLab :Application() {

    override fun onCreate() {
        super.onCreate()

        FacebookSdk.setApplicationId("5894516690609304")
        FacebookSdk.setClientToken("cc829f89949ba76d6700939ba22e21cb")
        FacebookSdk.fullyInitialize();
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
            FacebookSdk.setAutoLogAppEventsEnabled(true)
            FacebookSdk.setAdvertiserIDCollectionEnabled(true)
            FacebookSdk.setAutoInitEnabled(true)
        }



        printHashKey()
    }

    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                "com.sparklab.ai",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }
}