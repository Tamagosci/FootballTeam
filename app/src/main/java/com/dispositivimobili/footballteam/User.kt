package com.dispositivimobili.footballteam

class User {

    var name: String = ""
    var surname: String = ""
    var email: String = ""
    var phone: String = ""
    var password: String = ""

    constructor(){
    }

    constructor(name: String, surname: String, email: String, phone: String, password: String) {
        this.name = name
        this.surname = surname
        this.email = email
        this.phone = phone
        this.password = password
    }

}