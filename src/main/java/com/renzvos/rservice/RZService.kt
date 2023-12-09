package com.renzvos.rservice

import android.content.Context


class RZService  {


    companion object {

        fun<T : RZWorker> new(name : String, worker_class: Class<T>, context: Context,) : new<T>
        {
            return com.renzvos.rservice.new(name , worker_class ,context)
        }







    }

}