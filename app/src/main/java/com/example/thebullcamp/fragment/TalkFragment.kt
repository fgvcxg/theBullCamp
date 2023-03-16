package com.example.thebullcamp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.thebullcamp.R
import com.example.thebullcamp.board.BoardInsideActivity
import com.example.thebullcamp.board.BoardLVAdapter
import com.example.thebullcamp.board.BoardModel
import com.example.thebullcamp.board.BoardWriteActivity
import com.example.thebullcamp.contentList.ContentModel
import com.example.thebullcamp.databinding.FragmentTalkBinding
import com.example.thebullcamp.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private  val TAG = TalkFragment::class.java.simpleName

    private  lateinit var boardLVAdapter : BoardLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

//        val boardList = mutableListOf<BoardModel>()
//        boardList.add(BoardModel("a","b", "C","d"))

        boardLVAdapter = BoardLVAdapter(boardDataList)
        binding.boardListView.adapter = boardLVAdapter
        

        //두번째 방법으로는 Firebase에 있는 board에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법

        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }


        binding.boardListView.setOnItemClickListener { adapterView, view, i, l ->

            //첫번째 방법으로는 listview에서 있는 데이터 title content time 다 다른 액티비디로 전달해줘서 만들기
//            val intent = Intent(context, BoardInsideActivity::class.java)
//            intent.putExtra("title", boardDataList[i].title)
//            intent.putExtra("content", boardDataList[i].content)
//            intent.putExtra("time", boardDataList[i].time)
//
//            startActivity(intent)

            //두번째 방법으로는 Firebase에 있는 board에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법
            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[i])
            startActivity(intent)

        }


        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }

        getFBBoardData()

        return binding.root
    }

    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                // 모든 데이터가 한번에 나오기에 반복문을 사용해서 하나씩 빼와야 한다
                for(dataModel in dataSnapshot.children){

                    Log.d(TAG, dataModel.toString())

//                    dataModel.key

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())


                }
                boardKeyList.reverse()
                boardDataList.reverse()
                //비동기 형식으로 코드가 진행되기 때문에 리프레쉬를 시켜줘야 한다
                boardLVAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }

}