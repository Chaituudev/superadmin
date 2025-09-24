package project.superadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import project.shared.PrefManager

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        db.collection("users").document(result.user!!.uid).get()
                            .addOnSuccessListener { doc ->
                                val role = doc.getString("role") ?: ""
                                if (role == "super_admin") {
                                    PrefManager.setLogin(this, role)
                                    startActivity(Intent(this, SuperAdminDashboard::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, "Not Super Admin", Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                }
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Login Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
