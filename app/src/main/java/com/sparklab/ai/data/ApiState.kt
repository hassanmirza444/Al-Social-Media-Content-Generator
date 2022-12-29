package com.sparklab.ai.data

import com.sparklab.ai.OpenAIResponse

sealed class ApiState{

    object Empty : ApiState()
    object Loading : ApiState()
    class Success(val data:OpenAIResponse):ApiState()
    class Failure(val msg:Throwable) : ApiState()
}

