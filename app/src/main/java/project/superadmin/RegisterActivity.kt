package project.superadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val data = hashMapOf(
                            "email" to email,
                            "role" to "super_admin",
                            "approved" to true
                        )
                        db.collection("users").document(result.user!!.uid).set(data)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Register Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
