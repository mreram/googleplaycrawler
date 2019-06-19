package com.eram.googleplaycrawler

import com.eram.googleplaycrawler.data.*
import com.eram.googleplaycrawler.googleplay.GooglePlayAPI
import com.eram.googleplaycrawler.googleplay.repo.AndroidApp.downloadApp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
    fun downloadEndPoint(email: String?, password: String?, packageName: String): ResponseEntity<Resource> {
        val playAPI = createConnection(email, password)
        val file = downloadApp(playAPI, packageName, 1)
        val resource = ByteArrayResource(file.readBytes())

        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${file.name}")
        header.add("Cache-Control", "no-cache, no-store, must-revalidate")
        header.add("Pragma", "no-cache")
        header.add("Expires", "0")

        return if (file != null)
            ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource)
        else
            ResponseEntity.noContent().build()
    }

    @RequestMapping(path = [PATH_SEARCH], method = [RequestMethod.GET])
    fun searchEndPoint(email: String?, password: String?, query: String): String {
        val playAPI = createConnection(email, password)
        return "debug: ${playAPI.searchApp(query)}"
    }


    @RequestMapping(path = [PATH_HOME, PATH_ROOT], method = [RequestMethod.GET])
    fun homeEndPoint(input: String?): String {
        return "hi, this is home page. ${cryptProperties}"
    }

    fun createConnection(email: String?, password: String?): GooglePlayAPI {
        val mEmail = email ?: "YOUR_EMAIL"
        val mPassword = password ? "YOUR_PASS"
        val googlePlayAPI = GooglePlayAPI(cryptProperties, mEmail, mPassword)

        val l = Locale.getDefault()
        var s = l.language
        if (l.country != null) {
            s = s + "-" + l.country
        }
        googlePlayAPI.localization = s
        googlePlayAPI.login()


        val currentUser = authRepository.findByEmail(mEmail)

        if (currentUser == null) {
            System.out.println("createConnection: new login")
            try {
                googlePlayAPI.checkin()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            googlePlayAPI.uploadDeviceConfig()
            val auth = Auth(1, googlePlayAPI.token, googlePlayAPI.useragent, googlePlayAPI.androidID, mEmail)
            authRepository.save(auth)
        } else {
            googlePlayAPI.androidID = currentUser.androidId
            googlePlayAPI.token = currentUser.token
            googlePlayAPI.useragent = currentUser.userAgent
            googlePlayAPI.email = currentUser.email
            System.out.println("createConnection: exists in database, auth:$currentUser")
        }

        return googlePlayAPI
    }


}
