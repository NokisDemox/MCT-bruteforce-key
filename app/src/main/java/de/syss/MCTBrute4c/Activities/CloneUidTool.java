package de.syss.MCTBrute4c.Activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import de.syss.MCTBrute4c.Common;
import de.syss.MCTBrute4c.Emulator.MainActivity;
import de.syss.MCTBrute4c.MCReader;
import de.syss.MCTBrute4c.R;

public class CloneUidTool extends Activity {

    private EditText mUid;
    private EditText mEditTextBlock0Rest;
    private EditText mEditTextBlock0Key;
    private CheckBox mShowOptions;
    private RadioButton mRadioButtonKeyB;
    private TextView mStatusLogContent;

    private String mBlock0Complete = "";
    // Taken from sample card, in most cases it will not matter because bad
    // access control systems only check for the UID.
    private String mBlock0Rest = "08040001A2EC736FC3351D";
    // Default key to write to a factory formatted block 0 of "magic tag gen2".
    private String mBlock0Key = "FFFFFFFFFFFF";
    private enum Status { INIT, BLOCK0_CALCULATED, CLONED }
    private Status mStatus = Status.INIT;
    public NotificationCompat.Builder notificacion_cloneuid;

    /**
     * Initialize some member variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clone_uid_tool);

        mUid = (EditText) findViewById(R.id.editTextCloneUidToolOriginalUid);
        mEditTextBlock0Rest = (EditText) findViewById(R.id.editTextCloneUidToolBlock0Rest);
        mEditTextBlock0Key = (EditText) findViewById(R.id.editTextCloneUidToolWriteKey);
        mStatusLogContent = (TextView) findViewById(
                R.id.textViewCloneUidToolStatusLogContent);
        mShowOptions = (CheckBox) findViewById(
                R.id.checkBoxCloneUidToolOptions);
        mRadioButtonKeyB = (RadioButton) findViewById(
                R.id.radioButtonCloneUidToolKeyB);

        mEditTextBlock0Rest.setText(mBlock0Rest);
        mEditTextBlock0Key.setText(mBlock0Key);

        // If a tag was scanned before, fill the UID.
        if (Common.getTag() != null) {
            mUid.setText(Common.byte2HexString(Common.getUID()));
            appendToLog(getString(R.string.text_use_uid_of_scanned_tag)
                    + " (" + Common.byte2HexString(Common.getUID()) + ")");
        }
    }

    /**
     * Handle new Intent as a new tag Intent and tread them according to
     * the {@link #mStatus}.
     */
    @Override
    public void onNewIntent(Intent intent) {
        int typeCheck = Common.treatAsNewTag(intent, this);
        if (typeCheck == -1 || typeCheck == -2) {
            // Device or tag does not support MIFARE Classic.
            // Run the only thing that is possible: The tag info tool.
            Intent i = new Intent(this, TagInfoTool.class);
            startActivity(i);
        }
        // Was the new intent a new tag?
        if (typeCheck != -4) {
            String uid = Common.byte2HexString(Common.getUID());
            switch (mStatus) {
                case INIT:
                    // Original tag scanned.
                    mUid.setText(uid);
                    appendToLog(getString(
                            R.string.text_use_uid_of_scanned_tag)
                            + " (" + Common.byte2HexString(Common.getUID())
                            + ")");
                    break;
                case BLOCK0_CALCULATED:
                    // UID is present, block 0 calculated.
                    // Write to magic tag gen2.
                    writeManufacturerBlock();
                    break;
                case CLONED:
                    // Confirm the UID is cloned.
                    // Read it again from current tag.
                    appendToLog(getString(R.string.text_checking_clone));
                    String uidOriginal = mUid.getText().toString();
                    if (uid.equals(uidOriginal)) {
                        appendToLog(getString(R.string.text_clone_successfully));
                        mStatus = Status.INIT;
                    } else {
                        appendToLog(getString(R.string.text_uid_match_error)
                                + " (" + uidOriginal + " <-> " + uid + ")");
                        appendToLog(getString(R.string.text_clone_error));
                        mStatus = Status.BLOCK0_CALCULATED;
                    }
            }
        }
    }

    /**
     * Calculate the BCC of the UID and create block 0
     * (manufacturer block) by concatenating UID, BCC and the
     * rest of block 0 ({@link #mBlock0Rest}).
     * @param view The View object that triggered the method
     * (in this case the "calculate block 0 and clone uid" button).
     */
    public void onCalculateBlock0(View view) {
        String uid = mUid.getText().toString();
        mBlock0Rest = mEditTextBlock0Rest
                .getText().toString();
        mBlock0Key = mEditTextBlock0Key
                .getText().toString();

        // Check if all data is HEX.
        if (!mBlock0Rest.matches("[0-9A-Fa-f]+")
                || !uid.matches("[0-9A-Fa-f]+")
                || !mBlock0Key.matches("[0-9A-Fa-f]+")) {
            // Error, not hex.
            Toast.makeText(this, R.string.info_not_hex_data,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Check key field.
        if (mBlock0Key.length() != 12) {
            Toast.makeText(this, R.string.info_valid_keys_not_6_byte,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Check the UID length.
        if (uid.length() != 8) {
            // Error: no 4 bytes UID. 7 and 10 bytes UID not supported (yet).
            Toast.makeText(this, R.string.info_invalid_uid_length,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Check rest of block 0..
        if (mBlock0Rest.length() != 22) {
            Toast.makeText(this,
                    R.string.info_rest_of_block_0_length,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Calculate BCC and cuonstruct full block 0.
        byte bcc = Common.calcBCC(Common.hexStringToByteArray(uid));
        mBlock0Complete = uid + String.format("%02X", bcc) + mBlock0Rest;
        mStatus = Status.BLOCK0_CALCULATED;
        appendToLog(getString(R.string.text_block_0_calculated)
                + " (" + mBlock0Complete + ")");
        appendToLog(getString(R.string.text_waiting_for_magic_tag));
        // Hide options.
        mShowOptions.setChecked(false);
        onShowOptions(null);
    }

    /**
     * Write block 0 (manufacturer block) with {@link #mBlock0Complete}
     * containing the cloned UID.
     */
    private void writeManufacturerBlock() {

        boolean keyB = mRadioButtonKeyB.isChecked();
        byte[] key = Common.hexStringToByteArray(
                mEditTextBlock0Key.getText().toString());

        // Create reader.
        MCReader reader = Common.checkForTagAndCreateReader(this);
        if (reader == null) {
            return;
        }

        // Check UID length of magic card gen2.
        byte[] uid = Common.getUID();
        if (uid.length != 4) {
            // Error: no 4 bytes UID. 7 and 10 bytes UID not supported (yet).
            Toast.makeText(this, R.string.info_invalid_uid_length,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Write to block 0.
        appendToLog(getString(R.string.text_writing_block_0));
        int result = reader.writeBlock(0, 0,
                Common.hexStringToByteArray(mBlock0Complete), key, keyB);

        // Error handling.
        switch (result) {
            case 4:
                Toast.makeText(this, R.string.info_incorrect_key,
                        Toast.LENGTH_LONG).show();
                return;
            case -1:
                Toast.makeText(this, R.string.info_write_error,
                        Toast.LENGTH_LONG).show();
                appendToLog(getString(R.string.text_clone_error));
                return;
        }

        appendToLog(getString(R.string.text_no_errors_on_write));
        appendToLog(getString(R.string.text_rescan_tag_to_check));
        mStatus = Status.CLONED;
        reader.close();
    }

    /**
     * Append a text to the status log.
     * @param text The text to append to the status log.
     */
    private void appendToLog(String text) {
        CharSequence content = mStatusLogContent.getText();
        String newline = "";
        if (!content.equals("")) {
            newline = "\n";
        }
        content = content + newline + "\u2022 " + text;
        mStatusLogContent.setText(content);
    }

    /**
     * Show / hide options.
     * @param view The View object that triggered the method
     * (in this case the "show options" check box).
     */
    public void onShowOptions(View view) {
        LinearLayout optionsLayout = (LinearLayout) findViewById(
                R.id.linearLayoutOptions);
        if (mShowOptions.isChecked()) {
            optionsLayout.setVisibility(View.VISIBLE);
        } else {
            optionsLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Show general information about this tool and cloning
     * UIDs of MIFARE Classic tags.
     * @param view The View object that triggered the method
     * (in this case the "show info" button).
     */
    public void onShowInfo(View view) {
        android.app.AlertDialog.Builder showinfo = new android.app.AlertDialog.Builder(this);
        showinfo.setTitle(R.string.dialog_gen2_tags_info_title);
        showinfo.setMessage(R.string.dialog_gen2_tags_info);
        showinfo.setIcon(R.mipmap.ic_launcher);
        showinfo.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CloneUidTool.this, R.string.dialog_closed, Toast.LENGTH_SHORT).show();
            }
        });
        showinfo.show();
    }



    /**
     * Show information about how the rest of the block 0 is
     * used during the UID cloning process.
     * @param view The View object that triggered the method
     * (in this case the "show info" button).
     */
    public void onShowBlock0RestInfo(View view) {
        android.app.AlertDialog.Builder showblock0info = new android.app.AlertDialog.Builder(this);
        showblock0info.setTitle(R.string.dialog_rest_of_block_0_title);
        showblock0info.setMessage(R.string.dialog_rest_of_block_0);
        showblock0info.setIcon(R.mipmap.ic_launcher);
        showblock0info.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CloneUidTool.this, R.string.dialog_closed, Toast.LENGTH_SHORT).show();
            }
        });
        showblock0info.show();
    }

    /**
     * Show information about the key which is needed
     * to write to block 0.
     * @param view The View object that triggered the method
     * (in this case the "show info" button).
     */
    public void onShowWriteKeyInfo(View view) {
        android.app.AlertDialog.Builder showwritekeyinfo = new android.app.AlertDialog.Builder(this);
        showwritekeyinfo.setTitle(R.string.dialog_key_for_block_0_title);
        showwritekeyinfo.setMessage(R.string.dialog_key_for_block_0);
        showwritekeyinfo.setIcon(R.mipmap.ic_launcher);
        showwritekeyinfo.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CloneUidTool.this, R.string.dialog_closed, Toast.LENGTH_SHORT).show();
            }
        });
        showwritekeyinfo.show();
    }

    /**
     * Paste the content of the Android clipboard (if plain text) to the
     * UID edit text.
     * @param view The View object that triggered the method
     * (in this case the paste button).
     */
    public void onPasteUidFromClipboard(View view) {
        String text = Common.getFromClipboard(this);
        if (text != null) {
            mUid.setText(text);
        }
    }
}
