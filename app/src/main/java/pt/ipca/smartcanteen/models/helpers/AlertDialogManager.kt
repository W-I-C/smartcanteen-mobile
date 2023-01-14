package pt.ipca.smartcanteen.models.helpers

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import pt.ipca.smartcanteen.R


class AlertDialogManager(private val layoutInflater: LayoutInflater, private val context: Context) {
    lateinit var dialog: AlertDialog
    fun createLoadingAlertDialog() {
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_alert_dialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun createConfirmAlertDialog(message: String, onConfirmFunction: () -> Unit) {
        val builder = AlertDialog.Builder(context,R.style.AlertDialogTheme)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.confirm)) { dialog, id ->
                onConfirmFunction()
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, id ->
                dialog.dismiss()
            }
        val dialog = builder.create()

        dialog.window?.setBackgroundDrawable(ResourcesCompat.getDrawable(context.resources,R.drawable.square_card_background, null))
        dialog.show()
    }
}