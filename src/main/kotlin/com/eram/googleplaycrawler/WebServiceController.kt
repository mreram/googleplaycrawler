package com.eram.googleplaycrawler

import com.eram.googleplaycrawler.data.PATH_DOWNLOAD
import com.eram.googleplaycrawler.data.PATH_HOME
import com.eram.googleplaycrawler.data.PATH_ROOT
import com.eram.googleplaycrawler.data.PATH_SEARCH
import com.eram.googleplaycrawler.googleplay.GooglePlayAPI
import com.eram.googleplaycrawler.googleplay.repo.AndroidApp.downloadApp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.util.*

@RestController
class WebServiceController {
    @Autowired
    lateinit var cryptProperties: CryptProperties


    @RequestMapping(path = [PATH_DOWNLOAD], method = [RequestMethod.GET])
    fun downloadEndPoint(packageName: String): String {
        val playAPI = createConnection()
        downloadApp(playAPI, packageName, 1)
        return "debug: successfully download."
    }

    @RequestMapping(path = [PATH_SEARCH], method = [RequestMethod.GET])
    fun searchEndPoint(query: String): String {
        val playAPI = createConnection()
        return "debug: ${playAPI.searchApp(query)}"
    }

    fun createConnection(): GooglePlayAPI {
        val googlePlayAPI = GooglePlayAPI(cryptProperties,"mreram2@gmail.com", "mre9037103")
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
        googlePlayAPI.uploadDeviceConfig()
        return googlePlayAPI
    }



    @RequestMapping(path = [PATH_HOME, PATH_ROOT], method = [RequestMethod.GET])
    fun homeEndPoint(input: String?): String {
        return "hi, this is home page. ${cryptProperties}"
    }
}
