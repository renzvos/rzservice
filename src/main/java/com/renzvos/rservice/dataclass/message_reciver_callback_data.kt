package com.renzvos.rservice.dataclass

import com.renzvos.rservice.callbacks.message_receiver

class message_reciver_callback_data
    (
            val name : String,
            val type : Int,
            val callback : message_receiver
            ){

}