package com.keelim.comssa.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.keelim.comssa.R
import com.keelim.comssa.data.model.Feed
import com.keelim.comssa.databinding.ItemFeedListBinding

class FeedAdapter(
    private val click: (Feed) -> Unit
) : ListAdapter<Feed, FeedAdapter.FeedViewHolder>(diffUtil) {

    inner class FeedViewHolder(private val binding: ItemFeedListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Feed) = with(binding) {
            imageviewFeedListMainImage.load(R.drawable.img_post) {
                crossfade(true)
            }
            textviewFeedListTitle.text = item.title
            textviewFeedListContent.text = item.content
            textviewFeedListDate.text = item.date
            imageviewFeedListHeart.isSelected = item.isHeartPressed
            textviewFeedListHeartNum.text = item.heartNum.toString()
            textviewFeedListCommentNum.text = item.commentNum.toString()

            constraintlayoutFeedHeart.setOnClickListener {
                val parseInt = item.heartNum
                if (imageviewFeedListHeart.isSelected.not()) {
                    imageviewFeedListHeart.isSelected = true
                    textviewFeedListHeartNum.text = (parseInt + 1).toString()
                } else {
                    imageviewFeedListHeart.isSelected = false
                    textviewFeedListHeartNum.text = parseInt.toString()
                }
            }

            root.setOnClickListener {
                click(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            ItemFeedListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem == newItem
            }
        }
    }
}
