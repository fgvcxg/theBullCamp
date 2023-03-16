package com.example.thebullcamp.contentList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thebullcamp.R
import com.example.thebullcamp.utils.FBAuth
import com.example.thebullcamp.utils.FBRef

class ContentRVAdapter(val context : Context, 
                       val items : ArrayList<ContentModel>, 
                       val keyList : ArrayList<String>, 
                       //어뎁터를 선언할때 우리가 이미 알고 있는 북마크를 누른 것의 키값을 이용하여 같은 키값이 있으면 북마크의 이미지를 변경해준다
                       val bookmarkIdList : MutableList<String>)
    : RecyclerView.Adapter<ContentRVAdapter.Viewholder>() {

    //webUrl 을 추가하여 받아서 사진을 눌렀을때 가기
//    interface ItemClick{
//        fun onClick(view: View, position: Int)
//    }
//    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentRVAdapter.Viewholder {
        //content_rv_item 랑 연결해주면서 각각의 아이템들을 넣어주기
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item,parent,false)



        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: ContentRVAdapter.Viewholder, position: Int) {
        
        //item 클릭하는 메서드
//        if(itemClick != null){
//            holder.itemView.setOnClickListener { v->
//                itemClick?.onClick(v,position)
//            }
//        }
        holder.bindItems(items[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //content_rv_item 안에 있는 데이터 들을 불러오는 것
    inner class Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(item : ContentModel, key : String){
            
            //클릭하는 부분을 위에 주석처리 했는데 이렇게 할 수도 있다
            itemView.setOnClickListener {
                Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ContentShActivity::class.java)
                intent.putExtra("url",item.webUrl)
                itemView.context.startActivity(intent)
            }
            
            //itemview 에서 textArea를 찾는다
            val contextTitle = itemView.findViewById<TextView>(R.id.textArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            //북마크 정보의 키값이 있다면 북마크의 그림을 변경시킨다
            if(bookmarkIdList.contains(key)){
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            }else{
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            //북마크를 클릭하면 나온다
            bookmarkArea.setOnClickListener {
                Toast.makeText(context,key,Toast.LENGTH_SHORT).show()
                
                //북마크에 uid 키와 체크한 컨텐츠를 표시해줌
                //FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).setValue("Good")
                //로그를 찍어보니까 원하는 값이 들어가지 않아서 변경해야함

                
                if(bookmarkIdList.contains(key)){
                    //북마크가 있을때 데이터를 삭제를 할때 클릭된 애의 키를 지워준다
                    bookmarkIdList.remove(key)
                    FBRef.bookmarkRef
                        .child(FBAuth.getUid())
                        .child(key)
                        .removeValue()
                    
                }else{
                    //북마크가 없을때
                    FBRef.bookmarkRef
                        .child(FBAuth.getUid())
                        .child(key)
                        .setValue(BookmarkModel(true))
                }

            }

            contextTitle.text = item.title

            //glide
            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)

        }

    }


}