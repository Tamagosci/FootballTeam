package com.dispositivimobili.footballteam

import java.util.*

class Player {

    var name: String = ""
    var surname: String = ""
    var date: String = ""
    var phone: String = ""
    var ruolo: String = ""
    var results: String = ""
    var certification: String = ""

    constructor(){
    }

    constructor(name: String, surname: String, date: String, phone: String, ruolo: String, results: String, certification: String) {
        this.name = name
        this.surname = surname
        this.phone = phone
        this.ruolo = ruolo
        this.results = results
        this.certification = certification
    }
}