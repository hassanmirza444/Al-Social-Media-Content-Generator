package com.sparklab.ai

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


import com.github.willena.phoneinputview.CountryConfigurator
import com.github.willena.phoneinputview.CountryConfigurator.HintType
import com.sparklab.ai.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this
        binding.toolbar.title = "SparkLab"

        val config = CountryConfigurator()
        config.displayFlag = true
        config.displayCountryCode = true
        config.displayDialingCode = true
        config.phoneNumberHintType = HintType.MOBILE //Set the phone number type that will be displayed as hint (MOBILE, FIXED, NONE)
        config.defaultCountry = "DE" //Set the default country that will be selected when loading

        binding.phoneId.config = config
    }



}