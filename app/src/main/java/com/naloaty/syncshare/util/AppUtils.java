package com.naloaty.syncshare.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.naloaty.syncshare.R;
import com.naloaty.syncshare.dialog.RationalePermissionRequest;

import java.util.ArrayList;
import java.util.List;

/*
 * This class provides extra utils
 * -------------------------------------------
 * Some parts of this activity borrowed from TrebleShot (com.genonbeta.TrebleShot.dialog)
 * Author: velitasali (https://github.com/velitasali)
 */

public class AppUtils {


    /*public static SharedPreferences getDefaultPreferences(final Context context) {

    }*/

    //This method returns list of 'dangerous' permissions, that require dialog with description
    public static List<RationalePermissionRequest.PermissionRequest> getRequiredPermissions(Context context)
    {
        List<RationalePermissionRequest.PermissionRequest> permissionRequests = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= 16) {
            permissionRequests.add(new RationalePermissionRequest.PermissionRequest(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    R.string.text_requestPermissionStorage,
                    R.string.text_requestPermissionStorageSummary));
        }

        if (Build.VERSION.SDK_INT >= 26) {
            permissionRequests.add(new RationalePermissionRequest.PermissionRequest(context,
                    Manifest.permission.READ_PHONE_STATE,
                    R.string.text_requestPermissionReadPhoneState,
                    R.string.text_requestPermissionReadPhoneStateSummary));
        }

        return permissionRequests;
    }

    public static boolean checkRunningConditions(Context context)
    {
        for (RationalePermissionRequest.PermissionRequest request : getRequiredPermissions(context))
            if (ActivityCompat.checkSelfPermission(context, request.permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }
}