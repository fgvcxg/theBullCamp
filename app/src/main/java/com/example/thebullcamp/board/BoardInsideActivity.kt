package com.example.thebullcamp.board

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.thebullcamp.R
import com.example.thebullcamp.comment.CommentLVAdapter
import com.example.thebullcamp.comment.CommentModel
import com.example.thebullcamp.databinding.ActivityBoardInsideBinding
import com.example.thebullcamp.utils.FBAuth
import com.example.thebullcamp.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key : String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        //첫번째 방법으로는 listview에서 있는 데이터 title content time 다 다른 액티비디로 전달해줘서 만들기
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.timeArea.text = title
//        binding.contentArea.text = content
//        binding.timeArea.text = time

//        Log.d(TAG, title)
//        Log.d(TAG, content)
//        Log.d(TAG, time)

        //다이어로그 만들기
        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }


        //두번째 방법
        key = intent.getStringExtra("key").toString()
//        Toast.makeText(this, key,Toast.LENGTH_SHORT).show()

        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {

            insertComment(key)

        }

        getCommentData(key)

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter



    }

    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }

                commentAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)


    }

    private fun insertComment(Key : String){

        val ttt = binding.commentArea.text.toString()

        if(ttt != ""){
            FBRef
                .commentRef
                .child(key)
                .push()
                .setValue(CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getTime()
                ))

            Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show()
            binding.commentArea.setText("")
        }else{
            Toast.makeText(this, "글을 작성해주세요", Toast.LENGTH_SHORT).show()
        }
        
    }


    private fun showDialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정 / 삭제")

       val alertDialog =  mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {

            val intent = Intent(this, BoardEditActivity::class.java)


            intent.putExtra("key", key)
            startActivity(intent)

            Toast.makeText(this,"수정버튼을 눌렀습니다",Toast.LENGTH_SHORT).show()
        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this,"삭제완료",Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun getBoardData(key : String){


            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    try {

                        Log.d(TAG, dataSnapshot.toString())

                        val dataModel = dataSnapshot.getValue(BoardModel::class.java)
//                    Log.d(TAG, dataModel!!.title)

                        binding.textArea.text = dataModel!!.title
                        binding.contentArea.text = dataModel!!.content
                        binding.timeArea.text = dataModel!!.time

                        val myUID = FBAuth.getUid()
                        val writerUID = dataModel.uid

                        if(myUID.equals(writerUID)){

                            binding.boardSettingIcon.isVisible = true
                            Log.w(TAG, "aaaaaaaaaaaaaaa")

                        }else{
                            Log.w(TAG, "vvvvvvvvvvvvvvv")
                        }

                    }catch (e : java.lang.Exception){
                        Log.d(TAG, "삭제완료")
                    }

                    //다른 것과는 다르게 키값과 연결된 값은 한 덩어리이기 때문에 반복문을 돌릴필요가 없다



                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
                }
            }
            FBRef.boardRef.child(key).addValueEventListener(postListener)

        }

    private fun getImageData(key: String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful){

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            }else{

                binding.getImageArea.isVisible = false

            }
        })
    }


}