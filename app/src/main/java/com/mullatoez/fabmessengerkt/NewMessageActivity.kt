package com.mullatoez.fabmessengerkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mullatoez.fabmessengerkt.databinding.ActivityNewMessageBinding
import com.mullatoez.fabmessengerkt.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_registration.view.*

class NewMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Select User"

        /*val adapter = GroupAdapter<ViewHolder>()
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())*/


        //binding.recyclerviewNewMessage.layoutManager = LinearLayoutManager(this)

        fetchUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach {

                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }

                binding.recyclerviewNewMessage.adapter = adapter

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                   // intent.putExtra(USER_KEY, userItem.user.userName)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)
                    finish()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        // will be called on our list for each user object later on...
        viewHolder.itemView.userName_textView.text = user.userName

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.user_imageview)


    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_registration
    }

}

/*Boiler plate code*/
// We'll use GroupieAdapter


/*
class CustomAdapter: RecyclerView.Adapter<ViewHolder> {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    }
}*/
