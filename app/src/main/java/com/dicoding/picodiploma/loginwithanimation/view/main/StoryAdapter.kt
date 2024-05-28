package com.dicoding.picodiploma.loginwithanimation.view.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailStoryActivity
import androidx.core.util.Pair
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding

class StoryAdapter(private var stories: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivPhoto)

            binding.tvName.text = story.name
            binding.tvDesc.text = story.description

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
                intent.putExtra("story", story)
                val options: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.ivPhoto, "photo"),
                        Pair(binding.tvDesc, "desc"),
                        Pair(binding.tvName, "name"),
                )
                binding.root.context.startActivity(intent, options.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateStories(newStories: List<ListStoryItem>) {
        stories = newStories
        notifyDataSetChanged()
    }
}