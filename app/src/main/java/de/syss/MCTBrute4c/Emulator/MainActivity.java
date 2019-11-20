package de.syss.MCTBrute4c.Emulator;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootShell.RootShell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.syss.MCTBrute4c.Activities.EMV_cc_check;
import de.syss.MCTBrute4c.Activities.KeyMapCreator;
import de.syss.MCTBrute4c.R;

public class MainActivity extends Activity {
    int item_id;
    String cardNFC;
    NotificationCompat.Builder notification_emulate_UID;
    private static final int idUnica_emulate = 0000;
    SparseArray<String> IDmap= new SparseArray<String>();


    private static boolean isRooted() {return RootShell.isAccessGiven();}

    private static void showcurrent(TextView textView, SharedPreferences spcu){
        boolean isexisted=new File(Environment.getRootDirectory().getPath()+"/etc/libnfc-nxp.conf.bak").exists();
        if(isexisted) {
            if(spcu.contains("current"))
                textView.setText(spcu.getString("current",""));
            else
                textView.setText("N/A");
        }
        else {
            SharedPreferences.Editor editor=spcu.edit();
            editor.putString("current","N/A");
            editor.apply();
            textView.setText("N/A");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_emulator);

        boolean ir=isRooted();
        Toast suc=Toast.makeText(getApplicationContext(), R.string.emulator_root, Toast.LENGTH_SHORT);
        Toast fai=Toast.makeText(getApplicationContext(),R.string.emulator_not_root,Toast.LENGTH_SHORT);
        if(ir&&isfirstrun()) {
            suc.show();
            SharedPreferences first = getSharedPreferences("first", MODE_PRIVATE);
            SharedPreferences.Editor editor=first.edit();
            editor.putString("first","no");
            editor.apply();
        }
        else if(!ir&&isfirstrun()) {
            fai.show();
            finish();
        }
        showcurrent((TextView) findViewById(R.id.textView3),getSharedPreferences("current",MODE_PRIVATE));
        showUIDlst(this);
        ListView lst= (ListView) findViewById(R.id.listView);
        lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_id=i;
                return false;
            }
        });
        lst.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0,0,0,R.string.action_emulator_emulate);
                contextMenu.add(0,1,0,R.string.action_emulator_rename);
                contextMenu.add(0,2,0,R.string.action_emulator_delete);
            }
        });
    }


    public boolean onContextItemSelected(MenuItem item) {
        String card= IDmap.get(item_id);
        card = cardNFC;
        SharedPreferences spuid = getSharedPreferences("UID", MODE_PRIVATE);
        SharedPreferences.Editor euid = spuid.edit();
        SharedPreferences spname = getSharedPreferences("name", MODE_PRIVATE);
        SharedPreferences.Editor ename = spname.edit();
        SharedPreferences spcu = getSharedPreferences("current", MODE_PRIVATE);
        SharedPreferences.Editor ecu = spcu.edit();
        switch (item.getItemId()) {
            case 0:
                if(spcu.contains("current")&&!spcu.getString("current","").equals("N/A"))
                    Toast.makeText(getApplicationContext(),R.string.emulator_first_restore, Toast.LENGTH_SHORT).show();
                else{
                    //挂载system为rw
                    java.lang.Process p = null;
                    try {
                        p = Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o rw,remount " + Environment.getRootDirectory().getPath()});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert p != null;
                        p.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean isexisted = new File(Environment.getRootDirectory().getPath() + "/etc/libnfc-nxp.conf.bak").exists();
                    if (!isexisted) {
                        try {
                            p = Runtime.getRuntime().exec(new String[]{"su", "-c", "cp " + Environment.getRootDirectory().getPath() + "/etc/libnfc-nxp.conf " + Environment.getRootDirectory().getPath() + "/etc/libnfc-nxp.conf.bak"});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        p.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        p = Runtime.getRuntime().exec(new String[]{"su", "-c", "sed -i \"s/01, 02, 03, 04/" + card + "/g\" " + Environment.getRootDirectory().getPath() + "/etc/libnfc-nxp.conf"});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        p.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //挂载system为ro
                    try {
                        p = Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o ro,remount " + Environment.getRootDirectory().getPath()});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        p.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //重启NFC服务
                    try {
                        p = Runtime.getRuntime().exec(new String[]{"su", "-c", "svc nfc disable"});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        p.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        p = Runtime.getRuntime().exec(new String[]{"su", "-c", "svc nfc enable"});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        p.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast esuc = Toast.makeText(getApplicationContext(), card + " Emulated!", Toast.LENGTH_SHORT);
                    esuc.show();
                    ecu.putString("current", card);
                    ecu.apply();
                    showcurrent((TextView) findViewById(R.id.textView3), getSharedPreferences("current", MODE_PRIVATE));
                    onNotifyEmulator();
                }
                break;
            case 1: //修改卡片名
                final EditText editText=new EditText(this);
                editText.setText(spname.getString(R.string.emulator_card+card,""));
                new AlertDialog.Builder(this).setTitle("New Name:").setView(
                        editText).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences spname = getSharedPreferences("name", MODE_PRIVATE);
                        SharedPreferences.Editor ename = spname.edit();
                        ename.putString(R.string.emulator_card+IDmap.get(item_id),editText.getText().toString());
                        ename.apply();
                        showUIDlst(MainActivity.this);
                        dialogInterface.dismiss();
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                break;
            case 2:

                euid.remove(R.string.emulator_card+card);
                euid.apply();
                ename.remove(R.string.emulator_card+card);
                ename.apply();
                showUIDlst(this);
                Toast suc=Toast.makeText(getApplicationContext(), card+" Deleted!", Toast.LENGTH_SHORT);
                suc.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences spname = getSharedPreferences("name", MODE_PRIVATE);
        SharedPreferences.Editor ename = spname.edit();
        if(resultCode==1||resultCode==2) {
            Bundle bundle = data.getExtras();
            String uid = bundle.getString("UID");
            SharedPreferences spuid = getSharedPreferences("UID", MODE_PRIVATE);
            SharedPreferences.Editor euid = spuid.edit();
            euid.putString(R.string.emulator_card + uid, uid);
            euid.apply();
            ename.putString(R.string.emulator_card+uid,"(null)");
            ename.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUIDlst(this);
        showcurrent((TextView) findViewById(R.id.textView3),getSharedPreferences("current",MODE_PRIVATE));
    }
    public void showUIDlst(Context context){
        List<Map<String, String>> listmap=new ArrayList<>();
        SharedPreferences spname = getSharedPreferences("name", MODE_PRIVATE);
        SharedPreferences spuid = getSharedPreferences("UID", MODE_PRIVATE);
        Map<String, ?> allContent = spuid.getAll();
        int i=0;
        for(Map.Entry<String, ?>  entry : allContent.entrySet()){
            Map<String, String> map1 = new HashMap<>();
            map1.put("name",spname.getString("card:"+entry.getValue(),""));
            map1.put("uid",(String) entry.getValue());
            listmap.add(map1);
            IDmap.put(i++,(String)entry.getValue());
        }
        ListView lst= (ListView) findViewById(R.id.listView);
        lst.setAdapter(new SimpleAdapter(context,listmap,android.R.layout.simple_list_item_2,new String[]{"name","uid"},new int[]{android.R.id.text1,android.R.id.text2}));
    }
    private boolean isfirstrun(){
        SharedPreferences first = getSharedPreferences("first", MODE_PRIVATE);
        return first.getString("first","yes").equals("yes");
    }
    public void onShowEmulatorSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    // RESTORE NFC STATUS

    public void onRestoreNFC(View view) {
        boolean isexisted=false;
        File f=new File(Environment.getRootDirectory().getPath()+"/etc/libnfc-nxp.conf.bak");
        if(f.exists())
            isexisted=true;
        if(isexisted){
            //挂载system为rw
            Process p=null;
            try {
                p=Runtime.getRuntime().exec(new String[]{"su","-c","mount -o rw,remount "+Environment.getRootDirectory().getPath()});
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                p=Runtime.getRuntime().exec(new String[]{"su","-c","mv "+Environment.getRootDirectory().getPath()+"/etc/libnfc-nxp.conf.bak "+Environment.getRootDirectory().getPath()+"/etc/libnfc-nxp.conf"});
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //挂载system为ro
            try {
                p=Runtime.getRuntime().exec(new String[]{"su","-c","mount -o ro,remount "+Environment.getRootDirectory().getPath()});
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //重启NFC服务
            try {
                p=Runtime.getRuntime().exec(new String[]{"su","-c","svc nfc disable"});
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                p=Runtime.getRuntime().exec(new String[]{"su","-c","svc nfc enable"});
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast suc=Toast.makeText(getApplicationContext(), R.string.emulator_already_restore, Toast.LENGTH_SHORT);
            suc.show();
            SharedPreferences spcu = getSharedPreferences("current", MODE_PRIVATE);
            SharedPreferences.Editor ecu = spcu.edit();
            ecu.putString("current","N/A");
            ecu.apply();
        }
        else{
            Toast suc=Toast.makeText(getApplicationContext(), R.string.emulator_no_restore, Toast.LENGTH_SHORT);
            suc.show();
        }
    }

    // Scan for new UID
    public void onScanNewUID (View view){
        Intent intent = new Intent(MainActivity.this,Scanning.class);
        startActivityForResult(intent,1);
    }
    //Notification for emulation
    public void onNotifyEmulator () {
        notification_emulate_UID = new NotificationCompat.Builder(this);
        notification_emulate_UID.setAutoCancel(true);
        notification_emulate_UID.setSmallIcon(R.mipmap.ic_launcher);
        notification_emulate_UID.setContentTitle(R.string.dialog_emulator_title+"");
        Intent IntentShowEmulator = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,IntentShowEmulator,PendingIntent.FLAG_UPDATE_CURRENT);
        notification_emulate_UID.setContentIntent(pendingIntent);
        final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification_emulate_UID.setContentText(R.string.dialog_emulator_content + cardNFC);
        nm.notify(idUnica_emulate,notification_emulate_UID.build());

    }

    // Alerts dialogs
    public void onShowEmulateInfo() {
        android.app.AlertDialog.Builder ventana2 = new android.app.AlertDialog.Builder(this);
        ventana2.setTitle(R.string.not_emulator_title);
        ventana2.setMessage(R.string.not_emulator_message);
        ventana2.setIcon(R.mipmap.ic_launcher);
        ventana2.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, R.string.dialog_closed, Toast.LENGTH_SHORT).show();
            }
        });
        ventana2.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.emv_functions, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.menuEMVShowInfo:
                onShowEmulateInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
