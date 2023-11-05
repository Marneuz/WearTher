package com.marneux.marneweather.domain.cajondesastre.location

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val LocalDateTime.hourStringInTwelveHourFormat: String
    get(){
        val dateTimeFormatter = DateTimeFormatter.ofPattern("hh a")

        return format(dateTimeFormatter).let{time ->
            if(time.startsWith("0")) time.replaceFirst('0', ' ')
            else time
        }
    }