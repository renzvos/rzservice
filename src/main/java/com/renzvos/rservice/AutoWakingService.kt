package com.renzvos.rservice

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.renzvos.rservice.dataclass.Autowaking_WorkInfo
import com.renzvos.rservice.dataclass.MESSAGES
import com.renzvos.rservice.dataclass.NativeTypes
import com.renzvos.rservice.dataclass.RZ_NativeWorkInfo
import com.renzvos.rservice.dataclass.RZ_WorkInfo
import com.renzvos.rservice.dataclass.Types
import com.renzvos.rservice.dataclass.message_receiver
import com.renzvos.rservice.errors.RZWorkerNotFound
import com.renzvos.rservice.features.get
import java.util.concurrent.TimeUnit

class AutoWakingService <T : RZWorker>
    (
    val name : String,
    val worker_class: Class<T>,
    val context: Context,
            ) : ServiceObj()
{
    val TAG = "RZAutoWakingService"
    //var periodicWorkInfo : RZ_NativeWorkInfo? = null
    //var coroutineWorkInfo : RZ_NativeWorkInfo? = null
    val conn = message_receiver.getInstance()
    val WorkManager = androidx.work.WorkManager.getInstance(context)
    val type : Int = Types.AUTOWAKING_CONTINOUS_WORK

        init {

            val existing_workinfo = get() // Its better to get everytime

            conn.set_callbacks(name , Types.AUTOWAKING_CONTINOUS_WORK , object : com.renzvos.rservice.callbacks.message_receiver
            {
                override fun message(message : Int) {
                    when(message)
                    {
                        MESSAGES.AUTOWAKING_PERIODIC_STARTED->
                        {
                            Log.i(TAG, "message: Message Recived")
                        }

                        MESSAGES.AUTOWAKING_COROUTINE_STARTED->
                        {
                            Log.i(TAG, "message: Message Recived")
                            Started()
                        }

                        MESSAGES.AUTOWAKING_COROUTINE_ENDED->
                        {
                            kill_coroutine()
                        }


                    }
                }

            })






            if(existing_workinfo == null)
            {
                Log.i(TAG, "Checking Service : Not Running: ")
                // periodicWorkInfo = periodic_start()
            }
            else
            {
                Log.i(TAG, "Checking Service: Runnning")
                var flag = false
                if(existing_workinfo!!.periodic_waking_service  == null)
                {
                    //periodicWorkInfo = existing_workinfo!!.periodic_waking_service
                    Log.i(TAG, "Repair Needed: Periodic Not Running")
                    flag = true
                }

                if(existing_workinfo!!.coroutine_running_service == null)
                {
                    //coroutineWorkInfo = existing_workinfo!!.coroutine_running_service
                    Log.i(TAG, "Repair Needed: Coroutine Not Running")
                    flag = true
                }

                running = !flag
            }



        }

    private fun get() : Autowaking_WorkInfo?
    {
        val getter = get(context) // Its better to get everytime
        var existing_workinfo : Autowaking_WorkInfo? = null
        try { existing_workinfo = getter.find_autowaking(name) }
        catch (_: RZWorkerNotFound) { }
        return existing_workinfo
    }

    fun start()
    {
        val existing_workinfo = get() // Its better to get everytime

        if(existing_workinfo == null)
        {
            Log.i(TAG, "start: Not Running - Starting Periodic")
            periodic_start()}
        else
        {
            if (existing_workinfo.periodic_waking_service == null)
            {
                Log.i(TAG, "start: Running - Repairing Periodic ")
                periodic_start()}


            if (existing_workinfo.coroutine_running_service == null)
            {
                Log.i(TAG, "start: Running - Repairing Coroutine by Restarting Periodic")
                kill_periodic()
                periodic_start()
            }

        }

    }

    fun kill()
    {
        kill_periodic()
        kill_coroutine()
        WorkManager.cancelAllWorkByTag("n_" + name )
        WorkManager.pruneWork()
        Ended()
    }


    private fun periodic_start() : RZ_NativeWorkInfo {

        Log.i(TAG, "run_periodic: Start")
        val periodic_service_repeatition: Long = 2L
        val periodic_service_interval = TimeUnit.SECONDS

        //periodic checking of status - works if idle and off-activity
        val passingdata = Data.Builder().putString("worker-package", worker_class.name)
            .putString("worker-name", name)
            .putInt("type" , type)
            .putInt("native-type" , NativeTypes.PERIODIC)
            //.putInt("worker-interval", coroutine_interval)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<periodic_service>(
                periodic_service_repeatition,
                periodic_service_interval
                ).setInputData(passingdata)
                .addTag("rz")
                .addTag("n_" + name)
                .addTag("t_" + Types.AUTOWAKING_CONTINOUS_WORK)
                .addTag("nt_" + NativeTypes.PERIODIC)
                .build()

        Log.i(TAG, "fulltime_loop: Periodic Work Request Builded - Calling")

        val nativeWorkInfo = RZ_NativeWorkInfo(name , Types.AUTOWAKING_CONTINOUS_WORK , NativeTypes.PERIODIC , periodicWorkRequest.id)

        WorkManager.enqueue(periodicWorkRequest)
        Log.i(TAG, "fulltime_loop: Periodic Work Request - Enqued")

        return nativeWorkInfo

    }

    private fun kill_periodic()
    {
        val existing_workinfo = get() // Its better to get everytime
        if(existing_workinfo!= null)
        {
            if(existing_workinfo.periodic_waking_service != null)
            {
                WorkManager.cancelWorkById(existing_workinfo.periodic_waking_service!!.native_Id)
                WorkManager.pruneWork()
                Log.i(TAG, "kill_periodic: Periodic Found Killed")
            }else { Log.i(TAG, "kill_periodic: Periodic not running nothing to kill")}
        }else{ Log.i(TAG, "kill_periodic: Service is not running")}
        
        
    }

    private fun kill_coroutine()
    {
        val existing_workinfo = get() // Its better to get everytime
        if(existing_workinfo!= null)
        {
            if(existing_workinfo.coroutine_running_service != null)
            {
                WorkManager.cancelWorkById(existing_workinfo.coroutine_running_service!!.native_Id)
                WorkManager.pruneWork()
            }else { Log.i(TAG, "kill_coroutine: Coroutine not running nothing to kill")}
        }else{ Log.i(TAG, "kill_coroutine: Service is not running")}
    }










}