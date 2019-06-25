package com.eram.googleplaycrawler.data

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Auth")
class Auth(id: Int? = null, token: String? = null, userAgent: String? = null, androidId: String? = null, email: String? = null) {

    @Column
    @Id
    var id: Int? = id

    @Column
    var token: String? = token

    @Column
    var androidId: String? = androidId

    @Column
    var userAgent: String? = userAgent

    @Column
    var email: String? = email

    override fun toString(): String {
        return "Auth(id=$id, token=$token, androidId=$androidId, userAgent=$userAgent, email=$email)"
    }


}
