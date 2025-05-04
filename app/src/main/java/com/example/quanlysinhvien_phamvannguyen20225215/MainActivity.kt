package com.example.quanlysinhvien_phamvannguyen20225215

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: StudentAdapter
    private var students = ArrayList<Student>()
    private var selectedPosition = -1

    companion object {
        const val REQUEST_ADD = 100
        const val REQUEST_UPDATE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listViewStudents)
        adapter = StudentAdapter(this, students)
        listView.adapter = adapter
        registerForContextMenu(listView)

        listView.setOnItemClickListener {_, view, position, _ ->
            selectedPosition = position
            view.showContextMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_student) {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra("students", students)
            startActivityForResult(intent, REQUEST_ADD)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle("Chọn thao tác")
        menu?.add("Cập nhật")
        menu?.add("Xoá")
        menu?.add("Gọi điện")
        menu?.add("Gửi email")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val student = students[selectedPosition]
        when (item.title) {
            "Cập nhật" -> {
                val intent = Intent(this, UpdateStudentActivity::class.java)
                intent.putExtra("students", students)
                intent.putExtra("student", student)
                intent.putExtra("position", selectedPosition)
                startActivityForResult(intent, REQUEST_UPDATE)
            }

            "Xoá" -> {
                AlertDialog.Builder(this)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá sinh viên này?")
                    .setPositiveButton("Có") {_, _ ->
                        students.removeAt(selectedPosition)
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("Không", null)
                    .show()
            }

            "Gọi điện" -> {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${student.phoneNumber}")
                startActivity(callIntent)
            }

            "Gửi email" -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:${student.email}")
                startActivity(emailIntent)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val student = data.getSerializableExtra("student") as Student
            when (requestCode) {
                REQUEST_ADD -> students.add(student)
                REQUEST_UPDATE -> {
                    val position = data.getIntExtra("position", -1)
                    if (position != -1) students[position] = student
                }
            }
            adapter.notifyDataSetChanged()
        }
    }
}