package com.aymsou.rstaurantsapp.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import com.aymsou.rstaurantsapp.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public final class Permission {

    private Activity mContext;
    private String[] mRequiredPermissions;
    private List<String> mPermissionsToRequest = new ArrayList<>();

    public Permission(Activity context, String... requiredPermissions) {
        mContext = context;
        mRequiredPermissions = requiredPermissions;
    }

    public boolean checkPermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1)
            return true;
        for (String permission : mRequiredPermissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionsToRequest.add(permission);
            }
        }
        return mPermissionsToRequest.isEmpty();
    }

    public void requestPermissions(int requestCode) {
        ActivityCompat.requestPermissions(mContext, mPermissionsToRequest.toArray(new String[mPermissionsToRequest.size()]), requestCode);
    }

    public void showTipDialog(Activity context, final int requestCode){
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(R.string.enable_permissions);
        dlgAlert.setTitle(R.string.permissions_required);
        dlgAlert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(requestCode);
                dialog.dismiss();
            }
        });
        dlgAlert.setNegativeButton(R.string.nop, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public boolean areAllRequiredPermissionsGranted(String[] permissions, int[] grantResults) {
        if (permissions == null || permissions.length == 0 || grantResults == null || grantResults.length == 0) {
            return false;
        }
        LinkedHashMap<String, Integer> perms = new LinkedHashMap<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!perms.containsKey(permissions[i]) || (perms.containsKey(permissions[i]) && perms.get(permissions[i]) == PackageManager.PERMISSION_DENIED))
                perms.put(permissions[i], grantResults[i]);
        }
        for (Map.Entry<String, Integer> entry : perms.entrySet()) {
            if (entry.getValue() != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}