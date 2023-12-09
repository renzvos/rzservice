package com.renzvos.rservice

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.renzvos.rservice.dataclass.Autowaking_WorkInfo
import com.renzvos.rservice.dataclass.MESSAGES
import com.renzvos.rservice.dataclass.NativeTypes
import com.renzvos.rservice.dataclass.RZ_NativeWorkInfo
import com.renzvos.rservice.dataclass.Types
import com.renzvos.rservice.dataclass.message_receiver
import com.renzvos.rservice.errors.CoroutineWorkNotFound
import com.renzvos.rservice.errors.MultipleService_SameName
import com.renzvos.rservice.errors.RZWorkerNotFound
import com.renzvos.rservice.features.get

class periodic_service(appContext: Context,  params: WorkerParameters) :
    Worker(appContext,params)
{
    private val TAG = "RZ-Periodic"

    // Checks weather coroutine is working
    override fun doWork(): Result {


        val inputData = inputData
        val name = inputData.getString("worker-name")
        val type = inputData.getInt("type" , -1 )
        val native_type = inputData.getInt("native-type" , -1 )
        if (type == -1 || native_type == -1 ) {throw RuntimeException("Type or Nativetype not given")}
        if (name == null) {throw RuntimeException("Cannot find name")}
        val conn = message_receiver.getInstance()
        Log.i(TAG, "Periodic Started: " + System.currentTimeMillis());
        conn.message(name , type , MESSAGES.AUTOWAKING_PERIODIC_STARTED)


        if (!is_coroutine_running(name)) {
            val workerClassName = inputData.getString("worker-package")
            val target_interval = inputData.getInt("worker-interval", 5000)
            if (workerClassName == null) {
                Log.e(TAG, "Worker ClassName not passed")
                return Result.failure()
            }
            //val workerClass = Class.forName(workerClassName).asSubclass(RZWorker::class.java)
            try {
                val workerClass = Class.forName(workerClassName).asSubclass(RZWorker::class.java)
                //val coroutineWork = OneTimeWorkRequest.from(workerClass)

                val passingData = Data.Builder()
                    .putString("worker-name", name)
                    .putInt("type" , type)
                    .putInt("native-type" , NativeTypes.COROUTINE)
                    .build()

                val coroutineWork =
                    OneTimeWorkRequest.Builder(workerClass)
                        .addTag("rz")
                        .addTag("n_" + name)
                        .addTag("t_" + Types.AUTOWAKING_CONTINOUS_WORK)
                        .addTag("nt_" + NativeTypes.COROUTINE)
                        .setInputData(passingData).build()
                Log.i(TAG, "doWork: Coroutine Work Enqued")
                WorkManager.getInstance(applicationContext).enqueue(coroutineWork)
                return Result.success()
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, "Cannot find Worker Package")
            }
        }
        else
        {
            Log.i(TAG, "Periodic Checking: Coroutine Already Working")
        }


            return Result.failure()




    }

    fun is_coroutine_running(name : String): Boolean
    {
        val getter = get(applicationContext)
        var existing_workinfo : Autowaking_WorkInfo? = null
        try { existing_workinfo = getter.find_autowaking(name) }
        catch (_: RZWorkerNotFound) { }

        if(existing_workinfo == null)
        {
            throw RuntimeException("Impossible State: This worker should be present in the list")
        }
        else
        {
            if(existing_workinfo.coroutine_running_service == null)
            {
                Log.i(TAG, "Periodic Checking : Coroutine Not Running")
                return false
            }
            else
            {
                return true
            }
        }

        /*
        try {
            if (get_coroutine_workinfo(name).state == WorkInfo.State.RUNNING) { return true }
        }
        catch (e : CoroutineWorkNotFound)
        {
            return false
        }

        return false
        */

    }


     fun get_coroutine_workinfo(name: String) : WorkInfo
    {
        val workInfos = WorkManager.getInstance(applicationContext).getWorkInfosByTag(name + "_coroutine").get()
        if (workInfos.size == 0){throw CoroutineWorkNotFound()
        }
        if(workInfos.size > 1)
        { throw MultipleService_SameName() }
        return workInfos.get(0)
    }

}