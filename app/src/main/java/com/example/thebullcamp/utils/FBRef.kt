package com.example.thebullcamp.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object{
        private val database = Firebase.database

        //전체적으로 불러오는 것을 알아보자
        val categoryct = database.getReference("contentsCT")
        val categorybb = database.getReference("contentsBB")
        val categorycf = database.getReference("contentsCF")
        val categorypl = database.getReference("contentsPL")


        val bookmarkRef = database.getReference("bookmark_list")

        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")
    }
}