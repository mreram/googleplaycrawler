package com.eram.googleplaycrawler.data

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Auth")
class Auth(id: Int? = null, token: String? = null, userAgent: String? = null, androidId: String? = null) {

    @Column
    @Id
    var id: Int? = id

    @Column
    var token: String? = token

    @Column
    var androidId: String? = androidId

    @Column
    var userAgent: String? = userAgent



}
