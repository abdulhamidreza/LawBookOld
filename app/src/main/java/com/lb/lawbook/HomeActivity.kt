package com.lb.lawbook

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.lb.lawbook.databinding.ActivityHomeBinding
import com.lb.lawbook.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class HomeActivity : AppCompatActivity() {

    var text = arrayOf("Sign Up with Us", "We will be live soon", getImoji())
    private lateinit var mHomeBinding: ActivityHomeBinding
    val auth = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mHomeBinding.root)

        mHomeBinding.fadingText.setTexts(text)
        mHomeBinding.layoutHomeProfile.buttonHomeBottomProfile.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity, ProfileActivity::class.java
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth == null) {
            Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        playVideo()
    }

    private fun getImoji(): String {
        // Emoji https://apps.timwhitlock.info/emoji/tables/unicode
        var unicode = 0x1F680 //rocket
        return String(Character.toChars(unicode))
    }

    private fun playVideo() {
        videoWait.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.wait_anim))
        videoWait.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            it.isLooping = true
        })
        videoWait.start()

    }
}