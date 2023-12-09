package com.renzvos.rservice.dataclass

class Autowaking_WorkInfo(
    workInfo: RZ_WorkInfo ,
    var periodic_waking_service : RZ_NativeWorkInfo?,
    var coroutine_running_service : RZ_NativeWorkInfo?,
    ) :

    RZ_WorkInfo(workInfo.name , workInfo.type,) {

    init {
        if(periodic_waking_service == null && coroutine_running_service == null)
        {
            throw RuntimeException("Cannot Create AutoWakingWorkInfo, both worker is null")
        }

        if(periodic_waking_service != null)
        if(periodic_waking_service!!.nativeType != NativeTypes.PERIODIC)
        { throw RuntimeException("Wrong Native Type")}

        if(coroutine_running_service != null)
        if(coroutine_running_service!!.nativeType != NativeTypes.COROUTINE)
        { throw RuntimeException("Wrong Native Type")}

    }



}