package com.dispositivimobili.footballteam

class Player {

    var name: String = ""
    var surname: String = ""
    var date: String = ""
    var phone: String = ""
    var ruolo: String = ""
    var results: String = ""
    var certification: String = ""
    var idnumero: String = ""

    constructor(){
    }

    constructor(name: String, surname: String, date: String, phone: String, ruolo: String, results: String, certification: String, idnumero: String) {
        this.name = name
        this.surname = surname
        this.date = date
        this.phone = phone
        this.ruolo = ruolo
        this.results = results
        this.certification = certification
        this.idnumero = idnumero
    }

    fun set(newPlayer: Player) {
        this.name = newPlayer.name
        this.surname = newPlayer.surname
        this.ruolo = newPlayer.ruolo
    }
}