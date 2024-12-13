package com.example.dermaaid.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dermaaid.domain.DoctorsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeViewModel() : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _doctors = MutableLiveData<MutableList<DoctorsModel>>()

    val doctors: LiveData<MutableList<DoctorsModel>> = _doctors

    fun loadDoctors() {
        val Ref = firebaseDatabase.getReference("Doctors")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<DoctorsModel>()

                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(DoctorsModel::class.java)

                    if (list != null) {
                        lists.add(list)
                    }
                }
                _doctors.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}