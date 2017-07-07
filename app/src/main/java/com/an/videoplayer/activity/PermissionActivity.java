package com.an.videoplayer.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.an.videoplayer.R;
import com.an.videoplayer.utils.permission.PermissionManager;
import com.an.videoplayer.utils.permission.PermissionUtils;

public abstract class PermissionActivity extends BaseActivity implements PermissionManager.PermissionCallback {

    protected PermissionManager permissionManager;
    protected PermissionUtils permissionUtils;
    protected String PERMISSION_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionManager = new PermissionManager(this);
        permissionUtils = new PermissionUtils(this);
    }

    protected void askPermission() {
        if (!permissionUtils.isPermissionGranted(PERMISSION_STORAGE)) {
            permissionManager.showPermissionDialog(PERMISSION_STORAGE)
                    .withCallback(this)
                    .withDenyDialogEnabled(true)
                    .withDenyDialogMsg(getString(R.string.permission_storage))
                    .build();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.handleResult(requestCode, permissions, grantResults);
    }
}
