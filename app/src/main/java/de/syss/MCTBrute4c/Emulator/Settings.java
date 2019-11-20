package de.syss.MCTBrute4c.Emulator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.syss.MCTBrute4c.R;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_emulator);



        Button add=(Button) findViewById(R.id.button2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText uid= (EditText) findViewById(R.id.editText);
                String UID= uid.getText().toString();
                Intent in=new Intent();
                in.setClass(Settings.this,MainActivity.class);
                in.putExtra("UID",UID);
                setResult(2,in);
                finish();
            }
        });

    }

    public void onAddTag (View v) {
        EditText uid= (EditText) findViewById(R.id.editText);
        String UID= uid.getText().toString();
        Intent in=new Intent();
        in.setClass(Settings.this,MainActivity.class);
        in.putExtra("UID",UID);
        setResult(2,in);
        finish();

    }
}
