package com.nilac.zebra.znotificationssamplelistener.utils

import android.content.Context
import android.util.Log
import com.nilac.emdkloader.EMDKLoader
import com.nilac.emdkloader.interfaces.EMDKManagerInitCallBack

object EMDKLoaderUtils {

    private const val TAG = "EMDKLoaderInitializer"

    fun processProfile(context: Context, task: () -> Unit) {
        if (EMDKLoader.getInstance().isManagerInit()) {
            task()
            return
        }

        Log.w(TAG, "Initializing EMDK Manager for the first time...")
        EMDKLoader.getInstance().initEMDKManager(
            context,
            object : EMDKManagerInitCallBack {
                override fun onFailed(message: String) {
                    Log.e(TAG, "Failed to initialise EMDK Manager: $message")
                }

                override fun onSuccess() {
                    Log.i(TAG, "EMDK Manager was successfully initialised")
                    task()
                }
            }
        )
    }

    fun release() {
        EMDKLoader.getInstance().release()
    }
}