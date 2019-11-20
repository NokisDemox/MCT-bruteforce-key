package de.syss.MCTBrute4c.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import de.syss.MCTBrute4c.Common;
import de.syss.MCTBrute4c.R;

public class OtherTools extends BasicActivity {

    private final static int FILE_CHOOSER_DUMP_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_tools);

    }


    public void onShowTagInfo(View view) {
        Intent intent = new Intent(this, TagInfoTool.class);
        startActivity(intent);
    }
    public void onShowValueBlock(View view) {
        Intent intent = new Intent(this, ValueBlockTool.class);
        startActivity(intent);
    }
    public void onShowAccessTool(View view) {
        Intent intent = new Intent(this, AccessConditionTool.class);
        startActivity(intent);
    }
    public void onShowDiffTool(View view) {
        Intent intent = new Intent(this, DiffTool.class);
        startActivity(intent);
    }
    public void onShowBccTool(View view) {
        Intent intent = new Intent(this, BccTool.class);
        startActivity(intent);
    }
    public void onShowCloneUidTool(View view) {
        Intent intent = new Intent(this, CloneUidTool.class);
        startActivity(intent);
    }
    public void onShowEMV_cc_check(View view) {
        Intent intent = new Intent(this, EMV_cc_check.class);
        startActivity(intent);
    }

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


}
