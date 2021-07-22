package com.lb.lawbook.profile

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.lb.lawbook.LoginActivity
import com.lb.lawbook.R
import com.lb.lawbook.databinding.ActivityProfileBinding
import com.lb.lawbook.profile.services.ServiceActivity
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.io.IOException
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityProfileBinding

    private val auth = FirebaseAuth.getInstance().currentUser
    private val PICK_IMAGE_REQUEST = 222
    private lateinit var filePath: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var profileViewModel: ProfileViewModel
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.buttonProfileBack.setOnClickListener {
            onBackPressed()
        }

        mBinding.textViewProfileBasicInfo.setOnClickListener {
            startActivity(
                    Intent(
                            this@ProfileActivity,
                            ProfileBasicInfoActivity::class.java
                    )
            )
        }

        mBinding.textViewProfileProfessionalInfo.setOnClickListener {
            startActivity(
                    Intent(
                            this@ProfileActivity,
                            ProfileProfessionalActivity::class.java
                    )
            )
        }

        mBinding.textViewProfileMyAddressInfo.setOnClickListener {
            startActivity(
                    Intent(
                            this@ProfileActivity,
                            ProfileAddressActivity::class.java
                    )
            )
        }

        mBinding.textViewMyServices.setOnClickListener {
            startActivity(
                    Intent(
                            this@ProfileActivity,
                            ServiceActivity::class.java
                    )
            )
        }

        mBinding.textViewProfileLogout.setOnClickListener {
            if (auth != null) {
                Firebase.auth.signOut()
                Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }

        mBinding.textViewContactUs.setOnClickListener {
            val emailList = "abdulhamidreza@gmail.com" //todo need to change
            val subject = "Lawyer Quary | Android | " + auth?.uid + " | " + auth?.phoneNumber
            composeEmail(arrayOf(emailList), subject)
        }


        //image
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        downloadImage()

    }


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
                profileImageView.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


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
        profileViewModel.readDataOwnData().observe(this, androidx.lifecycle.Observer {

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
                                .into(profileImageView)
                    }
                } catch (e: Exception) {

                }
            }

        })

    }

    fun editProfile(view: View) {
        SelectImage()
    }


    fun composeEmail(addresses: Array<String?>?, subject: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}