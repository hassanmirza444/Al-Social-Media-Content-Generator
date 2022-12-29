package com.sparklab.ai


import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.ClipboardManager
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.sparklab.ai.data.ApiState
import com.sparklab.ai.databinding.ActivityMainBinding
import com.sparklab.ai.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var rotationAngle = 0
    private var isCollaped = true
    lateinit var mContext: Context
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val informal = arrayListOf(
        "casual",
        "chatty",
        "conversational",
        "everyday",
        "friendly",
        "casual",
        "easy",
        "informal"
    )//8
    private val formals = arrayListOf(
        "ceremonious",
        "conventional",
        "exact",
        "self-proclaimed",
        "literary",
        "polite",
        "stilted",
        "titular"
    )//8
    private val assertive =
        arrayListOf("aggressive", "militaristic", "positive", "certain", "energetic")//5
    private val friendly = arrayListOf(
        "amicable",
        "cordial",
        "affectionate",
        "approving",
        "good",
        "kind",
        "polite",
        "sympathetic"
    )//8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mContext = this
        setContentView(binding.root)
        setDrawer()
        setUpUI()

        lifecycleScope.launchWhenStarted {
            mainViewModel.apiStateFlow.collect {
                Log.d("Hssn", it.toString())
                when (it) {
                    is ApiState.Success -> {
                        Log.d("Hssn", it.data.toString())
                        binding.appBarMain.contentMain.loadingbar.visibility = GONE
                        binding.appBarMain.contentMain.tvOutput.text =
                            it.data.choices.get(0).text.trim()
                        binding.appBarMain.contentMain.btnGenerateContent.text =
                            "Regenerate Content"
                        binding.appBarMain.contentMain.clOutput.visibility = VISIBLE
                    }
                    ApiState.Empty -> {
                        //    binding.appBarMain.contentMain.loadingbar.visibility = View.GONE
                        Log.d("Hssn", it.toString())

                    }
                    is ApiState.Failure -> {
                        binding.appBarMain.contentMain.loadingbar.visibility = GONE
                        Log.d("Hssn", it.msg.toString())
                        showSnackBar("Something  went wrong...")

                    }
                    ApiState.Loading -> {
                        //   binding.appBarMain.contentMain.loadingbar.visibility = View.GONE
                        Log.d("Hssn", it.toString())

                    }
                }
            }
        }
    }

    private fun setUpUI() {
        binding.apply {
            navView.setNavigationItemSelectedListener { menuItem ->
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                if (menuItem.itemId == R.id.nav_login) {
                    startActivity(Intent(mContext, LoginActivity::class.java))
                } else if (menuItem.itemId == R.id.nav_writing) {
                    startActivity(Intent(mContext, WritingActivity::class.java))
                }
                true
            }
            binding.appBarMain.contentMain.apply {
                btnGenerateContent.setOnClickListener {
                    if (tilInput.editText?.text.toString().length > 3) {

                        var prompt = ""
                        if (llKeywords.childCount > 0) {
                            prompt = " using keyword "
                            for (index in 0..(llKeywords.childCount - 1)) {
                                prompt = prompt.plus(
                                    (llKeywords.getChildAt(index) as Chip).text.trim()
                                        .toString() + ","
                                )
                            }
                            prompt = prompt.dropLast(1)
                        }

                        if (binding.appBarMain.contentMain.sliderFormalInformal.value.toInt() < 50) {
                            val randomValue = (0..7).random()
                            prompt = prompt.plus(" in ").plus(informal[randomValue])
                        } else {
                            val randomValue = (0..7).random()
                            prompt = prompt.plus(" in ").plus(formals[randomValue])
                        }

                        if (binding.appBarMain.contentMain.sliderFriendlyAssertive.value.toInt() < 50) {
                            val randomValue = (0..7).random()
                            prompt = prompt.plus(" and ").plus(friendly[randomValue])
                        } else {
                            val randomValue = (0..4).random()
                            prompt = prompt.plus(" and ").plus(assertive[randomValue])
                        }
                        prompt = prompt.plus(" tone.")




                        prompt = tilInput.editText?.text.toString().plus(prompt)
                        Log.d("hssn", prompt)
                        binding.appBarMain.contentMain.temp.text = prompt
                        val j = JSONObject()
                        j.put("model", "text-davinci-003")
                        j.put("prompt", prompt)
                        j.put("temperature", 0.8)
                        j.put("max_tokens", sliderWordsCount.value.toInt())

                        val jsonParser = JsonParser()
                        val gsonObject = jsonParser.parse(j.toString()) as JsonObject
                        mainViewModel.getData(gsonObject)
                        binding.appBarMain.contentMain.loadingbar.visibility = VISIBLE
                        binding.appBarMain.contentMain.clOutput.visibility = GONE
                    }
                }
                ivClose.setOnClickListener {
                    reset()
                }
                ivCopy.setOnClickListener {
                    setClipboard(binding.appBarMain.contentMain.tvOutput.text.toString())
                }
                ivArrow.setOnClickListener {
                    rotationAngle = if (rotationAngle == 0) 180 else 0 //toggle
                    binding.appBarMain.contentMain.ivArrow.animate()
                        .rotation(rotationAngle.toFloat()).setDuration(500).start()

                    if (isCollaped) {
                        expand(binding.appBarMain.contentMain.sliders)
                        isCollaped = false
                    } else {
                        collapse(binding.appBarMain.contentMain.sliders)
                        isCollaped = true
                    }

                }
                shareFacebook.setOnClickListener {
                    val shareDialog = ShareDialog(this@MainActivity)
                    val linkContent =
                        ShareLinkContent.Builder()
                            .setQuote(binding.appBarMain.contentMain.tvOutput.text.toString())
                            .build()

                    //returns True if the ShareLinkContent type can be shown via the dialog
                    if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                        shareDialog.show(linkContent)
                    }

                }
                shareWhatsapp.setOnClickListener {
                    shareWhatsapp(binding.appBarMain.contentMain.tvOutput.text.toString())
                }
                shareLinkedin.setOnClickListener {
                    val linkedinIntent = Intent(Intent.ACTION_SEND)
                    linkedinIntent.type = "text/plain"
                    linkedinIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        binding.appBarMain.contentMain.tvOutput.text.toString()
                    )

                    var linkedinAppFound = false
                    val matches2 = packageManager.queryIntentActivities(linkedinIntent, 0)

                    for (info in matches2) {
                        if (info.activityInfo.packageName.lowercase(Locale.getDefault())
                                .startsWith("com.linkedin")
                        ) {
                            linkedinIntent.setPackage(info.activityInfo.packageName)
                            linkedinAppFound = true
                            break
                        }
                    }

                    if (linkedinAppFound) {
                        startActivity(linkedinIntent)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "LinkedIn app not Installed in your mobile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                tietKeyword.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        v: TextView?, actionId: Int, event: KeyEvent?
                    ): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_DONE && binding.appBarMain.contentMain.tilKeyword.editText?.text?.length!! > 1) {
                            val chip = Chip(mContext)
                            chip.isCloseIconVisible = true
                            chip.chipStrokeWidth = 1.0f
                            chip.setTextColor(mContext.getColorStateList(R.color.white))
                            chip.closeIconTint = mContext.getColorStateList(R.color.white)
                            chip.chipStrokeColor = mContext.getColorStateList(R.color.main_color)
                            chip.chipBackgroundColor =
                                mContext.getColorStateList(R.color.box_background_color)
                            chip.text = binding.appBarMain.contentMain.tilKeyword.editText?.text
                            binding.appBarMain.contentMain.llKeywords.addView(
                                chip, 0, ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                            )
                            binding.appBarMain.contentMain.tilKeyword.editText?.text?.clear()
                            chip.setOnCloseIconClickListener {
                                binding.appBarMain.contentMain.llKeywords.removeView(it)
                            }
                            return true
                        }
                        return false
                    }
                })
            }

        }
    }

    private fun reset() {
        binding.appBarMain.contentMain.tilInput.editText?.text?.clear()
        binding.appBarMain.contentMain.tilInput.focusable = FOCUSABLE
        binding.appBarMain.contentMain.tilInput.requestFocus()
        binding.appBarMain.contentMain.tilKeyword.editText?.text?.clear()
        binding.appBarMain.contentMain.tvOutput.text = ""
        binding.appBarMain.contentMain.btnGenerateContent.text = "Generate Content"
        binding.appBarMain.contentMain.clOutput.visibility = GONE

    }

    private fun setClipboard(text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = text
            Toast.makeText(mContext, "Text Copied!", Toast.LENGTH_SHORT).show()
        } else {
            val clipboard =
                mContext.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
            showSnackBar("Text Copied!")

        }
    }

    private fun showSnackBar(textToShow: String) {
        Snackbar.make(binding.appBarMain.root, textToShow, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(R.color.box_color))
            .show()
    }


    private fun shareWhatsapp(text: String) {
        try {
            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"
            val info = packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp")
            waIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(waIntent, "Share Via SparkLab"))

        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(mContext, "app not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDrawer() {
        setSupportActionBar(binding.appBarMain.toolbar)

        drawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.tab_color, R.string.tab_color
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.title = mContext.getString(R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = false
        drawerToggle.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24) //set your horizontal line icon
        binding.drawerLayout.addDrawerListener(drawerToggle)
    }

    private fun collapse(v: View) {
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

    private fun expand(v: View) {
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
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return false
    }

}




