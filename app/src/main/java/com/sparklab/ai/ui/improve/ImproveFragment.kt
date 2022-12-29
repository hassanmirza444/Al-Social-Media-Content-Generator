package com.sparklab.ai.ui.improve


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sparklab.ai.LoginActivity
import com.sparklab.ai.databinding.FragmentImproveBinding
import com.sparklab.sparklab.ui.improve.ImproveViewModel

class ImproveFragment : Fragment() {

    private var _binding: FragmentImproveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val improveViewModel =
            ViewModelProvider(this).get(ImproveViewModel::class.java)

        _binding = FragmentImproveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCorrect.setOnClickListener {
            binding.clOutput.visibility = View.VISIBLE
            startActivity(Intent( context, LoginActivity::class.java))
        }

        binding.tilInput.addOnEditTextAttachedListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}