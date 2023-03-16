package com.example.thebullcamp.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.thebullcamp.R
import com.example.thebullcamp.contentList.BookmarkModel
import com.example.thebullcamp.databinding.ActivityBoardWriteBinding
import com.example.thebullcamp.utils.FBAuth
import com.example.thebullcamp.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    private var imageTF = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            Log.d(TAG, title)
            Log.d(TAG, content)

            //board
            // - key
            //  - boardModel(title, content, uid, time)

            //이미지가 올라갈때 텍스트로 된 이미지가 올라가기 전에 
            //먼저 생성되는 키 값을 갖고 싶어서 아래처럼 하면 작성이 된다
            //데이터 베이스에서 키값을 생성해서 한다
            //파이어베이스 store에 이미지를 저장하고 싶음
            //만약 내가 게시글을 클릭했을 때, 게시글에 대한 정보를 받아와야 할때 
            //이미지 이름에 대한 정보를 모르기 때문에
            //이미지 이름의 문서의 key 값으로 해줘서 이미지에 대한 정보를 찾기 쉽게 해놓음
            val key = FBRef.boardRef
                .push().key.toString()

            FBRef.boardRef
                //.push()   //이렇게 하면 그냥 넣는 것이지만 텍스트로 넣을때 키값으로 관리한 것처럼 이미지도 같이 키값으로 관리하기 위해서 이렇게 한것이다
                .child(key)
                .setValue(BoardModel(title, content, uid, time))

            Toast.makeText(this, "글작성 완료", Toast.LENGTH_SHORT).show()

            if (imageTF == true){
                imageUpload(key)
            }


            finish()

        }

        binding.imageArea.setOnClickListener {
            imageTF = true
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }

    }

    private fun imageUpload(key : String){

        val storage = Firebase.storage

        val imageView = binding.imageArea

        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100){
            //이렇게 하면 핸드폰 안에 갤러리를 볼 수 있고 거기서 선택한 것을 imageView 에 넣는다
            binding.imageArea.setImageURI(data?.data)
        }

    }
}