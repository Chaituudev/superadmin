package project.superadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import project.shared.PrefManager

class SuperAdminDashboard : AppCompatActivity() {
    private lateinit var rvPendingAdmins: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val adminList = mutableListOf<AdminUser>()
    private lateinit var adapter: PendingAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        rvPendingAdmins = findViewById(R.id.rvPendingAdmins)
        rvPendingAdmins.layoutManager = LinearLayoutManager(this)
        adapter = PendingAdminAdapter(adminList)
        rvPendingAdmins.adapter = adapter

        loadPendingAdmins()

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            PrefManager.logout(this)
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadPendingAdmins() {
        db.collection("users")
            .whereEqualTo("role", "admin")
            .whereEqualTo("approved", false)
            .get()
            .addOnSuccessListener { snapshot ->
                adminList.clear()
                for (doc in snapshot.documents) {
                    val id = doc.id
                    val email = doc.getString("email") ?: "Unknown"
                    adminList.add(AdminUser(id, email))
                }
                adapter.notifyDataSetChanged()
            }
    }
}
