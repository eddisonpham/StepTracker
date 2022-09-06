package com.example.steptracker

class UserModel {
    var id: Int
    var email: String
    var password: String
    var steps: Int
    constructor(id: Int, email: String, password: String, steps: Int){
        this.id = id
        this.email = email
        this.password = password
        this.steps = steps
    }
}