package de.syss.MCTBrute4c.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import de.syss.MCTBrute4c.Activities.KeyMapCreator;
import de.syss.MCTBrute4c.Activities.Preferences;
import de.syss.MCTBrute4c.Common;
import de.syss.MCTBrute4c.MCReader;
import de.syss.MCTBrute4c.R;

import static de.syss.MCTBrute4c.Activities.KeyMapCreator.EXTRA_KEYS_DIR;
import static de.syss.MCTBrute4c.Activities.KeyMapCreator.EXTRA_keyfiles;
import static de.syss.MCTBrute4c.Activities.KeyMapCreator.EXTRA_keynames;

public class StealthService extends Service{
    private File mKeyDirPath;
    private LinearLayout mKeyFilesGroup;
    private final Handler mHandler = new Handler();
    public int sLastSector;
    public int sFirstSector;
    public int sProgressStatus;
    public String sSectorRange;
    public boolean sIsCreatingKeyMap;
    public ArrayList<String> fileNames;
    public ArrayList<File> keyFiles;
    public NotificationCompat.Builder notification_stealth;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Import Variables
        int last = intent.getIntExtra(KeyMapCreator.EXTRA_Lastsector,0);
        int first = intent.getIntExtra(KeyMapCreator.EXTRA_Firstsector,0);
        int progressStat = intent.getIntExtra(KeyMapCreator.EXTRA_ProgressStatus,0);
        String sectorrang = intent.getStringExtra(KeyMapCreator.EXTRA_SectorRange);
        boolean cretingmap = intent.getBooleanExtra(KeyMapCreator.EXTRA_IsCreatingKeyMap,false);
        ArrayList<String> names = intent.getStringArrayListExtra(EXTRA_keynames);
        //ArrayList<File> files = intent.getStringArrayExtra(EXTRA_keyfiles);
        String[] StringFiles = intent.getStringArrayExtra(EXTRA_keyfiles);

        //string[] to file array text
        ArrayList<File> files = new ArrayList<File>();

        //tst
        if (StringFiles.length>0){
            for (int i = 0; i < StringFiles.length; i++) {
                files.set(i, new File(StringFiles[i]));
            }
        }
        else Toast.makeText(this, "Puto no hay archivos we", Toast.LENGTH_SHORT).show();


        // Set them as a public and usable variable
        sLastSector = last;
        sFirstSector = first;
        sProgressStatus = progressStat;
        sSectorRange = sectorrang;
        sIsCreatingKeyMap = cretingmap;
        mKeyDirPath = new File(EXTRA_KEYS_DIR);
        fileNames = names;
        keyFiles = files;
        // Notification for the service
        onShowStealthServiceNotification();
        // Start service

        Toast.makeText(this, "Inicio del test servicio", Toast.LENGTH_LONG).show();
        onCreateKeyMapStealthLoop();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "fin del test servicio", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void createKeyMap(final MCReader reader, final Context context) {
        new Thread(new Runnable() {
            public void run() {
                // Build key map parts and update the progress bar.
                while (sProgressStatus < sLastSector) {
                    sProgressStatus = reader.buildNextKeyMapPart();
                    if (sProgressStatus == -1 || !sIsCreatingKeyMap) {
                        // Error while building next key map part.
                        break;
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        reader.close();
                        if (sIsCreatingKeyMap && sProgressStatus != -1) {
                            keyMapCreated(reader);
                        } else {
                            // Error during key map creation.
                            Common.setKeyMap(null);
                            Common.setKeyMapRange(-1, -1);
                            Toast.makeText(context, R.string.info_key_map_error,
                                    Toast.LENGTH_LONG).show();
                            notification_stealth.setContentTitle("Stealth info : Connection lost");
                        }
                        sIsCreatingKeyMap = false;
                    }
                });
            }
        }).start();
    }

    public void onCreateKeyMapStealthLoop() {
        View view;
        // Create reader.
        MCReader reader = Common.checkForTagAndCreateReader_Stealth(this);
        if (reader == null) {
            return;
        }

        // Set key files.
        File[] keys = keyFiles.toArray(new File[keyFiles.size()]);
        if (!reader.setKeyFile(keys, this)) {
            // Error.
            reader.close();
            return;
        }
        // Get key map range.
        if (sSectorRange.equals(
                getString(R.string.text_sector_range_all))) {
            // Read all.
            sFirstSector = 0;
            sLastSector = reader.getSectorCount()-1;
        } else {
            String[] fromAndTo = sSectorRange.split(" ");
            sFirstSector = Integer.parseInt(fromAndTo[0]);
            sLastSector = Integer.parseInt(fromAndTo[2]);
        }
        // Set map creation range.
        if (!reader.setMappingRange(
                sFirstSector, sLastSector)) {
            // Error.
            Toast.makeText(this,
                    R.string.info_mapping_sector_out_of_range,
                    Toast.LENGTH_LONG).show();
            reader.close();
            return;
        }
        Common.setKeyMapRange(sFirstSector, sLastSector);
        // Init. GUI elements.
        sProgressStatus = -1;
        sIsCreatingKeyMap = true;
        notification_stealth.setContentTitle("Stealth info : READING");

        // Create key Map
        createKeyMap(reader, this);
    }




    private void keyMapCreated(MCReader reader) {
        // LOW: Return key map in intent.
        if (reader.getKeyMap().size() == 0) {
            Common.setKeyMap(null);
            // Error. No valid key found.
            Toast.makeText(this, R.string.info_no_key_found,
                    Toast.LENGTH_LONG).show();
        } else {
            Common.setKeyMap(reader.getKeyMap());
//            Intent intent = new Intent();
//            intent.putExtra(KeyMapCreator.EXTRA_KEY_MAP, mMCReader);
//            setResult(Activity.RESULT_OK, intent);
//            setResult(Activity.RESULT_OK);
//            finish();
        }

    }
    private void onShowStealthServiceNotification (){
        //Notification info
        notification_stealth = new NotificationCompat.Builder(this);
        notification_stealth.setAutoCancel(true);
        //Notification things
        notification_stealth.setSmallIcon(R.drawable.ic_stealth);
        notification_stealth.setContentTitle("Stealth info : Not reading");
        Intent IntentShowStealth = new Intent(this,KeyMapCreator.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,IntentShowStealth,PendingIntent.FLAG_UPDATE_CURRENT);
        notification_stealth.setContentIntent(pendingIntent);
        final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
}
