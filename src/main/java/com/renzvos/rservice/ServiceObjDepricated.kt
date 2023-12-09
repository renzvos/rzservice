package com.renzvos.rservice

/*
class ServiceObjDepricated <T : RZWorker>
    (val name : String,
    val worker_class: Class<T>,
     val context: Context,
     val type : Int
     )
{
    private val TAG = "RZService"
    private val statusCallbacks : ArrayList<status_callback> = arrayListOf()
    val conn = message_receiver.getInstance()


    init {
/*
        val periodicWorkInfos = WorkManager.getInstance(context).getWorkInfosByTag(name + "_periodic").get()
        val corountineworkInfos = WorkManager.getInstance(context).getWorkInfosByTag(name + "_coroutine").get()

        if(periodicWorkInfos.size == 0)
        { Log.i(TAG, "Periodic Worker Not Found: ") }
        else if (periodicWorkInfos.size == 1)
        {
            val periodicWorkInfo = get_periodic_workinfo()
            if(periodicWorkInfo.state == WorkInfo.State.RUNNING)
            { Log.i(TAG, "Periodic Worker Found Worker State: Running ") }
            else
            { Log.i(TAG, "Periodic Worker Found Worker State: " + periodicWorkInfo.state.name) }

        }
        else{throw MultipleService_SameName()
        }

        if(corountineworkInfos.size == 0)
        { Log.i(TAG, "Coroutine Worker Not Found: ") }
        else if (corountineworkInfos.size == 1)
        {
            val coroutineWorkInfo = get_coroutine_workinfo()
            if(coroutineWorkInfo.state == WorkInfo.State.RUNNING)
            { Log.i(TAG, "Coroutine Worker Found Worker State: Running ") }
            else
            { Log.i(TAG, "Coroutine Worker Found Worker State: " + coroutineWorkInfo.state.name) }
        }
        else{throw MultipleService_SameName()
        }

*/
    }




    fun fulltime_loop(interval: Int)
    {
        run_periodic(interval)
    }

    fun fulltime()
    {

    }

    fun setOnChange_fulltimeloop(fulltimeLoopCallback: fulltime_loop_callback)
    { conn.set_callbacks(fulltimeLoopCallback) }

    private fun periodic_waking(coroutine_interval: Int)
    {
        Log.i(TAG, "run_periodic: Start")
        if (!is_periodic_running())
        {
            val periodic_service_repeatition : Long = 2L
            val periodic_service_interval = TimeUnit.SECONDS

            //periodic checking of status - works if idle and off-activity
            val passingdata = Data.Builder().
            putString("worker-package" , worker_class.name).
            putString("worker-name" , name).
            putInt("worker-interval" , coroutine_interval).
            build()

            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<periodic_service>(periodic_service_repeatition, periodic_service_interval).
                setInputData(passingdata).
                addTag(name + "_periodic").
                build()

            Log.i(TAG, "fulltime_loop: Periodic Work Request Builded - Calling")



            WorkManager.getInstance(context).enqueue(periodicWorkRequest)
            Log.i(TAG, "fulltime_loop: Periodic Work Request - Enqued")
        }
        else{
            kill_periodic()
            run_periodic(interval)
        }

    }

    fun wake()
    {


    }

    fun kill()
    {
        kill_coroutine()
        kill_periodic()
        WorkManager.getInstance(context).pruneWork()
    }

    fun exists():Boolean
    {
        return !(is_coroutine_running() == false && is_periodic_running() == false)
    }

    private fun is_periodic_running(): Boolean {
        try {
            if (get_periodic_workinfo().state == WorkInfo.State.RUNNING) {
                return true
            }
        }catch (e : PeriodicWorkNotFound) {return false}
            return false
    }

    private fun is_coroutine_running() : Boolean
    {   try {
        if (get_coroutine_workinfo().state == WorkInfo.State.RUNNING) { return true }
        }catch (e: CoroutineWorkNotFound){return false}
        return false }


    private fun get_periodic_workinfo() : WorkInfo
    {
        val workInfos = WorkManager.getInstance(context).getWorkInfosByTag(name + "_periodic").get()
        if (workInfos.size == 0){throw PeriodicWorkNotFound() }
        if(workInfos.size >  1)
        { throw MultipleService_SameName() }
        return workInfos.get(0)
    }

    private fun get_coroutine_workinfo() : WorkInfo
    {
        val workInfos = WorkManager.getInstance(context).getWorkInfosByTag(name + "_coroutine").get()
        if (workInfos.size == 0){throw CoroutineWorkNotFound()
        }
        if(workInfos.size > 1)
        { throw MultipleService_SameName() }
        return workInfos.get(0)
    }

    private fun kill_periodic()
    {
        WorkManager.getInstance(context).cancelAllWorkByTag(name + "_periodic")
        Log.i(TAG, "kill_periodic: " + name)
    }

    private fun kill_coroutine()
    {
        WorkManager.getInstance(context).cancelAllWorkByTag(name + "_coroutine")
        Log.i(TAG, "kill_coroutine: " + name)
        for (callback in statusCallbacks)
        {callback.not_running()}
    }



    fun new(name : String , worker_class: Class<T>, context: Context, ) : new<T> {
        return new(name , worker_class ,context)
    }






}


 */
