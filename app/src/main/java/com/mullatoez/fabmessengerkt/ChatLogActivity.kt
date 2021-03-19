package com.mullatoez.fabmessengerkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.mullatoez.fabmessengerkt.databinding.ActivityChatLogBinding
import com.mullatoez.fabmessengerkt.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = user?.userName

        binding.buttonSendMessageChatLog.setOnClickListener {
            performSendMessage()
        }

        //setUpDummyData()
        ListenForMessages()
    }

    private fun ListenForMessages() {
        val reference = FirebaseDatabase.getInstance().getReference("/messages")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }

    class ChatMessage(
        val id: String, val text: String, val fromid: String, val toid: String,
        val timestamp: Long
    )

    private fun performSendMessage() {

        val message = binding.editTextEnterMessage.text.toString()
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val fromid = FirebaseAuth.getInstance().uid ?: return

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toid = user?.uid ?: return

        val chatMessage =
            ChatMessage(reference.key!!, message, fromid, toid, System.currentTimeMillis() / 1000)

        reference.setValue(chatMessage)
            .addOnSuccessListener {

            }
    }

    private fun setUpDummyData() {
        val adapter = GroupAdapter<ViewHolder>()
        binding.recyclerViewChatLog.adapter = adapter

        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
    }

}

class ChatFromItem : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = "Text from message"
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = "This is text from to message that is longer"
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}