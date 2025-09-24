package project.superadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

data class AdminUser(val id: String, val email: String)

class PendingAdminAdapter(
    private val admins: List<AdminUser>
) : RecyclerView.Adapter<PendingAdminAdapter.AdminViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    inner class AdminViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmail: TextView = view.findViewById(R.id.tvAdminEmail)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnDecline: Button = view.findViewById(R.id.btnDecline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pending_admin, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val admin = admins[position]
        holder.tvEmail.text = admin.email

        holder.btnApprove.setOnClickListener {
            db.collection("users").document(admin.id)
                .update("approved", true)
                .addOnSuccessListener {
                    holder.btnApprove.isEnabled = false
                    holder.btnDecline.isEnabled = false
                }
        }

        holder.btnDecline.setOnClickListener {
            db.collection("users").document(admin.id)
                .delete()
                .addOnSuccessListener {
                    holder.btnApprove.isEnabled = false
                    holder.btnDecline.isEnabled = false
                }
        }
    }

    override fun getItemCount(): Int = admins.size
}
