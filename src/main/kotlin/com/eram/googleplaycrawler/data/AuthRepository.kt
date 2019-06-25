package com.eram.googleplaycrawler.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface AuthRepository : JpaRepository<Auth, Int>{
    @Query("SELECT t FROM Auth t where t.email = :email")
    fun findByEmail(@Param("email") email: String): Auth?
}
