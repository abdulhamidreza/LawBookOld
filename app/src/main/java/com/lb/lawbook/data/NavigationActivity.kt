package com.lb.lawbook.data

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.lb.lawbook.LoginActivity
import com.lb.lawbook.R
import com.lb.lawbook.data.ui.gallery.ProfileViewModel
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


class NavigationActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var headerView: View
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val auth = FirebaseAuth.getInstance().currentUser
    private val PICK_IMAGE_REQUEST = 22
    private lateinit var filePath: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseStorage: FirebaseStorage
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_sign_out
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        if (auth == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else {
            val navigationView = findViewById<NavigationView>(R.id.nav_view)
            headerView = navigationView.getHeaderView(0)
            headerView.userIdTextView.text = auth.uid
            headerView.numberTextView.text = auth.phoneNumber
            headerView.profileImageView.setOnClickListener(View.OnClickListener {
                SelectImage()
            })
        }

        downloadImage()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    // Select Image method
    private fun SelectImage() {

        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    // Override onActivityResult method
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data!!
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath
                    )
                headerView.profileImageView.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    // UploadImage method
    private fun uploadImage() {
        val ref = storageReference.child("uploads/profile/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(filePath)
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        val urlTask =
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    val downloadUri = task.result
                    saveProfilePathToFireStore(downloadUri.toString())
                } else {
                    Toast.makeText(this, "Please Upload Image again", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e -> // Error, Image not uploaded
                progressDialog.dismiss()
                Toast
                    .makeText(
                        this,
                        "Failed " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
    }

    fun saveProfilePathToFireStore(uri: String) {
        val pictures: MutableMap<String, Any> = HashMap()
        pictures.put("profile_pic", uri)
        val user: MutableMap<String, Any> = HashMap()
        user.put("pictures", pictures)
        val documentRef = db.collection("users").document(auth?.uid.toString())
        documentRef.set(user)
    }

    fun downloadImage() {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.readDataOwnData().observe(this, Observer {
            if (it != null) {
                try {
                    val pictures = it.get("pictures") as HashMap<String, Any>
                    pictures.let {
                        Glide.with(this)
                            .load(pictures.get("profile_pic"))
                            .placeholder(R.mipmap.ic_profile_placeholder)
                            .override(300, 200)  //resized
                            .centerCrop()
                            .error(R.mipmap.ic_profile_placeholder)
                            .into(headerView.profileImageView)
                    }
                } catch (e: Exception) {

                }
            }

        })
    }

}