/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.nandadiagnosis.ui.main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.data.model.CardItem
import com.keelim.nandadiagnosis.databinding.FragmentFavoriteBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class FavoriteFragment : Fragment(), CardStackListener {
  private var _binding: FragmentFavoriteBinding? = null
  private val binding get() = _binding!!
  private val userDB by lazy { Firebase.database.reference.child("Users") }
  private val auth by lazy { Firebase.auth }
  private val cardAdapter by lazy { CardItemAdapter() }
  private val cardItem = mutableListOf<CardItem>()
  private val manager by lazy { CardStackLayoutManager(requireActivity(), this) }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val currentUserDB = userDB.child(getCurrentUserID())
    currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        snapshot.child("name").value ?: run {
          showNameInput()
          return
        }
        getUnselectedUser()
      }

      override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
      }
    })

    initStackView()
  }

  private fun getCurrentUserID(): String {
    auth.currentUser ?: run {
      Toast.makeText(requireContext(), "로그인이 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
      findNavController().navigate(R.id.loginFragment)
    }

    return auth.currentUser?.uid.orEmpty()
  }

  private fun showNameInput() {
    val edit = EditText(requireActivity())

    MaterialAlertDialogBuilder(requireContext())
      .setTitle("이름을 입력해주세요")
      .setView(edit)
      .setPositiveButton("저장") { _, _ ->
        if (edit.text.isEmpty()) {
          showNameInput()
        } else {
          saveUserName(edit.text.toString())
        }
      }
      .setCancelable(false)
      .show()
  }

  private fun saveUserName(name: String) {
    val uid = getCurrentUserID()
    val currentUserDB = userDB.child(uid)
    val user = mutableMapOf<String, Any>()
    user["uid"] = uid
    user["name"] = name
    currentUserDB.updateChildren(user)
    getUnselectedUser()
  }

  private fun initStackView() {
    binding.cardStack.layoutManager = manager
    binding.cardStack.adapter = cardAdapter
  }

  private fun getUnselectedUser() {
    userDB.addChildEventListener(object : ChildEventListener {
      override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        if (snapshot.child("uid").value != getCurrentUserID() &&
          snapshot.child("likedBy").child("like").hasChild(getCurrentUserID()).not() &&
          snapshot.child("likedBy").child("dislike").hasChild(getCurrentUserID()).not()
        ) {
          val userId = snapshot.child("uid").value.toString()
          var name = "undefined"
          if (snapshot.child("name").value != null) {
            name = snapshot.child("name").value.toString()
          }

          cardItem.add(CardItem(userId, name))
          cardAdapter.submitList(cardItem)
          cardAdapter.notifyDataSetChanged()
        }
      }

      override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        cardItem.find { it.userId == snapshot.key }?.let {
          it.name = snapshot.child("name").value.toString()
        }
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

  private fun like() {
    val card = cardItem[manager.topPosition - 1]
    cardItem.removeFirst()

    userDB.child(card.userId)
      .child("likedBy")
      .child("like")
      .child(getCurrentUserID())
      .setValue(true)
    otherLikeMe(card.userId)
    Toast.makeText(requireContext(), "${card.name}을 like 하였습니다. ", Toast.LENGTH_SHORT).show()
  }
  private fun dislike() {
    val card = cardItem[manager.topPosition - 1]
    cardItem.removeFirst()

    userDB.child(card.userId)
      .child("likedBy")
      .child("dislike")
      .child(getCurrentUserID())
      .setValue(true)

    Toast.makeText(requireContext(), "${card.name}을 dislike 하였습니다. ", Toast.LENGTH_SHORT).show()
  }

  private fun otherLikeMe(userId: String) {
    val other = userDB.child(getCurrentUserID())
      .child("likedBy")
      .child("like")
      .child(userId)

    other.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.value == true) {
          userDB.child(getCurrentUserID())
            .child("likeBy")
            .child("match")
            .child(userId)
            .setValue(true)

          userDB.child(userId)
            .child("likeBy")
            .child("match")
            .child(getCurrentUserID())
            .setValue(true)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
      }
    })
  }

  override fun onCardDragging(direction: Direction?, ratio: Float) {
    TODO("Not yet implemented")
  }

  override fun onCardSwiped(direction: Direction?) {
    when (direction) {
      Direction.Right -> like()
      Direction.Left -> dislike()
      else -> Unit
    }
  }

  override fun onCardRewound() {
    TODO("Not yet implemented")
  }

  override fun onCardCanceled() {
    TODO("Not yet implemented")
  }

  override fun onCardAppeared(view: View?, position: Int) {
    TODO("Not yet implemented")
  }

  override fun onCardDisappeared(view: View?, position: Int) {
    TODO("Not yet implemented")
  }
}
