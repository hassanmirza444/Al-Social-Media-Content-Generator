package com.sparklab.ai

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.appcompat.app.AppCompatActivity
import com.sparklab.ai.databinding.ActivitySocialMediaSharingBinding

import com.twitter.sdk.android.tweetcomposer.TweetComposer


class SocialMediaSharingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySocialMediaSharingBinding
    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialMediaSharingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Share content on Social media!"

       binding.tvOutput.text= "Share content on Social media using Open AI"

        binding.btnGenerateContent.setOnClickListener {
            binding.clOutput.visibility = View.VISIBLE
            if (binding.tvOutput.text.length > 10){
                binding.llSocialMedia.visibility = View.VISIBLE
            }
        }


        binding.shareTwitter.setOnClickListener {
            shareOnTwitter( binding.tvOutput.text.toString())
        }
        binding.shareFacebook.setOnClickListener {
            share("", binding.tvOutput.text.toString(), "com.facebook.katana")
        }
        binding.shareLinkedin.setOnClickListener {
            share("", binding.tvOutput.text.toString(), "com.linkedin.android")
        }
        binding.shareWhatsapp.setOnClickListener {
            share("", binding.tvOutput.text.toString(), "com.whatsapp")
        }
        binding.shareInstagram.setOnClickListener {
            share("", binding.tvOutput.text.toString(), "com.instagram.android")
        }
        binding.shareSocialMedia.setOnClickListener {
            share("", binding.tvOutput.text.toString(), "")
        }

    }

    private fun share(title: String, description: String, packageName: String) {


        val intent = mContext.packageManager?.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            // The application exists
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.setPackage(packageName)
            shareIntent.putExtra(Intent.EXTRA_TITLE, title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, description)
            // Start the specific social application
            mContext.startActivity(shareIntent)
        } else if (packageName.isEmpty()){
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, description)
            startActivity(Intent.createChooser(share, "Share on Social Media"))
        }else{
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        }



    }

    fun shareOnTwitter(shareText: String) {
       // var shareIntent: Intent? = mContext.packageManager?.getLaunchIntentForPackage("com.twitter.android.PostActivity")

        val builder = TweetComposer.Builder(this)
            .text("just setting up my Fabric.")
        builder.show()





    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Collapse speed of 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }


    fun expand(v: View) {
        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                v.layoutParams.height =
                    if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Expansion speed of 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return false
    }
}