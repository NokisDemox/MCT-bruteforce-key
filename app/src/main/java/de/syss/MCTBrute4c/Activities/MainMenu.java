/*
 * Copyright 2018 NokisDemox
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package de.syss.MCTBrute4c.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.syss.MCTBrute4c.Common;
import de.syss.MCTBrute4c.Emulator.MainActivity;
import de.syss.MCTBrute4c.R;

/**
 * Main App entry point showing the main menu.
 * Some stuff about the App:
 * <ul>
 * <li>Error/Debug messages (Log.e()/Log.d()) are hard coded</li>
 * <li>This is my first App, so please by decent with me ;)</li>
 * </ul>
 *
 */

public class MainMenu extends BasicActivity {
    private static final String LOG_TAG =
            MainMenu.class.getSimpleName();

    private final static int FILE_CHOOSER_DUMP_FILE = 1;
    private final static int FILE_CHOOSER_KEY_FILE = 2;
    private AlertDialog mEnableNfc;
    private Button mReadTag;
    private Button mUltralight;
    private Button mEmulateTag;
    private Button mWriteTag;
        private boolean mResume = true;
    private Intent mOldIntent = null;


    /**
     * Check for NFC hardware, Mifare Classic support and for external storage.
     * If the directory structure and the std. keys files is not already there
     * it will be created. Also, at the first run of this App, a warning
     * notice and a donate message will be displayed.
     * @see #copyStdKeysFilesIfNecessary()
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Find Read/Write buttons and bind them to member vars.
        mReadTag = (Button) findViewById(R.id.buttonMainReadTag);
        mWriteTag = (Button) findViewById(R.id.buttonMainWriteTag);
        mEmulateTag = (Button) findViewById(R.id.buttonMainEmulator);
        mUltralight = (Button) findViewById(R.id.buttonMainUltralight);

        Common.setUseAsEditorOnly(false);

        // Check if there is an NFC hardware component.
        Common.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        if (Common.getNfcAdapter() == null) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_no_nfc_title)
                    .setMessage(R.string.dialog_no_nfc)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.action_exit_app,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setNeutralButton(R.string.action_editor_only,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Only use Editor.
                                    Common.setUseAsEditorOnly(true);
                                    mReadTag.setEnabled(false);
                                    mWriteTag.setEnabled(false);
                                    mEmulateTag.setEnabled(false);
                                    mUltralight.setEnabled(false);
                                }
                            })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .show();
            mResume = false;
        }

        // Create the directories needed by MCT and clean out the tmp folder.
        if (Common.isExternalStorageWritableErrorToast(this)) {
            // Create keys directory.
            File path = new File(Environment.getExternalStoragePublicDirectory(
                    Common.HOME_DIR) + "/" + Common.KEYS_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR
                        + "/" + Common.KEYS_DIR + "' directory.");
                return;
            }

            // Create dumps directory.
            path = new File(Environment.getExternalStoragePublicDirectory(
                    Common.HOME_DIR) + "/" + Common.DUMPS_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR
                        + "/" + Common.DUMPS_DIR + "' directory.");
                return;
            }

            // Create tmp directory.
            path = new File(Environment.getExternalStoragePublicDirectory(
                    Common.HOME_DIR) + "/" + Common.TMP_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR
                        + Common.TMP_DIR + "' directory.");
                return;
            }
            // Create cc directory.
            path = new File(Environment.getExternalStoragePublicDirectory(
                    Common.HOME_DIR) + "/" + Common.CC_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR
                        + Common.CC_DIR + "' directory.");
                return;
            }
            // Create html directory.
            path = new File(Environment.getExternalStoragePublicDirectory(
                    Common.HOME_DIR) + "/" + Common.HTML_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR
                        + Common.HTML_DIR + "' directory.");
                return;
            }
            // Create mut directory.
            path = new File(Environment.getExternalStoragePublicDirectory(
                    Common.HOME_DIR) + "/" + Common.MUT_DIR);
            if (!path.exists() && !path.mkdirs()) {
                // Could not create directory.
                Log.e(LOG_TAG, "Error while creating '" + Common.HOME_DIR
                        + Common.MUT_DIR + "' directory.");
                return;
            }

            // Create std. key file if there is none.
            copyStdKeysFilesIfNecessary();
        }

        // Create a dialog that send user to NFC settings if NFC is off.
        // (Or let the user use the App in editor only mode / exit the App.)
        mEnableNfc = new AlertDialog.Builder(this)
            .setTitle(R.string.dialog_nfc_not_enabled_title)
            .setMessage(R.string.dialog_nfc_not_enabled)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton(R.string.action_nfc,
                    new DialogInterface.OnClickListener() {
                @Override
                @SuppressLint("InlinedApi")
                public void onClick(DialogInterface dialog, int which) {
                    // Goto NFC Settings.
                    if (Build.VERSION.SDK_INT >= 16) {
                        startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                    } else {
                        startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
             })
             .setNeutralButton(R.string.action_editor_only,
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Only use Editor.
                    Common.setUseAsEditorOnly(true);
                }
             })
             .setNegativeButton(R.string.action_exit_app,
                     new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // Exit the App.
                    finish();
                }
             }).create();

        // Show first usage notice.
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final Editor sharedEditor = sharedPref.edit();
        boolean isFirstRun = sharedPref.getBoolean("is_first_run", true);
        if (isFirstRun) {
            new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_first_run_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.dialog_first_run)
                .setPositiveButton(R.string.action_ok,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (Common.IS_DONATE_VERSION) {
                            mResume = true;
                            checkNfc();
                        }
                        sharedEditor.putBoolean("is_first_run", false);
                        sharedEditor.apply();
                    }
                 })
                .show();
            mResume = false;
        }

        if (Common.IS_DONATE_VERSION) {
            // Do not show the donate dialog.
            return;
        }
        // Show donate dialog.
        int currentVersion = 0;
        try {
            currentVersion = getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.d(LOG_TAG, "Version not found.");
        }
        int lastVersion = sharedPref.getInt("mct_version", currentVersion - 1);
        boolean showDonateDialog = sharedPref.getBoolean(
                "show_donate_dialog", true);
        if (lastVersion < currentVersion || showDonateDialog) {
            // This is either a new version of MCT or the user wants to see
            // the donate dialog.
            if (lastVersion < currentVersion) {
                // Update the version.
                sharedEditor.putInt("mct_version", currentVersion);
                sharedEditor.putBoolean("show_donate_dialog", true);
                sharedEditor.apply();
            }
            View dialogLayout = getLayoutInflater().inflate(
                    R.layout.dialog_donate,
                    (ViewGroup)findViewById(android.R.id.content), false);
            final CheckBox showDonateDialogCheckBox = (CheckBox) dialogLayout
                    .findViewById(R.id.checkBoxDonateDialog);
            new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_donate_title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(dialogLayout)
                .setPositiveButton(R.string.action_beer_sounds_fine,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open Google Play for the donate version of MCT.
                        Uri uri = Uri.parse(
                                "market://details?id=de."
                                + "syss.MifareClassicToolDonate");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store"
                                            + "/apps/details?id=de.syss.Mifare"
                                            + "ClassicToolDonate")));
                        }
                        if (showDonateDialogCheckBox.isChecked()) {
                            // Do not show the donate dialog again.
                            sharedEditor.putBoolean(
                                    "show_donate_dialog", false);
                            sharedEditor.apply();
                        }
                        mResume = true;
                        checkNfc();
                    }
                })
                .setNegativeButton(R.string.action_beer_sounds_fine2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent donate_modder=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/NokisDemoxDonations"));
                                startActivity(donate_modder);
                            }
                        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (showDonateDialogCheckBox.isChecked()) {
                            // Do not show the donate dialog again.
                            sharedEditor.putBoolean(
                                    "show_donate_dialog", false);
                            sharedEditor.apply();
                        }
                        mResume = true;
                        checkNfc();
                    }
                 })
                .show();
            mResume = false;
        }

        // Check if there is Mifare Classic support.
        if (!Common.useAsEditorOnly() && !Common.hasMifareClassicSupport()) {
            // Disable read/write tag options.
            mReadTag.setEnabled(false);
            mWriteTag.setEnabled(false);
            mEmulateTag.setEnabled(false);
            mUltralight.setEnabled(false);
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
    /**
     * If resuming is allowed because all dependencies from
     * {@link #onCreate(Bundle)} are satisfied, call
     * {@link #checkNfc()}
     * @see #onCreate(Bundle)
     * @see #checkNfc()
     */
    @Override
    public void onResume() {
        super.onResume();

        if (mResume) {
            checkNfc();
        }
    }

    /**
     * Check if NFC adapter is enabled. If not, show the user a dialog and let
     * him choose between "Goto NFC Setting", "Use Editor Only" and "Exit App".
     * Also enable NFC foreground dispatch system.
     * @see Common#enableNfcForegroundDispatch(Activity)
     */
    private void checkNfc() {
        // Check if the NFC hardware is enabled.
        if (Common.getNfcAdapter() != null
                && !Common.getNfcAdapter().isEnabled()) {
            // NFC is disabled. Show dialog.
            // Use as editor only?
            if (!Common.useAsEditorOnly()) {
                mEnableNfc.show();
                // Disable read/write tag options.
                mReadTag.setEnabled(false);
                mWriteTag.setEnabled(false);
                mEmulateTag.setEnabled(false);
                mUltralight.setEnabled(false);
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
            mEnableNfc.hide();
            if (Common.hasMifareClassicSupport()) {
                mReadTag.setEnabled(true);
                mWriteTag.setEnabled(true);
                mEmulateTag.setEnabled(true);
                mUltralight.setEnabled(true);
            }
        }
    }

    /**
     * Disable NFC foreground dispatch system.
     * @see Common#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onPause() {
        super.onPause();
        Common.disableNfcForegroundDispatch(this);
    }

    /**
     * Handle new Intent as a new tag Intent and if the tag/device does not
     * support Mifare Classic, then run {@link TagInfoTool}.
     * @see Common#treatAsNewTag(Intent, android.content.Context)
     * @see TagInfoTool
     */
    @Override
    public void onNewIntent(Intent intent) {
        int typeCheck = Common.treatAsNewTag(intent, this);
        if (typeCheck == -1 || typeCheck == -2) {
            // Device or tag does not support Mifare Classic.
            // Run the only thing that is possible: The tag info tool.
            Intent i = new Intent(this, TagInfoTool.class);
            startActivity(i);
        }
    }

    /**
     * Show the {@link ReadTag}.
     * @param view The View object that triggered the method
     * (in this case the read tag button).
     * @see ReadTag
     */
    public void onShowReadTag(View view) {
        Intent intent = new Intent(this, ReadTag.class);
        startActivity(intent);
    }
    public void onShowUltralightReader(View view) {
        Intent intent = new Intent(this, Ultralight_activity.class);
        startActivity(intent);
    }

    /**
     * Show the {@link WriteTag}.
     * @param view The View object that triggered the method
     * (in this case the write tag button).
     * @see WriteTag
     */
    public void onShowWriteTag(View view) {
        Intent intent = new Intent(this, WriteTag.class);
        startActivity(intent);
    }
    public void onShowEmulator(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Open a file chooser ({@link FileChooser}). The
     * Activity result will be processed in
     * {@link #onActivityResult(int, int, Intent)}.
     * If the dump files folder is empty display an additional error
     * message.
     * @param view The View object that triggered the method
     * (in this case the show/edit tag dump button).
     * @see FileChooser
     * @see #onActivityResult(int, int, Intent)
     */
    public void onOpenTagDumpEditor(View view) {
        String dumpsDir = Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + "/" + Common.DUMPS_DIR;
        if (Common.isExternalStorageWritableErrorToast(this)) {
            File file = new File(dumpsDir);
            if (file.isDirectory() && (file.listFiles() == null
                    || file.listFiles().length == 0)) {
                Toast.makeText(this, R.string.info_no_dumps,
                    Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(this, FileChooser.class);
            intent.putExtra(FileChooser.EXTRA_DIR, dumpsDir);
            intent.putExtra(FileChooser.EXTRA_TITLE,
                    getString(R.string.text_open_dump_title));
            intent.putExtra(FileChooser.EXTRA_BUTTON_TEXT,
                    getString(R.string.action_open_dump_file));
            intent.putExtra(FileChooser.EXTRA_ENABLE_DELETE_FILE, true);
            startActivityForResult(intent, FILE_CHOOSER_DUMP_FILE);
        }
    }

    /**
     * Open a file chooser ({@link FileChooser}). The
     * Activity result will be processed in
     * {@link #onActivityResult(int, int, Intent)}.
     * @param view The View object that triggered the method
     * (in this case the show/edit key button).
     * @see FileChooser
     * @see #onActivityResult(int, int, Intent)
     */
    public void onOpenKeyEditor(View view) {
        if (Common.isExternalStorageWritableErrorToast(this)) {
            Intent intent = new Intent(this, FileChooser.class);
            intent.putExtra(FileChooser.EXTRA_DIR,
                    Environment.getExternalStoragePublicDirectory(
                            Common.HOME_DIR) + "/" + Common.KEYS_DIR);
            intent.putExtra(FileChooser.EXTRA_TITLE,
                    getString(R.string.text_open_key_file_title));
            intent.putExtra(FileChooser.EXTRA_BUTTON_TEXT,
                    getString(R.string.action_open_key_file));
            intent.putExtra(FileChooser.EXTRA_ENABLE_NEW_FILE, true);
            intent.putExtra(FileChooser.EXTRA_ENABLE_DELETE_FILE, true);
            startActivityForResult(intent, FILE_CHOOSER_KEY_FILE);
        }
    }

    /**
     * Run the {@link DumpEditor} or the {@link KeyEditor}
     * if file chooser result is O.K.
     * @see DumpEditor
     * @see KeyEditor
     * @see #onOpenTagDumpEditor(View)
     * @see #onOpenKeyEditor(View)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
        case FILE_CHOOSER_DUMP_FILE:
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(this, DumpEditor.class);
                intent.putExtra(FileChooser.EXTRA_CHOSEN_FILE,
                        data.getStringExtra(
                                FileChooser.EXTRA_CHOSEN_FILE));
                startActivity(intent);
            }
            break;
        case FILE_CHOOSER_KEY_FILE:
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(this, KeyEditor.class);
                intent.putExtra(FileChooser.EXTRA_CHOSEN_FILE,
                        data.getStringExtra(
                                FileChooser.EXTRA_CHOSEN_FILE));
                startActivity(intent);
            }
            break;
        }
    }

    /**
     * Copy the standard key files ({@link Common#STD_KEYS} and
     * {@link Common#STD_KEYS_EXTENDED}) form assets to {@link Common#KEYS_DIR}.
     * Key files are simple text files. Any plain text editor will do the trick.
     * All key and dump data from this App is stored in
     * getExternalStoragePublicDirectory(Common.HOME_DIR) to remain
     * there after App uninstallation.
     * @see Common#KEYS_DIR
     * @see Common#HOME_DIR
     * @see Common#copyFile(InputStream, OutputStream)
     */
    private void copyStdKeysFilesIfNecessary() {
        File std = new File(Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + "/" + Common.KEYS_DIR, Common.STD_KEYS);
        File extended = new File(Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + "/" + Common.KEYS_DIR,
                Common.STD_KEYS_EXTENDED);
        File full = new File(Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + "/" + Common.KEYS_DIR,
                Common.STD_KEYS_FULL);
        File cc = new File(Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + "/" + Common.CC_DIR,
                Common.STD_CC);
        File mut = new File(Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + "/" + Common.MUT_DIR,
                Common.STD_MUT);
        File html = new File(Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + "/" + Common.HTML_DIR,
                Common.STD_HTML);
        File logo = new File (Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + "/" + Common.HTML_DIR,
                Common.STD_LOGO);
        AssetManager assetManager = getAssets();

        if (!std.exists()) {
            // Copy std.keys.
            try {
                InputStream in = assetManager.open(
                        Common.KEYS_DIR + "/" + Common.STD_KEYS);
                OutputStream out = new FileOutputStream(std);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
              } catch(IOException e) {
                  Log.e(LOG_TAG, "Error while copying 'std.keys' from assets "
                          + "to external storage.");
              }
        }
        if (!extended.exists()) {
            // Copy extended-std.keys.
            try {
                InputStream in = assetManager.open(
                        Common.KEYS_DIR + "/" + Common.STD_KEYS_EXTENDED);
                OutputStream out = new FileOutputStream(extended);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
              } catch(IOException e) {
                  Log.e(LOG_TAG, "Error while copying 'extended-std.keys' "
                          + "from assets to external storage.");
              }
        }
        if (!full.exists()) {
            // Copy full.keys.
            try {
                InputStream in = assetManager.open(
                        Common.KEYS_DIR + "/" + Common.STD_KEYS_FULL);
                OutputStream out = new FileOutputStream(full);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error while copying 'full.keys' "
                        + "from assets to external storage.");
            }
        }
        if (!cc.exists()) {
            // Copy test.cc
            try {
                InputStream in = assetManager.open(
                        Common.CC_DIR + "/" + Common.STD_CC);
                OutputStream out = new FileOutputStream(cc);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error while copying 'ultralight-test.mut' "
                        + "from assets to external storage.");
            }
        }
        if (!html.exists()) {
            // Copy help.html
            try {
                InputStream in = assetManager.open(
                        Common.HTML_DIR + "/" + Common.STD_HTML);
                OutputStream out = new FileOutputStream(html);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error while copying 'ultralight-test.mut' "
                        + "from assets to external storage.");
            }
        }
        if (!mut.exists()) {
            // Copy help.html
            try {
                InputStream in = assetManager.open(
                        Common.MUT_DIR + "/" + Common.STD_MUT);
                OutputStream out = new FileOutputStream(mut);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error while copying 'ultralight-test.mut' "
                        + "from assets to external storage.");
            }
        }
        if (!logo.exists()) {
            // Copy help.html
            try {
                InputStream in = assetManager.open(
                        Common.HTML_DIR + "/" + Common.STD_LOGO);
                OutputStream out = new FileOutputStream(logo);
                Common.copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error while copying 'icon.png' "
                        + "from assets to external storage.");
            }
        }
    }
}


