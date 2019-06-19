package com.eram.googleplaycrawler

import com.eram.googleplaycrawler.googleplay.GooglePlayAPI
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.util.*

@RestController
class WebServiceController {
    @RequestMapping(path = [PATH_ROOT], method = [RequestMethod.GET])
    fun rootEndPoint(): String {
        var googlePlayAPI = GooglePlayAPI("mreram2@gmail.com", "mre9037103")
//        googlePlayAPI.useragent = profile.getAgent()
//        googlePlayAPI.androidID = profile.getGsfId()
//        googlePlayAPI.token = profile.getToken()
        val l = Locale.getDefault()
        var s = l.language
        if (l.country != null) {
            s = s + "-" + l.country
        }
        googlePlayAPI.localization = s
        googlePlayAPI.login()
        try {
            googlePlayAPI.checkin()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return "debug: ${googlePlayAPI.details("com.likotv")}"
    }

    @RequestMapping(path = [PATH_HOME], method = [RequestMethod.GET])
    fun homeEndPoint(input: String?): String {
        return "hi, this is home page. $input"
    }
}
