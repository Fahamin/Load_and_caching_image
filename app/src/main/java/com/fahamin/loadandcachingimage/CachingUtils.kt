package com.fahamin.loadandcachingimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileOutputStream
import java.net.URL

class CachingUtils {

    companion object {

        // saving the image in internal stoarge
        fun storeBitmap(bitmap: Bitmap, context: Context) {
            val fileOutputStream: FileOutputStream =
                context.openFileOutput(
                    Constance.IMAGE_NAME, Context.MODE_PRIVATE
                )
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)

            fileOutputStream.flush()
            fileOutputStream.close()
        }

        //loading the cached image from internal storage
        fun loadBitmap(context: Context): Bitmap? {
            return BitmapFactory.decodeFile(
                context.filesDir.absolutePath
                        + "/" + Constance.IMAGE_NAME
            )
        }

        //download an image with the url
        fun getBitmapFromUrl(): Bitmap? {
            val url = URL(Constance.IMAGE_URL)
            return BitmapFactory.decodeStream(
                url.openConnection().getInputStream()
            )
        }

    }
}