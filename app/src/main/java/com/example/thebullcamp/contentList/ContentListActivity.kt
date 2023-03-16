package com.example.thebullcamp.contentList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thebullcamp.R
import com.example.thebullcamp.utils.FBAuth
import com.example.thebullcamp.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {

    private lateinit var myRef : DatabaseReference

    //북마크 리스트를 넣을 수 있는 변수 생성
    val bookmarkIdList = mutableListOf<String>()

    // 어뎁터 연결 초기화
    lateinit var rvAdapter: ContentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        val items = ArrayList<ContentModel>()

        //아이템의 키값을 저장하는 것
        val itemKeyList = ArrayList<String>()

        //어뎁터에서 북마크 추가해줬으니 연결하는 이곳에서도 추가
        rvAdapter = ContentRVAdapter(baseContext, items, itemKeyList, bookmarkIdList)

        val database = Firebase.database



        //카테고리 받아오기
        val category = intent.getStringExtra("category")

        //카태고리가 all 이면 contents 가 오게 한다
        // 이런식으로 누른 것을 다 만들 필요 없이 만들 수 있다
        if(category == "categoryall"){
             myRef = database.getReference("contentsAll")

        }else if(category == "categoryct"){
             myRef = database.getReference("contentsCT")
        }else if(category == "categorybb"){
            myRef = database.getReference("contentsBB")
        }else if(category == "categorycf"){
            myRef = database.getReference("contentsCF")
        }else if(category == "categorypl"){
            myRef = database.getReference("contentsPL")
        }
//        else if(category == "categoryrn"){
//            myRef = database.getReference("contentsRN")
//        }


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                //이렇게 하면 모든 데이터가 하나로 나옴
                Log.d("ContentListActivity", dataSnapshot.toString())

                // 모든 데이터가 한번에 나오기에 반복문을 사용해서 하나씩 빼와야 한다
                for(dataModel in dataSnapshot.children){
                    Log.d("ContentListActivity", dataModel.toString())
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)

                    //키값을 넣는다
                    itemKeyList.add(dataModel.key.toString())

                }
                //비동기 형식으로 코드가 진행되기 때문에 리프레쉬를 시켜줘야 한다
                rvAdapter.notifyDataSetChanged()
                Log.d("ContentListActivity", items.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val rv : RecyclerView = findViewById(R.id.rv)
        

        rv.adapter = rvAdapter

        rv.layoutManager = LinearLayoutManager(this)

        getBookmarkData()


        //아이템을 클릭했을때 일어나는 일
//        rvAdapter.itemClick = object :ContentRVAdapter.ItemClick{
//            override fun onClick(view: View, position: Int) {
//                Toast.makeText(baseContext, items[position].title, Toast.LENGTH_SHORT).show()
//
//                val intent = Intent(this@ContentListActivity,ContentShActivity::class.java)
//                intent.putExtra("url",items[position].webUrl)
//                //누른 아이템에 webUrl의 값을 url이란 이름으로 보낸다
//                startActivity(intent)
//            }
//        }

        //        myRef.child("test").setValue(
//            ContentModel("title1","https://postfiles.pstatic.net/MjAyMzAzMDlfMTE0/MDAxNjc4MzY1NDE5ODAx.w7KWdxwyGK-iEGXzd0wQQZrSzhPJbmmayw8sK1_i_U4g.z7N0HPoldxkKO-_6NsnDhFc_pcNnW0XjB5RBBeMALd4g.JPEG.prince0717/bb1.jpg?type=w773",
//                "https://youtu.be/cPjbDo7DF1U")
//        )

//        myRef.push().setValue(
//
//        )



        //DB에 넣기
//        val myRefbb = database.getReference("contentsBB")
//        myRefbb.push().setValue(
//            ContentModel("title1","https://postfiles.pstatic.net/MjAyMzAzMDlfMTE0/MDAxNjc4MzY1NDE5ODAx.w7KWdxwyGK-iEGXzd0wQQZrSzhPJbmmayw8sK1_i_U4g.z7N0HPoldxkKO-_6NsnDhFc_pcNnW0XjB5RBBeMALd4g.JPEG.prince0717/bb1.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//
//        myRefbb.push().setValue(
//            ContentModel("title2","https://postfiles.pstatic.net/MjAyMzAzMDlfMTc2/MDAxNjc4MzY1NDE5ODAy.X1_Is5lpIsbOCHV93VNpnazuM13X7ePvR0lXjc28e7sg.sKeWX-vE3lfpVjhjxvrrIFTXFSg89Wawnnws9Z5Gaasg.JPEG.prince0717/bb2.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//
//        myRefbb.push().setValue(
//            ContentModel("title3","https://postfiles.pstatic.net/MjAyMzAzMDlfNTYg/MDAxNjc4MzY1NDE5ODAx.Z3IX4Ml82iChVoyj1djhc96AvcmCMz4FvRvb_ag53Ikg.lEPA6jM8Hhr-EWVi1SEyt-ZJdz4YfcExHxMGAft_ElQg.JPEG.prince0717/bb3.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//        val myRefcf = database.getReference("contentsCF")
//        myRefcf.push().setValue(
//            ContentModel("title4","https://postfiles.pstatic.net/MjAyMzAzMDlfMjgy/MDAxNjc4MzY1NDk0Mzk0.5b7dZalugoKKbctqM7wdXRbh9iTrWfeeq-RwZxOpBsQg.t2MGHk5ZD9CBlmhLEONrZdU4kcvsB57gJAX-KPJBLOog.JPEG.prince0717/cf1.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//
//        myRefcf.push().setValue(
//            ContentModel("title5","https://postfiles.pstatic.net/MjAyMzAzMDlfNjQg/MDAxNjc4MzY1NDk0Mzk3.xo63E9L2BIDyKQo0rNWfHQdnJ9lDip5eA_dIocXpKWMg.CdFoVMlTcdUr-vVcQdb4aqxr4DHT-OR8TB13yZhwj1cg.JPEG.prince0717/cf2.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//
//        myRefcf.push().setValue(
//            ContentModel("title6","https://postfiles.pstatic.net/MjAyMzAzMDlfMTk4/MDAxNjc4MzY1NDk0Mzk3._p9xP39UoSwjNzvx3YJIdM1qUg9SZELj9qD64ZcXTrkg.QI19apd5mxxvNF_iX_Z8JutHAVlO87_5hhdGEeLG7dUg.JPEG.prince0717/cf3.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//        val myRefct = database.getReference("contentsCT")
//        myRefct.push().setValue(
//            ContentModel("title7","https://postfiles.pstatic.net/MjAyMzAzMDlfMjcg/MDAxNjc4MzY1NTEwMTk3.BOdllGN-GdhtI7h9T-nWCoajummXiP6IbE9xtx11VzUg.OAw7WWGhLoL8o1EM720TD1F4ZcEOlqpVLirk_SOpnzog.JPEG.prince0717/ct1.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//
//        myRefct.push().setValue(
//            ContentModel("title8","https://postfiles.pstatic.net/MjAyMzAzMDlfMTY1/MDAxNjc4MzY1NTEwMTk3.mynuo4AtkeRvojRzAAhZIBoSX8NWqb7qa6k8aTmcMlwg.fnhl2WfJZEihLdmigBIwWJ6pOSLV-py3W1ZEyypW5Esg.JPEG.prince0717/ct2.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//
//        myRefct.push().setValue(
//                ContentModel("title9","https://postfiles.pstatic.net/MjAyMzAzMDlfOTMg/MDAxNjc4MzY1NTEwMTk2.BYXqa1dq8s_dStm17U129KVtDfWGgLnjBhUOKBXvgocg.oXWmL1IDCKdum6Fa1nBFAr35HpyN4jVVTsFMtS5oaYUg.JPEG.prince0717/ct3.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//        val myRefpl = database.getReference("contentsPL")
//        myRefpl.push().setValue(
//            ContentModel("title10","https://postfiles.pstatic.net/MjAyMzAzMDlfMjIy/MDAxNjc4MzY1NTIwNTI2.Jv6fiVIimv3YuohR3jrdULCUjQI5tNGNIuV3qtRQEo0g.ywOXP7WCpoiq7XWUwg1bOwCAtKCZtjcIQKHmpNjuL_sg.JPEG.prince0717/pl1.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//
//        myRefpl.push().setValue(
//            ContentModel("title11","https://postfiles.pstatic.net/MjAyMzAzMDlfMjY1/MDAxNjc4MzY1NTIwNTI1.2G36ZufcwYUI9f6kn0khtcAeivv90Bj2XVfEh1jUWQ8g.oPtkXcYMY5oWakjLNf6vv4zv6neGfbWVzbhxCTWfnjMg.JPEG.prince0717/pl2.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )
//
//        myRefpl.push().setValue(
//            ContentModel("title12","https://postfiles.pstatic.net/MjAyMzAzMDlfMTYw/MDAxNjc4MzY1NTIwNTI3.rDK5-ltTnJ3Xqwki7QotVSlRFraQJuFSkhW1wljZbBMg.28K8IOE7V1sRwc_gyhh_xfObJSSZ2x7ywDUgqs2YaaYg.JPEG.prince0717/pl3.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U")
//        )

        //수동으로 DB에 넣기
//        items.add(ContentModel("title1","https://postfiles.pstatic.net/MjAyMzAzMDlfMTE0/MDAxNjc4MzY1NDE5ODAx.w7KWdxwyGK-iEGXzd0wQQZrSzhPJbmmayw8sK1_i_U4g.z7N0HPoldxkKO-_6NsnDhFc_pcNnW0XjB5RBBeMALd4g.JPEG.prince0717/bb1.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title2","https://postfiles.pstatic.net/MjAyMzAzMDlfMTc2/MDAxNjc4MzY1NDE5ODAy.X1_Is5lpIsbOCHV93VNpnazuM13X7ePvR0lXjc28e7sg.sKeWX-vE3lfpVjhjxvrrIFTXFSg89Wawnnws9Z5Gaasg.JPEG.prince0717/bb2.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title3","https://postfiles.pstatic.net/MjAyMzAzMDlfNTYg/MDAxNjc4MzY1NDE5ODAx.Z3IX4Ml82iChVoyj1djhc96AvcmCMz4FvRvb_ag53Ikg.lEPA6jM8Hhr-EWVi1SEyt-ZJdz4YfcExHxMGAft_ElQg.JPEG.prince0717/bb3.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title4","https://postfiles.pstatic.net/MjAyMzAzMDlfMjgy/MDAxNjc4MzY1NDk0Mzk0.5b7dZalugoKKbctqM7wdXRbh9iTrWfeeq-RwZxOpBsQg.t2MGHk5ZD9CBlmhLEONrZdU4kcvsB57gJAX-KPJBLOog.JPEG.prince0717/cf1.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title5","https://postfiles.pstatic.net/MjAyMzAzMDlfNjQg/MDAxNjc4MzY1NDk0Mzk3.xo63E9L2BIDyKQo0rNWfHQdnJ9lDip5eA_dIocXpKWMg.CdFoVMlTcdUr-vVcQdb4aqxr4DHT-OR8TB13yZhwj1cg.JPEG.prince0717/cf2.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title6","https://postfiles.pstatic.net/MjAyMzAzMDlfMTk4/MDAxNjc4MzY1NDk0Mzk3._p9xP39UoSwjNzvx3YJIdM1qUg9SZELj9qD64ZcXTrkg.QI19apd5mxxvNF_iX_Z8JutHAVlO87_5hhdGEeLG7dUg.JPEG.prince0717/cf3.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title7","https://postfiles.pstatic.net/MjAyMzAzMDlfMjcg/MDAxNjc4MzY1NTEwMTk3.BOdllGN-GdhtI7h9T-nWCoajummXiP6IbE9xtx11VzUg.OAw7WWGhLoL8o1EM720TD1F4ZcEOlqpVLirk_SOpnzog.JPEG.prince0717/ct1.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title8","https://postfiles.pstatic.net/MjAyMzAzMDlfMTY1/MDAxNjc4MzY1NTEwMTk3.mynuo4AtkeRvojRzAAhZIBoSX8NWqb7qa6k8aTmcMlwg.fnhl2WfJZEihLdmigBIwWJ6pOSLV-py3W1ZEyypW5Esg.JPEG.prince0717/ct2.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title9","https://postfiles.pstatic.net/MjAyMzAzMDlfOTMg/MDAxNjc4MzY1NTEwMTk2.BYXqa1dq8s_dStm17U129KVtDfWGgLnjBhUOKBXvgocg.oXWmL1IDCKdum6Fa1nBFAr35HpyN4jVVTsFMtS5oaYUg.JPEG.prince0717/ct3.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title10","https://postfiles.pstatic.net/MjAyMzAzMDlfMjIy/MDAxNjc4MzY1NTIwNTI2.Jv6fiVIimv3YuohR3jrdULCUjQI5tNGNIuV3qtRQEo0g.ywOXP7WCpoiq7XWUwg1bOwCAtKCZtjcIQKHmpNjuL_sg.JPEG.prince0717/pl1.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title11","https://postfiles.pstatic.net/MjAyMzAzMDlfMjY1/MDAxNjc4MzY1NTIwNTI1.2G36ZufcwYUI9f6kn0khtcAeivv90Bj2XVfEh1jUWQ8g.oPtkXcYMY5oWakjLNf6vv4zv6neGfbWVzbhxCTWfnjMg.JPEG.prince0717/pl2.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))
//        items.add(ContentModel("title12","https://postfiles.pstatic.net/MjAyMzAzMDlfMTYw/MDAxNjc4MzY1NTIwNTI3.rDK5-ltTnJ3Xqwki7QotVSlRFraQJuFSkhW1wljZbBMg.28K8IOE7V1sRwc_gyhh_xfObJSSZ2x7ywDUgqs2YaaYg.JPEG.prince0717/pl3.jpg?type=w773",
//            "https://youtu.be/cPjbDo7DF1U"))

    }

    private fun getBookmarkData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //버그가 있다. 데이터가 변경될때 데이터가 누적이 되어 안되는 상황이 있었다
                //그래서 북마크의 데이터를 클리어 해준다
                bookmarkIdList.clear()
                //이렇게 하면 모든 데이터가 하나로 나옴

                // 모든 데이터가 한번에 나오기에 반복문을 사용해서 하나씩 빼와야 한다
                for(dataModel in dataSnapshot.children){
                    //키값을 String 형태로 bookmarkIdList에 저장
                    bookmarkIdList.add(dataModel.key.toString())
                }
                //이제 사용
                rvAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        
        // 데이터를 갖고 오는데 DB에서 FBRef에 있는 bookmarkRef를 FBAuth.getUid()를 부모 삼아 넣는다
        FBRef.bookmarkRef.child(FBAuth.getUid())
            .addValueEventListener(postListener)


    }
}