package com.eram.googleplaycrawler

import com.eram.googleplaycrawler.data.*
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

    @Autowired
    lateinit var authRepository: AuthRepository

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


    @RequestMapping(path = [PATH_HOME, PATH_ROOT], method = [RequestMethod.GET])
    fun homeEndPoint(input: String?): String {
        return "hi, this is home page. ${cryptProperties}"
    }

    fun createConnection(): GooglePlayAPI {


        val googlePlayAPI = GooglePlayAPI(cryptProperties, "mreram2@gmail.com", "mre9037103")
        val l = Locale.getDefault()
        var s = l.language
        if (l.country != null) {
            s = s + "-" + l.country
        }
        googlePlayAPI.localization = s
        googlePlayAPI.login()


        if (authRepository.count() == 0L) {
            System.out.println("createConnection: new login")
            try {
                googlePlayAPI.checkin()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            googlePlayAPI.uploadDeviceConfig()
            val auth = Auth(1, googlePlayAPI.token, googlePlayAPI.useragent, googlePlayAPI.androidID)
            authRepository.save(auth)
        } else {
            val auth = authRepository.findAll().first()
            googlePlayAPI.androidID = auth.androidId
            googlePlayAPI.token = auth.token
            googlePlayAPI.useragent = auth.userAgent
            System.out.println("createConnection: exists in database, auth:$auth")
        }

        return googlePlayAPI
    }

}
