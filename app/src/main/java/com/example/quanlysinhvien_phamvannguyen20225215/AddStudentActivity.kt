package com.example.quanlysinhvien_phamvannguyen20225215

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editStudentID: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhoneNumber: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonBack: Button
    private lateinit var existStudent: ArrayList<Student>

    private fun showConfirmExitDialog() {
        val name = editName.text.toString().trim()
        val studentID = editStudentID.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val phoneNumber = editPhoneNumber.text.toString().trim()

        if (name.isEmpty() && studentID.isEmpty() && email.isEmpty() && phoneNumber.isEmpty()) {
            finish()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có muốn lưu thông tin vừa nhập?")
            .setPositiveButton("Có") {_, _ ->
                buttonSave.performClick()
            }
            .setNegativeButton("Không") {_, _ ->
                finish()
            }
            .setNeutralButton("Huỷ", null)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        editName = findViewById(R.id.editName)
        editStudentID = findViewById(R.id.editStudentID)
        editEmail = findViewById(R.id.editEmail)
        editPhoneNumber = findViewById(R.id.editPhoneNumber)
        buttonSave = findViewById(R.id.buttonSave)
        buttonBack = findViewById(R.id.buttonBack)
        existStudent = intent.getSerializableExtra("students") as ArrayList<Student>

        buttonSave.setOnClickListener {
            val name = editName.text.toString().trim()
            val studentID = editStudentID.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val phoneNumber = editPhoneNumber.text.toString().trim()

            if (name.isEmpty() || studentID.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val emailPattern = android.util.Patterns.EMAIL_ADDRESS
            val phoneNumberPattern = Regex("^\\d{10}\$")

            if (!emailPattern.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!phoneNumberPattern.matches(phoneNumber)) {
                Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (existStudent.any { it.studentID == studentID }) {
                Toast.makeText(this, "Mã số sinh viên đã tồn tại!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (existStudent.any { it.email == email }) {
                Toast.makeText(this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (existStudent.any { it.phoneNumber == phoneNumber }) {
                Toast.makeText(this, "Số điện thoại đã được sử dụng!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(name, studentID, email, phoneNumber)
            val intent = Intent()
            intent.putExtra("student", student)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        buttonBack.setOnClickListener {
            showConfirmExitDialog()
        }
    }
}