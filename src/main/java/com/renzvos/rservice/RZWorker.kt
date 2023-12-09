package com.renzvos.rservice

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.renzvos.rservice.dataclass.MESSAGES
import com.renzvos.rservice.dataclass.message_receiver
import kotlinx.coroutines.delay

 abstract class RZWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
    params
){
    private val TAG = "RZ-Main"
    abstract suspend fun work()




    
    override suspend fun doWork(): Result {
        Log.i(TAG, "Coroutine Started")
        val inputData = inputData
        val name = inputData.getString("worker-name")
        val type = inputData.getInt("type" , -1 )
        val native_type = inputData.getInt("native-type" , -1 )
        if (type == -1 || native_type == -1 ) {throw RuntimeException("Type or Nativetype not given")}
        if (name == null) {throw RuntimeException("Cannot find name")}

        val conn = message_receiver.getInstance()
        conn.message(name , type , MESSAGES.AUTOWAKING_COROUTINE_STARTED)
        Log.i(TAG, "doWork: Coroutine Sending Message")



        work()

        conn.message(name , type , MESSAGES.AUTOWAKING_COROUTINE_ENDED)
        Log.i(TAG, "Work Loop Ended")
        return Result.success()
    }




}