package com.renzvos.rservice.dataclass

import android.util.Log

class message_receiver private constructor() {
    //private var callbacks : com.renzvos.rservice.callbacks.message_receiver? = null
    private val callbacks : ArrayList<message_reciver_callback_data> = arrayListOf()
    val TAG = "RZmessager"
    val log = false

    fun message(name: String , type : Int , message : Int) {
        if(log) Log.i(TAG, "Onmessage: " + callbacks.size + " callbacks")
        for(callback in callbacks)
        {
            if(log) Log.i(TAG, "message: Checking " + name + " " + type)
            if (callback.name.equals(name) && callback.type == type)
            {
                if(log) Log.i(TAG, "message: Found Source " + name + " " + type)
                callback.callback.message(message)
            }
        }
    }

    fun set_callbacks(name : String , type: Int , callback : com.renzvos.rservice.callbacks.message_receiver)
    {
        val callbackObj = message_reciver_callback_data(name , type , callback)
        callbacks.add(callbackObj)
    }




    companion object {
        @Volatile
        private var instance: message_receiver? = null

        fun getInstance(): message_receiver {
            return instance ?: synchronized(this) {
                instance ?: message_receiver().also { instance = it }
            }
        }
    }
}