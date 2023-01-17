package pt.ipca.smartcanteen.models.helpers

import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import java.util.concurrent.Executors

class ImagesHelper {
    fun getImage(imageURL: String, imageView: ImageView, rounded:Boolean) {
        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()

        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())

        // Initializing the image
        var image: Bitmap? = null

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    if(!rounded)
                        imageView.setImageBitmap(BitmapHelper().getRoundedCornerBitmap(image!!))
                    else
                        imageView.setImageBitmap(BitmapHelper().getRoundedBitmap(image!!))
                }
            }

            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}