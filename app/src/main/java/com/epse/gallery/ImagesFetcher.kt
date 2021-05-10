package com.epse.gallery

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

class ImagesFetcher {

    private val imagesURIs: ArrayList<Uri> = ArrayList()

    /**
     * Generates an array of Dummy Elements with number elements
     * @param number: number of dummy items
     * @return ArrayList of dummy items
     */
    fun generateDummyArray(number: Int):ArrayList<Uri>{
        val examplePhoto: Uri = Uri.parse("android.resource://com.epse.gallery/" +
                R.drawable.forest)

        val dummyArray: ArrayList<Uri> = ArrayList()
        for(index in 0..number){
            dummyArray.add(examplePhoto)
        }

        return dummyArray
    }

    /**
     * This function returns the images saved inside the storage of the smartphone.
     * Requires access to the media.
     * @param context: context of the application, used to query the internal storage
     * @return ArrayList containing image URIs
     */
    fun getImageURIs(context: Context): ArrayList<Uri>{
        val columns = arrayOf(MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN

        var imageCursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,
            null,null,"$orderBy DESC")

        var columnIndex = imageCursor!!.getColumnIndex(MediaStore.Images.Media._ID)

        while(imageCursor!!.moveToNext()){
            var name = imageCursor.getLong(columnIndex)
            var imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,name)
            imagesURIs.add(imageUri)
        }

        return imagesURIs
    }


}