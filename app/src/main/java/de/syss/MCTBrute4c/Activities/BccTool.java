package de.syss.MCTBrute4c.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import de.syss.MCTBrute4c.R;
import de.syss.MCTBrute4c.Common;

public class BccTool extends BasicActivity {

    private EditText mUid;
    private EditText mBcc;

    /**
     * Initialize the some member variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcc_tool);

        mUid = (EditText) findViewById(R.id.editTextBccToolUid);
        mBcc = (EditText) findViewById(R.id.editTextBccToolBcc);
    }

    /**
     * Calculate the BCC value of the given UID (part). This is done calling
     * {@link Common#calcBCC(byte[])} after some input checks (is the length
     * of the given UID valid; are there only hex symbols).
     * @param view The View object that triggered the method
     * (in this case the calculate BCC button).
     * @see Common#calcBCC(byte[])
     */
    public void onCalculate(View view) {
        String data = mUid.getText().toString();
        if (data.length() != 8) {
            // Error. UID (parts) are 4 bytes long.
            Toast.makeText(this, R.string.info_invalid_uid_length,
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (!data.matches("[0-9A-Fa-f]+")) {
            // Error, not hex.
            Toast.makeText(this, R.string.info_not_hex_data,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Calculate the BCC.
        byte bcc = Common.calcBCC(Common.hexStringToByteArray(data));
        mBcc.setText(String.format("%02X", bcc));
    }

    /**
     * Copy the calculated BCC to the Android clipboard.
     * @param view The View object that triggered the method
     * (in this case the copy button).
     */
    public void onCopyToClipboard(View view) {
        Common.copyToClipboard(mBcc.getText().toString(), this, true);
    }

    /**
     * Paste the content of the Android clipboard (if plain text) to the
     * UID edit text.
     * @param view The View object that triggered the method
     * (in this case the paste button).
     */
    public void onPasteFromClipboard(View view) {
        String text = Common.getFromClipboard(this);
        if (text != null) {
            mUid.setText(text);
        }
    }
    public void onShowTagInfo(View view) {
        Intent intent = new Intent(this, TagInfoTool.class);
        startActivity(intent);
    }
}
