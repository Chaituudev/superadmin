package project.superadmin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import project.shared.PrefManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PrefManager.isLoggedIn(this) && PrefManager.getRole(this) == "super_admin") {
            startActivity(Intent(this, SuperAdminDashboard::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}
