package com.renzvos.rservice.features

import android.content.Context
import android.util.Log
import androidx.work.WorkManager
import com.renzvos.rservice.AutoWakingService
import com.renzvos.rservice.dataclass.Autowaking_WorkInfo
import com.renzvos.rservice.dataclass.NativeTypes
import com.renzvos.rservice.dataclass.RZ_NativeWorkInfo
import com.renzvos.rservice.dataclass.RZ_WorkInfo
import com.renzvos.rservice.dataclass.Types
import com.renzvos.rservice.errors.RZWorkerNotFound
import com.renzvos.rservice.errors.UnknownTaginRZWorker

class get(context: Context) {

    val workinfos = WorkManager.getInstance(context).getWorkInfosByTag("rz").get()
    val nativeWorkInfos : ArrayList<RZ_NativeWorkInfo> = arrayListOf()
    val rzWorkInfos : ArrayList<RZ_WorkInfo> = arrayListOf()
    val distinct_names :  ArrayList<String> = arrayListOf()
    val Autowaking_WorkInfos : ArrayList<Autowaking_WorkInfo> = arrayListOf()

    val TAG = "RZService-Get"
    val log = false


    init {




        for( nn_workinfo in workinfos)
        {
            var name : String? = null
            var type : Int? = null
            var native_type : Int? = null
            val tags = nn_workinfo.tags

            for (tag in tags)
            {
                if(tag.equals("rz") or tag.startsWith("com."))
                {//Ignore
                }
                else if(tag.startsWith("n_"))
                {
                    name =  tag.substring(2)
                }
                else if(tag.startsWith("t_"))
                {
                    type = tag.substring(2).toInt()
                }
                else if(tag.startsWith("nt_"))
                {
                    native_type = tag.substring(3).toInt()
                }
                else
                {
                    throw UnknownTaginRZWorker(tag)
                }
            }

            if (name == null) { throw RuntimeException("Name not found in RZ Worker") }
            if (type == null) { throw RuntimeException("Type not found in RZ Worker") }
            if (native_type == null) { throw RuntimeException("Native Type not found in RZ Worker") }

            logi(TAG, "Found: " + name + "," +  type + ","  + native_type)
            val workinfo = RZ_NativeWorkInfo(name , type , native_type , nn_workinfo.id)
            append_nativeworkInfo(workinfo)

            if (!distinct_names.contains(name)) {
                distinct_names.add(name)
            }

        }


        for (name in distinct_names)
        {
            var type : Int? = null
            for(workinf in nativeWorkInfos)
            {
                if (workinf.name.equals(name))
                {
                    if(type == null)
                        type = workinf.type
                    else{
                        if(type != workinf.type)
                        {
                            throw RuntimeException("Workers found with different RZTypes")
                        }
                    }
                }
            }

            when(type)
            {
                Types.AUTOWAKING_CONTINOUS_WORK->
                {
                    logi(TAG, "Considering ACWork: " + name )
                    val rzWorkinfo = RZ_WorkInfo(name , type)
                    var periodic_autowaking_worker : RZ_NativeWorkInfo? = null
                    for(workinf in nativeWorkInfos)
                    {
                        if(workinf.type == type && name.equals(workinf.name) && workinf.nativeType == NativeTypes.PERIODIC)
                        {
                            if (periodic_autowaking_worker == null) {
                                logi(TAG, "Considering ACWork: " + name + ": Periodic Found")
                                periodic_autowaking_worker = workinf
                            }
                            else {throw  RuntimeException("Multiple Periodic Waking Worker for Coroutine")}
                        }
                    }

                    var coroutine_worker : RZ_NativeWorkInfo? = null
                    for(workinf in nativeWorkInfos)
                    {
                        if(workinf.type == type && name.equals(workinf.name) && workinf.nativeType == NativeTypes.COROUTINE)
                        {
                            if (coroutine_worker == null) {
                                coroutine_worker = workinf
                                logi(TAG, "Considering ACWork: " + name + ": Coroutine Found")
                            }
                            else {throw  RuntimeException("Multiple Corountine Worker")}
                        }
                    }

                    val autowaking_worker = Autowaking_WorkInfo(rzWorkinfo, periodic_autowaking_worker,coroutine_worker)

                    rzWorkInfos.add(autowaking_worker)
                    Autowaking_WorkInfos.add(autowaking_worker)

                }


                else ->
                {
                    throw RuntimeException("Not developed")
                }
            }
        }

    }

    fun find_autowaking(name : String) : Autowaking_WorkInfo
    {
        for (autoworker in Autowaking_WorkInfos)
        {
            if(autoworker.name == name)
            {
                return autoworker
            }
        }

        throw RZWorkerNotFound()
    }

    fun append_nativeworkInfo(nativeWorkInfo: RZ_NativeWorkInfo)
    {
        for (workinfo in nativeWorkInfos)
        {
            if (workinfo.name.equals(nativeWorkInfo.name)
                && workinfo.type == nativeWorkInfo.type
                && workinfo.nativeType == nativeWorkInfo.nativeType)
                throw RuntimeException("Duplicate Worker Found")
        }
        nativeWorkInfos.add(nativeWorkInfo)
    }

    fun logi(tag : String , message : String){
        if(log)
        {
            Log.i(tag,message )
        }
    }




}