package com.example.quanlysinhvien_phamvannguyen20225215

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class StudentAdapter(context: Context, private val students: ArrayList<Student>) : ArrayAdapter<Student>(context, 0, students) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false)
        }

        val student = students[position]
        itemView!!.findViewById<TextView>(R.id.tvName).text = student.name
        itemView.findViewById<TextView>(R.id.tvID).text = student.studentID

        return itemView
    }
}