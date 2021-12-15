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
package com.keelim.player

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.keelim.player.databinding.FragmentPlayBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs

@AndroidEntryPoint
class PlayerFragment : Fragment() {
  private var _binding: FragmentPlayBinding? = null
  private val binding get() = _binding!!
  private var player: SimpleExoPlayer? = null
  private val videoAdapter by lazy {
    VideoAdapter(
      click = { url, title ->
        play(url, title)
      }
    )
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentPlayBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViews()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    player?.release()
  }

  override fun onStop() {
    super.onStop()
    player?.pause()
  }

  private fun initViews() = with(binding) {
    playerMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
      override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
      override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, progress: Float) {
        (activity as PlayActivity).also { playActivity ->
          playActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress =
            abs(progress)
        }
      }

      override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}
      override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
    })

    fragmentRecyclerView.apply {
      adapter = videoAdapter
      layoutManager = LinearLayoutManager(context)
    }

    context?.let {
      player = SimpleExoPlayer.Builder(it).build()
    }

    playerView.player = player
    player?.addListener(object : Player.EventListener {

      override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)

        if (isPlaying) {
          bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
          bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
      }
    })

    bottomPlayerControlButton.setOnClickListener {
      val player = this@PlayerFragment.player ?: return@setOnClickListener
      if (player.isPlaying) {
        player.pause()
      } else {
        player.play()
      }
    }
  }

  fun play(url: String, title: String) {
    context?.let {
      val dataSourceFactory = DefaultDataSourceFactory(it)
      val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
        .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
      player?.setMediaSource(mediaSource)
      player?.prepare()
      player?.play()
    }

    binding.let {
      it.playerMotionLayout.transitionToEnd()
      it.bottomTitleTextView.text = title
    }
  }
}
