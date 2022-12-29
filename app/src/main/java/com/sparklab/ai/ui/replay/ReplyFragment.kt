package com.sparklab.ai.ui.replay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sparklab.ai.LoginActivity
import com.sparklab.ai.databinding.FragmentReplyBinding
import com.sparklab.sparklab.ui.replay.ReplyViewModel

class ReplyFragment : Fragment() {

    private var _binding: FragmentReplyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val replyViewModel =
            ViewModelProvider(this).get(ReplyViewModel::class.java)

        _binding = FragmentReplyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReply.setOnClickListener {
            binding.clOutput.visibility = View.VISIBLE
            startActivity(Intent( context, LoginActivity::class.java))

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}