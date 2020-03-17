package com.hotmart.sparkle.pocplayer

import android.util.Log
import kotlin.time.ClockMark
import kotlin.time.ExperimentalTime
import kotlin.time.MonoClock

@ExperimentalTime
object Timer {
    private lateinit var  monoClock : ClockMark

    fun mark(message : String ?= "mark"){
        if(::monoClock.isInitialized.not()){
            monoClock = MonoClock.markNow()
        }

        Log.e("Timer", "$message: ${monoClock.elapsedNow()}")
    }
}