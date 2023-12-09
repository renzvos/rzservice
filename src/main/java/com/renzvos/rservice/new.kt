package com.renzvos.rservice

import android.content.Context

class new <T : RZWorker>
    (val name : String,
     val worker_class: Class<T>,
     val context: Context,)
{

    var type : Int? = null

    fun periodic_work() : new<T>
    {
        type = 0
        return this
    }

    fun continous_work() : new<T> {
        type = 1
        return this
    }

    fun autowaking_continous_work() : AutoWakingService<T>
    {
        val autoWakingService = AutoWakingService(name, worker_class, context)
        return autoWakingService
    }


    /*

    fun run() : ServiceObj<T>{


        if (type == null){throw RuntimeException("Type Not set")}
        else {


            //val ServiceObj = ServiceObj(name, worker_class, context, type!!)
            //return ServiceObj
        }

    }

     */




}