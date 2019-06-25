package com.eram.googleplaycrawler.data

import org.springframework.data.jpa.repository.JpaRepository


interface AuthRepository : JpaRepository<Auth, Int>
