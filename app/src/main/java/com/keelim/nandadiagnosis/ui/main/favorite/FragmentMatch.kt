package com.keelim.nandadiagnosis.ui.main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.model.CardItem
import com.keelim.nandadiagnosis.databinding.FragmentMatchBinding

class FragmentMatch : Fragment() {
    private val userDB by lazy { Firebase.database.reference.child("Users") }
    private val auth by lazy { Firebase.auth }
    private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!
    private val matchAdapter by lazy { MatchAdapter()}
    private val cardItems = mutableListOf<CardItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMatchRecycler()
        getMatch()
    }

    private fun getCurrentUserID(): String {
        auth.currentUser ?: run {
            Toast.makeText(requireContext(), "로그인이 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
        }

        return auth.currentUser?.uid.orEmpty()
    }

    private fun initMatchRecycler(){
        binding.matchRecycler.adapter = matchAdapter
        binding.matchRecycler.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun getMatch(){
        val matchDB = userDB.child(getCurrentUserID())
            .child("likedBy")
            .child("match")

        matchDB.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.key?.isNotEmpty() == true){
                    getUserByKey(snapshot.key.orEmpty())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun getUserByKey(userId:String){
         userDB.child(userId).addListenerForSingleValueEvent(object:ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 cardItems.add(CardItem(userId, snapshot.child("name").value.toString()))
                 matchAdapter.submitList(cardItems)
             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }

         })

    }
}