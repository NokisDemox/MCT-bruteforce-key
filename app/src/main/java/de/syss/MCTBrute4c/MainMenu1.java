package de.syss.MCTBrute4c;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import de.syss.MCTBrute4c.Activities.HelpAndInfo;
import de.syss.MCTBrute4c.Activities.KeyMapCreator;
import de.syss.MCTBrute4c.Activities.MainMenu;
import de.syss.MCTBrute4c.Activities.Modder_Info;
import de.syss.MCTBrute4c.Activities.OtherTools;
import de.syss.MCTBrute4c.Activities.Preferences;
import de.syss.MCTBrute4c.Activities.TagInfoTool;

public class MainMenu1 extends Activity {
    private Intent mOldIntent = null;
    private int STORAGE_PERMISSION_CODE = 1;
    private boolean mResume = true;

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for the app to work")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu1);
        if (ContextCompat.checkSelfPermission(MainMenu1.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainMenu1.this, "You Have grant storage permission", Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }

        // More testing
        if (!Common.useAsEditorOnly() && !Common.hasMifareClassicSupport()) {
            CharSequence styledText = Html.fromHtml(
                    getString(R.string.dialog_no_mfc_support_device));
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_no_mfc_support_device_title)
                    .setMessage(styledText)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.action_exit_app,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton(R.string.action_continue,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    mResume = true;
                                    checkNfc();
                                }
                            })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .show();
            // Make links clickable.
            ((TextView)ad.findViewById(android.R.id.message)).setMovementMethod(
                    LinkMovementMethod.getInstance());
            mResume = false;
        }


    }

    // testing
    private void checkNfc() {
        // Check if the NFC hardware is enabled.
        if (Common.getNfcAdapter() != null
                && !Common.getNfcAdapter().isEnabled()) {
            // NFC is disabled. Show dialog.
            // Use as editor only?
            if (!Common.useAsEditorOnly()) {

            }
        } else {
            // NFC is enabled. Hide dialog and enable NFC
            // foreground dispatch.
            if (mOldIntent != getIntent()) {
                int typeCheck = Common.treatAsNewTag(getIntent(), this);
                if (typeCheck == -1 || typeCheck == -2) {
                    // Device or tag does not support Mifare Classic.
                    // Run the only thing that is possible: The tag info tool.
                    Intent i = new Intent(this, TagInfoTool.class);
                    startActivity(i);
                }
                mOldIntent = getIntent();
            }
            Common.enableNfcForegroundDispatch(this);
            Common.setUseAsEditorOnly(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mResume) {
            checkNfc();
        }
    }

    // testing

    public void onShowTagInfo_main(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void onShowOtherTools_main(View view) {
        Intent intent = new Intent(this, OtherTools.class);
        startActivity(intent);
    }

    public void onShowPreferences_main(View view) {
        Intent intent = new Intent(this, Preferences.class);
        startActivity(intent);
    }

    public void onShowAbout_main(View view) {
        onShowAboutDialog();
    }
    private void onShowAboutDialog() {
        CharSequence styledText = Html.fromHtml(
                getString(R.string.dialog_about_mct,
                        Common.getVersionCode()));
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_about_mct_title)
                .setMessage(styledText)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(R.string.action_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing.
                            }
                        }).create();
        ad.show();
        // Make links clickable.
        ((TextView)ad.findViewById(android.R.id.message)).setMovementMethod(
                LinkMovementMethod.getInstance());
    }


    public void onShowHelp(View view) {
        Intent intent = new Intent(this, HelpAndInfo.class);
        startActivity(intent);
    }

    public void onOpenModder_Info (View view){
        Intent intent = new Intent(this, Modder_Info.class);
        startActivity(intent);
    }

}
