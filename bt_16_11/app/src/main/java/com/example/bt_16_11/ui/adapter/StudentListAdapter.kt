package com.example.bt_16_11.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bt_16_11.databinding.ItemStudentBinding
import com.example.bt_16_11.model.Student

class StudentListAdapter(
    private val onStudentClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : ListAdapter<Student, StudentListAdapter.StudentViewHolder>(StudentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads.contains("PAYLOAD_GPA_CHANGED")) {
            holder.updateGpaOnly(getItem(position).gpa)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class StudentViewHolder(
        private val binding: ItemStudentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.tvName.text = student.name
            binding.tvMssv.text = "MSSV: ${student.mssv}"
            binding.tvGpa.text = "GPA: ${student.gpa}"
            binding.tvAvatar.text = student.name.take(1)
            binding.tvAvatar.background.setTint(student.avatarColor)

            binding.root.setOnClickListener { onStudentClick(student) }
            binding.btnDelete.setOnClickListener { onDeleteClick(student) }
        }

        fun updateGpaOnly(newGpa: Double) {
            binding.tvGpa.text = "GPA: $newGpa"
        }
    }

    class StudentDiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Student, newItem: Student): Any? {
            return if (oldItem.gpa != newItem.gpa) {
                "PAYLOAD_GPA_CHANGED"
            } else {
                super.getChangePayload(oldItem, newItem)
            }
        }
    }
}