package pt.ipca.smartcanteen.models.helpers

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import pt.ipca.smartcanteen.R

class LoadingDialogManager(private val layoutInflater: LayoutInflater, private val context: Context) {
    lateinit var dialog: AlertDialog
    fun createLoadingAlertDialog() {
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_alert_dialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}