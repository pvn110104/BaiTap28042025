package com.example.quanlysinhvien_phamvannguyen20225215

import java.io.Serializable

data class Student(
    var name: String,
    var studentID: String,
    var email: String,
    var phoneNumber: String
) : Serializable