package com.epse.gallery

import android.Manifest
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.PermissionChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StorageUtils {

    companion object {

        private val imagesURIs = mutableStateListOf<Uri>()

        private var queryOrder = "DESC"

        fun setQueryOrder(order:String,context: Context){
            queryOrder =
                if(order == "ASC" || order == "DESC") order
                else "DESC"
            acquireImageURIs(context)
        }

        /**
         * Boolean function that checks if the application has the READ_EXTERNAL_STORAGE
         * permission granted
         * @param context Context of the application
         * @return true if the permission is granted, false otherwise
         */
        fun hasReadStoragePermission(context: Context): Boolean {
            val permission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    PermissionChecker.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            return permission == PackageManager.PERMISSION_GRANTED
        }

        /**
         * Boolean function that checks if the application has the READ_EXTERNAL_STORAGE
         * permission granted
         * @param context Context of the application
         * @return true if the permission is granted, false otherwise
         */
        fun hasWriteStoragePermission(context: Context): Boolean {
            val permission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    PermissionChecker.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
            return permission == PackageManager.PERMISSION_GRANTED
        }

        /**
         * This function returns the images saved inside the storage of the smartphone.
         * REQUIRES READ ACCESS TO THE STORAGE
         * @return ArrayList containing image URIs
         */
        fun getImageURIs(): SnapshotStateList<Uri> {
            return imagesURIs
        }

        /**
         * This function refreshes the URIs of the available photos inside the device's storage.
         * If order param does not equal to ASC or DESC, by default returns order by DESC and does
         * not throw any exception.
         * THIS FUNCTION REQUIRES READ ACCESS TO THE STORAGE
         * @param context context of the application, used to query the internal storage
         * @return ArrayList containing image URIs
         * @throws SecurityException if the permission to read the storage has not been granted
         */
        private fun acquireImageURIs(context: Context){

            /**
             * Clear up the actual list
             */
            imagesURIs.clear()

            /**
             * Checking permission
             * if permission to read the storage has not been granted throw SecurityException
             */
            if (!hasReadStoragePermission(context = context))
                throw SecurityException(context.getString(R.string.permission_read_external_storage_not_granted))

            //Setting un the query
            val columns = arrayOf(MediaStore.Images.Media._ID)
            val orderBy = MediaStore.Images.Media.DATE_TAKEN

            val imageCursor: Cursor? = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, "$orderBy $queryOrder"
            )

            val columnIndex = imageCursor!!.getColumnIndex(MediaStore.Images.Media._ID)

            while (imageCursor.moveToNext()) {
                val id = imageCursor.getLong(columnIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imagesURIs.add(imageUri)
            }

            imageCursor.close()
        }

        /**
         * This function returns the number of images found in the device storage
         * @return number of images found
         * @throws SecurityException if the permission to read the storage has not been granted
         */
        fun numberOfImages(): Int{
            return imagesURIs.size
        }


        /**
         * This function returns an HashMap<String,String> containing information on images
         * as part of the device's file system. In details it returns info about
         * https://developer.android.com/reference/android/provider/MediaStore.MediaColumns#SIZE
         * https://developer.android.com/reference/android/provider/MediaStore.MediaColumns#DISPLAY_NAME
         * https://developer.android.com/reference/android/provider/MediaStore.MediaColumns#DATA
         * https://developer.android.com/reference/android/provider/MediaStore.MediaColumns#VOLUME_NAME
         * @param context context of the application, used to query the internal storage
         * @param uri URI of the image to look for
         * @return HashMap containing values
         */
        fun getFileData(context: Context,uri:Uri):Map<String,String>{

            val rv = HashMap<String,String>()

            /**
             * If the version of Android is <Q then the MediaStore.Images.Media.VOLUME_NAME
             * is not available
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val projection = arrayOf(
                    MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA, MediaStore.Images.Media.VOLUME_NAME
                )

                val cursor = context.contentResolver.query(
                    uri,
                    projection, null, null, null
                )
                cursor!!.moveToFirst()

                val sizeIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                val nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val dataIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val volumeIndex = cursor.getColumnIndex(MediaStore.Images.Media.VOLUME_NAME)

                rv["size"] = cursor.getString(sizeIndex)
                rv["name"] = cursor.getString(nameIndex)
                rv["path"] = cursor.getString(dataIndex)
                rv["storage"] = cursor.getString(volumeIndex)

                cursor.close()

            } else {

                val projection = arrayOf(
                    MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA )

                val cursor = context.contentResolver.query(
                    uri,
                    projection, null, null, null
                )
                cursor!!.moveToFirst()
                
                val sizeIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                val nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val dataIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)

                rv["size"] = cursor.getString(sizeIndex)
                rv["name"] = cursor.getString(nameIndex)
                rv["path"] = cursor.getString(dataIndex)

                cursor.close()
            }
            return rv
        }

        /**
         * This function removes from the storage the image passed as parameter
         * @param context context of the application, used to query the internal storage
         * @param uri URI of the image to look for
         * @return true if deleted, false otherwise
         */
        @ExperimentalMaterialApi
        @ExperimentalFoundationApi
        suspend fun delete(context:Context,uri:Uri){
            withContext(Dispatchers.IO){
                try{
                    context.contentResolver.delete(uri,null,null)
                }catch(e:SecurityException){
                    Log.d("EXCEPTION",e.toString())
                    val intentSender = when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                            MediaStore.createDeleteRequest(context.contentResolver, listOf(uri)).intentSender
                        }
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                            val recoverableSecurityException = e as? RecoverableSecurityException
                            recoverableSecurityException?.userAction?.actionIntent?.intentSender
                        }
                        else -> null
                    }
                    intentSender?.let { sender ->
                        MainActivity.intentSenderLauncher.launch(
                            IntentSenderRequest.Builder(sender).build()
                        )
                    }
                }
                finally{
                    //Refresh the imageURIs array
                    acquireImageURIs(context = context)
                }
            }
        }

    }

}