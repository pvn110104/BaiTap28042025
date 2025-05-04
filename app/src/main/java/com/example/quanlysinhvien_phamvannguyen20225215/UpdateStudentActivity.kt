package com.example.quanlysinhvien_phamvannguyen20225215

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateStudentActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editStudentID: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhoneNumber: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonBack: Button
    private lateinit var textViewUpdate: TextView
    private lateinit var student: Student
    private lateinit var existStudent: ArrayList<Student>
    private var position: Int = -1
    private lateinit var originalStudent: Student

    private fun showConfirmExitDialog() {
        val name = editName.text.toString().trim()
        val studentID = editStudentID.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val phoneNumber = editPhoneNumber.text.toString().trim()

        val isUnchaged = name == originalStudent.name &&
                studentID == originalStudent.studentID &&
                email == originalStudent.email &&
                phoneNumber == originalStudent.phoneNumber

        if (isUnchaged) {
            finish()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có muốn lưu thông tin vừa chỉnh sửa?")
            .setPositiveButton("Có") {_, _ ->
                buttonUpdate.performClick()
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
        buttonUpdate = findViewById(R.id.buttonSave)
        buttonUpdate.text = "Cập nhật"
        buttonBack = findViewById(R.id.buttonBack)
        textViewUpdate = findViewById(R.id.tvAdd)
        textViewUpdate.text = "Cập nhật sinh viên"

        student = intent.getSerializableExtra("student") as Student
        position = intent.getIntExtra("position", -1)
        originalStudent = intent.getSerializableExtra("student") as Student
        existStudent = intent.getSerializableExtra("students") as ArrayList<Student>

        editName.setText(student.name)
        editStudentID.setText(student.studentID)
        editEmail.setText(student.email)
        editPhoneNumber.setText(student.phoneNumber)

        buttonUpdate.setOnClickListener {
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

            val filteredList = existStudent.filterIndexed { index, _ -> index != position }
            val isDuplicateID = filteredList.any { it.studentID == studentID }
            val isDuplicateEmail = filteredList.any { it.email == email }
            val isDuplicatePhone = filteredList.any { it.phoneNumber == phoneNumber }

            if (isDuplicateID) {
                Toast.makeText(this, "Mã số sinh viên đã tồn tại!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isDuplicateEmail) {
                Toast.makeText(this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isDuplicatePhone) {
                Toast.makeText(this, "Số điện thoại đã được sử dụng!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = Student(name, studentID, email, phoneNumber)
            val result = Intent()
            result.putExtra("student", updatedStudent)
            result.putExtra("position", position)
            setResult(RESULT_OK, result)
            finish()
        }

        buttonBack.setOnClickListener {
            showConfirmExitDialog()
        }
    }
}