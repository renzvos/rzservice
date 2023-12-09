package com.renzvos.rservice

typealias  onStart_fun = ()-> Unit
typealias  onEnd_fun = ()-> Unit

open class ServiceObj {
    private val OnStartFuns: MutableList<onStart_fun> = mutableListOf()
    private val OnEndFuns: MutableList<onEnd_fun> = mutableListOf()
    var running : Boolean = false


    fun OnStart( callback : onStart_fun ){ OnStartFuns.add(callback) }
    fun OnEnd  (callback : onEnd_fun)    { OnEndFuns.add(callback)  }


    protected fun Started() {
        for (funs in OnStartFuns){ funs() }
        running = true
    }
    protected fun Ended() {
        for (funs in OnEndFuns) { funs() }
        running = false
    }



}