package com.sparklab.ai.ui.write

import android.R.attr.description
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sparklab.ai.LoginActivity
import com.sparklab.ai.databinding.FragmentWriteBinding
import com.sparklab.sparklab.ui.write.WriteViewModel


class WriteFragment : Fragment() {

    private var _binding: FragmentWriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val writeViewModel =
            ViewModelProvider(this).get(WriteViewModel::class.java)

        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvOutput.text = "We need to integrate  backed APIs"
        binding.btnWrite.setOnClickListener {
            startActivity(Intent( context, LoginActivity::class.java))

            binding.clOutput.visibility = View.VISIBLE
        }


        binding.btnShare.setOnClickListener {
            share("","")
        }

    }
    fun share(title:String, application:String){


       // Facebook - "com.facebook.katana"
       // Twitter - "com.twitter.android"
       // Instagram - "com.instagram.android"
       // Pinterest - "com.pinterest"

        val intent = context?.packageManager?.getLaunchIntentForPackage(application)
        if (intent != null) {
            // The application exists
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.setPackage(application)
            shareIntent.putExtra(Intent.EXTRA_TITLE, title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, description)
            // Start the specific social application
            context?.startActivity(shareIntent)
        } else {
            val message = "Text I want to share."
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, message)

            startActivity(Intent.createChooser(share, "Share on Social Media"))
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}