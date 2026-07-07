package com.nilac.zebra.znotificationssamplelistener.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nilac.emdkloader.ProfileLoader
import com.nilac.emdkloader.interfaces.ProfileLoaderResultCallback
import com.nilac.emdkloader.models.ProfileError
import com.nilac.emdkloader.utils.SignatureUtils
import com.nilac.zebra.znotificationssamplelistener.model.Event
import com.nilac.zebra.znotificationssamplelistener.utils.EMDKLoaderUtils

class MainViewModel(private var application: Application) : AndroidViewModel(application) {

    val bindNotificationListenerResult: MutableLiveData<Event<Boolean>> = MutableLiveData()

    fun preGrantNotificationListenerPermission() {
        val profile = """
                <wap-provisioningdoc>
                    <characteristic type="Profile">
                        <parm name="ProfileName" value="BindNotificationAccess" />
                        <parm name="TargetSystemVersion" value="10.5" />
                        <characteristic type="AccessMgr" version="10.4">
                            <parm name="PermissionAccessAction" value="1" />
                            <parm name="PermissionAccessPermissionName"
                                value="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE" />
                            <parm name="PermissionAccessPackageName"
                                value="${application.packageName}" />
                            <parm name="PermissionAccessSignature"
                                value="${SignatureUtils.getAppSigningCertificate(application)}" />
                        </characteristic>
                    </characteristic>
                </wap-provisioningdoc>"""

        EMDKLoaderUtils.processProfile(application) {
            ProfileLoader().processProfileNow(
                "BindNotificationAccess",
                profile,
                object : ProfileLoaderResultCallback {
                    override fun onProfileLoadFailed(
                        message: String,
                        errors: List<ProfileError>
                    ) {
                        Log.e(TAG, "Failed to process profile")
                        bindNotificationListenerResult.postValue(Event(false))
                    }

                    override fun onProfileLoaded() {
                        bindNotificationListenerResult.postValue(Event(true))
                    }
                })
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}