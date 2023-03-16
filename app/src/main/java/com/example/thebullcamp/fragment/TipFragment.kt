package com.example.thebullcamp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.thebullcamp.R
import com.example.thebullcamp.contentList.ContentListActivity
import com.example.thebullcamp.databinding.FragmentTipBinding


class TipFragment : Fragment() {

    private lateinit var binding : FragmentTipBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tip, container, false)

        //보디빌딩의 관한 루틴
        binding.catbb.setOnClickListener{
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category","categorybb")
            startActivity(intent)

        }
        
        //맨몸운동에 관한 루틴 보기
        binding.catct.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category","categoryct")
            startActivity(intent)
        }
        
        //크로스핏에 관한 루틴 보기
        binding.catcf.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category","categorycf")
            startActivity(intent)
        }

        //파워리프팅에 관한 루틴 보기
        binding.catpl.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category","categorypl")
            startActivity(intent)
        }

        //런닝에 관한 루틴 보기
//        binding.catrn.setOnClickListener {
//            val intent = Intent(context, ContentListActivity::class.java)
//            intent.putExtra("category","categoryrn")
//            startActivity(intent)
//        }


        //모든 루틴 보기
        //intent로 all을 눌렀을때 관련 액티비티로 간다
        binding.allProg.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category","categoryall")
            startActivity(intent)
        }
        
        

        
        //nav 바에서 이동하기
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_homeFragment)
        }

        binding.bookmarkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_tipFragment_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_storeFragment)

        }

        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_tipFragment_to_talkFragment)

        }

        return binding.root
    }



}