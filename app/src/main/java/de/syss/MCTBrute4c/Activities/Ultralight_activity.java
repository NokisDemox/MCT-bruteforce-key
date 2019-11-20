package de.syss.MCTBrute4c.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.syss.MCTBrute4c.Common;
import de.syss.MCTBrute4c.Emulator.MainActivity;
import de.syss.MCTBrute4c.R;

public class Ultralight_activity extends Activity {

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    static Tag mytag;
    Context ctx;
    EditText pg0;
    EditText pg1;
    EditText pg2;
    EditText pg3;
    EditText pg4;
    EditText pg5;
    EditText pg6;
    EditText pg7;
    EditText pg8;
    EditText pg9;
    EditText pg10;
    EditText pg11;
    EditText pg12;
    EditText pg13;
    EditText pg14;
    EditText pg15;
    private String FILE_NAME_ULTRA_TAG = "";
    private final static int FILE_CHOOSER_KEY_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultralight_activity);
        ctx = this;
        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
        //Declaring TextView
        final EditText page0 = (EditText) findViewById(R.id.editText1);
        final EditText page1 = (EditText) findViewById(R.id.editText2);
        final EditText page2 = (EditText) findViewById(R.id.editText3);
        final EditText page3 = (EditText) findViewById(R.id.editText4);
        final EditText page4 = (EditText) findViewById(R.id.editText5);
        final EditText page5 = (EditText) findViewById(R.id.editText6);
        final EditText page6 = (EditText) findViewById(R.id.editText7);
        final EditText page7 = (EditText) findViewById(R.id.editText8);
        final EditText page8 = (EditText) findViewById(R.id.editText9);
        final EditText page9 = (EditText) findViewById(R.id.editText10);
        final EditText page10 = (EditText) findViewById(R.id.editText11);
        final EditText page11 = (EditText) findViewById(R.id.editText12);
        final EditText page12 = (EditText) findViewById(R.id.editText13);
        final EditText page13 = (EditText) findViewById(R.id.editText14);
        final EditText page14 = (EditText) findViewById(R.id.editText15);
        final EditText page15 = (EditText) findViewById(R.id.editText16);

        pg0=page0;
        pg1=page1;
        pg2=page2;
        pg3=page3;
        pg4=page4;
        pg5=page5;
        pg6=page6;
        pg7=page7;
        pg8=page8;
        pg9=page9;
        pg10=page10;
        pg11=page11;
        pg12=page12;
        pg13=page13;
        pg14=page14;
        pg15=page15;

        // " page 5 "
        Button btnEdt = ( Button ) findViewById(R.id.button8);
        Button btnWrt = ( Button ) findViewById(R.id.button11);
        Button btnSave = ( Button ) findViewById(R.id.button1);



        final CharSequence[] cs = new CharSequence[6];
        cs[0] = "Convert hex to dec";
        cs[1] = "Convert bin to dec";
        cs[2] = "Convert dec to hex";
        cs[3] = "Convert bin to hex";
        cs[4] = "Convert hex to bin";
        cs[5] = "Convert dec to bin";
        final TextView text0 = (TextView) findViewById(R.id.textView1);
        final TextView text1 = (TextView) findViewById(R.id.TextView2);
        final TextView text2 = (TextView) findViewById(R.id.TextView3);
        final TextView text3 = (TextView) findViewById(R.id.TextView4);
        final TextView text4 = (TextView) findViewById(R.id.TextView5);
        final TextView text5 = (TextView) findViewById(R.id.TextView6);
        final TextView text6 = (TextView) findViewById(R.id.TextView7);
        final TextView text7 = (TextView) findViewById(R.id.TextView8);
        final TextView text8 = (TextView) findViewById(R.id.TextView9);
        final TextView text9 = (TextView) findViewById(R.id.TextView10);
        final TextView text10 = (TextView) findViewById(R.id.TextView11);
        final TextView text11 = (TextView) findViewById(R.id.TextView12);
        final TextView text12 = (TextView) findViewById(R.id.TextView13);
        final TextView text13 = (TextView) findViewById(R.id.TextView14);
        final TextView text14 = (TextView) findViewById(R.id.TextView15);
        final TextView text15 = (TextView) findViewById(R.id.TextView16);


        text0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page0.setText(HextoDec(page0.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page0.setText(BintoDec(page0.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page0.setText(DectoHex(page0.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page0.setText(BintoHex(page0.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page0.setText(HextoBin(page0.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page0.setText(DectoBin(page0.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page1.setText(HextoDec(page1.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page1.setText(BintoDec(page1.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page1.setText(DectoHex(page1.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page1.setText(BintoHex(page1.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page1.setText(HextoBin(page1.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page1.setText(DectoBin(page1.getText().toString()));
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page2.setText(HextoDec(page2.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page2.setText(BintoDec(page2.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page2.setText(DectoHex(page2.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page2.setText(BintoHex(page2.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page2.setText(HextoBin(page2.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page2.setText(DectoBin(page2.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page3.setText(HextoDec(page3.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page3.setText(BintoDec(page3.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page3.setText(DectoHex(page3.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page3.setText(BintoHex(page3.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page3.setText(HextoBin(page3.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page3.setText(DectoBin(page3.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page4.setText(HextoDec(page4.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page4.setText(BintoDec(page4.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page4.setText(DectoHex(page4.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page4.setText(BintoHex(page4.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page4.setText(HextoBin(page4.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page4.setText(DectoBin(page4.getText().toString()));
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page5.setText(HextoDec(page5.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page5.setText(BintoDec(page5.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page5.setText(DectoHex(page5.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page5.setText(BintoHex(page5.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page5.setText(HextoBin(page5.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page5.setText(DectoBin(page5.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page6.setText(HextoDec(page6.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page6.setText(BintoDec(page6.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page6.setText(DectoHex(page6.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page6.setText(BintoHex(page6.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page6.setText(HextoBin(page6.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page6.setText(DectoBin(page6.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page7.setText(HextoDec(page7.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page7.setText(BintoDec(page7.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page7.setText(DectoHex(page7.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page7.setText(BintoHex(page7.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page7.setText(HextoBin(page7.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page7.setText(DectoBin(page7.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page8.setText(HextoDec(page8.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page8.setText(BintoDec(page8.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page8.setText(DectoHex(page8.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page8.setText(BintoHex(page8.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page8.setText(HextoBin(page8.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page8.setText(DectoBin(page8.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page9.setText(HextoDec(page9.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page9.setText(BintoDec(page9.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page9.setText(DectoHex(page9.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page9.setText(BintoHex(page9.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page9.setText(HextoBin(page9.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page9.setText(DectoBin(page9.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page10.setText(HextoDec(page10.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page10.setText(BintoDec(page10.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page10.setText(DectoHex(page10.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page10.setText(BintoHex(page10.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page10.setText(HextoBin(page10.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page10.setText(DectoBin(page10.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page11.setText(HextoDec(page11.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page11.setText(BintoDec(page11.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page11.setText(DectoHex(page11.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page11.setText(BintoHex(page11.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page11.setText(HextoBin(page11.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page11.setText(DectoBin(page11.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page12.setText(HextoDec(page12.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page12.setText(BintoDec(page12.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page12.setText(DectoHex(page12.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page12.setText(BintoHex(page12.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page12.setText(HextoBin(page12.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page12.setText(DectoBin(page12.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page13.setText(HextoDec(page13.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page13.setText(BintoDec(page13.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page13.setText(DectoHex(page13.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page13.setText(BintoHex(page13.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page13.setText(HextoBin(page13.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page13.setText(DectoBin(page13.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page14.setText(HextoDec(page14.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page14.setText(BintoDec(page14.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page14.setText(DectoHex(page14.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page14.setText(BintoHex(page14.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page14.setText(HextoBin(page14.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page14.setText(DectoBin(page14.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });
        text15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) ctx;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Convertion");
                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity activity = (MainActivity) ctx;

                        try {
                            if(whichButton==0) {
                                page15.setText(HextoDec(page15.getText().toString()).toUpperCase());
                            }
                            if(whichButton==1) {
                                page15.setText(BintoDec(page15.getText().toString()).toUpperCase());
                            }
                            if(whichButton==2) {
                                page15.setText(DectoHex(page15.getText().toString()).toUpperCase());
                            }
                            if(whichButton==3) {
                                page15.setText(BintoHex(page15.getText().toString()).toUpperCase());
                            }
                            if(whichButton==4) {
                                page15.setText(HextoBin(page15.getText().toString()).toUpperCase());
                            }
                            if(whichButton==5) {
                                page15.setText(DectoBin(page15.getText().toString()).toUpperCase());
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity, "There was an during the operation..", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    };
                });
                builder.show();

            }
        });



        btnWrt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //Preparing variables to perform UID check
                    int uid0 = Integer.parseInt(page0.getText().toString().substring(0, 2), 16);
                    int uid1 = Integer.parseInt(page0.getText().toString().substring(2, 4), 16);
                    int uid2 = Integer.parseInt(page0.getText().toString().substring(4, 6), 16);
                    int bcc1 = Integer.parseInt(page0.getText().toString().substring(6, 8), 16);
                    int uid3 = Integer.parseInt(page1.getText().toString().substring(0, 2), 16);
                    int uid4 = Integer.parseInt(page1.getText().toString().substring(2, 4), 16);
                    int uid5 = Integer.parseInt(page1.getText().toString().substring(4, 6), 16);
                    int uid6 = Integer.parseInt(page1.getText().toString().substring(6, 8), 16);
                    int bcc2 = Integer.parseInt(page2.getText().toString().substring(0, 2), 16);

                    //Performing XOR operation to check if UID's checksums are correct
                    int bcc1Check = 0x88 ^ uid0 ^ uid1 ^ uid2;
                    int bcc2Check = uid3 ^ uid4 ^ uid5 ^ uid6;
                    final boolean continueBcc[] = {true};

                    //If not prompt alert to user
                    if(bcc1Check != bcc1) {
                        new AlertDialog.Builder(ctx)
                                .setTitle("Warning")
                                .setMessage("UID checksum BCC0 seems wrong. Correct value is: "+Integer.toHexString(bcc1Check).toUpperCase()+"\nDo you want to continue with your value?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        continueBcc[0] = true;
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        continueBcc[0] = false;
                                    }
                                })
                                .show();
                    }
                    if(bcc2Check != bcc2) {
                        new AlertDialog.Builder(ctx)
                                .setTitle("Warning")
                                .setMessage("UID checksum BCC1 seems wrong. Correct value is: "+Integer.toHexString(bcc2Check).toUpperCase()+"\nDo you want to continue with your value?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        continueBcc[0] = true;
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        continueBcc[0] = false;
                                    }
                                })
                                .show();
                    }
                    if(continueBcc[0] == true) {
                        //Write pages to the ticket
                        MifareUltralight ultralight = MifareUltralight.get(mytag);
                        ultralight.connect();
                        ultralight.writePage(0, hexStringToByteArray(page0.getText().toString()));
                        ultralight.writePage(1, hexStringToByteArray(page1.getText().toString()));
                        ultralight.writePage(2, hexStringToByteArray(page2.getText().toString()));
                        ultralight.writePage(3, hexStringToByteArray(page3.getText().toString()));
                        ultralight.writePage(4, hexStringToByteArray(page4.getText().toString()));
                        ultralight.writePage(5, hexStringToByteArray(page5.getText().toString()));
                        ultralight.writePage(6, hexStringToByteArray(page6.getText().toString()));
                        ultralight.writePage(7, hexStringToByteArray(page7.getText().toString()));
                        ultralight.writePage(8, hexStringToByteArray(page8.getText().toString()));
                        ultralight.writePage(9, hexStringToByteArray(page9.getText().toString()));
                        ultralight.writePage(10, hexStringToByteArray(page10.getText().toString()));
                        ultralight.writePage(11, hexStringToByteArray(page11.getText().toString()));
                        ultralight.writePage(12, hexStringToByteArray(page12.getText().toString()));
                        ultralight.writePage(13, hexStringToByteArray(page13.getText().toString()));
                        ultralight.writePage(14, hexStringToByteArray(page14.getText().toString()));
                        ultralight.writePage(15, hexStringToByteArray(page15.getText().toString()));
                        ultralight.close();
                    }
                } catch(Exception e){
                    Toast.makeText(ctx, "Error writing to Tag..", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        //Button save dump
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create an AlertDialog to get filename for the dump
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                alert.setTitle("Dump Filename");
                alert.setMessage("Insert filename for dump...");
                final EditText input = new EditText(ctx);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        FILE_NAME_ULTRA_TAG=value+".mut";
                        File file = new File(Environment.getExternalStoragePublicDirectory(Common.HOME_DIR) + "/" + Common.MUT_DIR,FILE_NAME_ULTRA_TAG);
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(page0.getText().toString().concat("\n").getBytes());
                            fos.write(page1.getText().toString().concat("\n").getBytes());
                            fos.write(page2.getText().toString().concat("\n").getBytes());
                            fos.write(page3.getText().toString().concat("\n").getBytes());
                            fos.write(page4.getText().toString().concat("\n").getBytes());
                            fos.write(page5.getText().toString().concat("\n").getBytes());
                            fos.write(page6.getText().toString().concat("\n").getBytes());
                            fos.write(page7.getText().toString().concat("\n").getBytes());
                            fos.write(page8.getText().toString().concat("\n").getBytes());
                            fos.write(page9.getText().toString().concat("\n").getBytes());
                            fos.write(page10.getText().toString().concat("\n").getBytes());
                            fos.write(page11.getText().toString().concat("\n").getBytes());
                            fos.write(page12.getText().toString().concat("\n").getBytes());
                            fos.write(page13.getText().toString().concat("\n").getBytes());
                            fos.write(page14.getText().toString().concat("\n").getBytes());
                            fos.write(page15.getText().toString().concat("\n").getBytes());
                            fos.close();
                            Toast.makeText(ctx,"Dump saved to: " + FILE_NAME_ULTRA_TAG, Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(ctx, "file not found", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(ctx, "There was an error during the operation..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(ctx, "Insert dump name..", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }

        });
    }

    //Method to convert an Hex String to a Byte Array.
    public static byte[] hexStringToByteArray(String s) {

        int len = s.length();
        byte[] data = new byte[len/2];
        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    //Method to convert a Byte Array to an Hex String.
    public static String byteArrayToHexString(byte[] bytes) {

        char[] hexChars = new char[bytes.length*2];
        int k;
        for(int i=0; i < bytes.length; i++) {
            k = bytes[i] & 0xFF;
            hexChars[i*2] = hexArray[k>>>4];
            hexChars[i*2 + 1] = hexArray[k & 0x0F];
        }
        return new String(hexChars);
    }


    @Override
    protected void onNewIntent(Intent intent){
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techlist = mytag.getTechList();
            if (techlist.length != 0){
                boolean validtech=false;
                int i=0;
                while(i<techlist.length){
                    if(techlist[i].equals("android.nfc.tech.MifareUltralight")){
                        validtech=true;
                        break;
                    }
                    i++;
                }
                if (validtech){
                    Toast.makeText(ctx, "Tag found", Toast.LENGTH_LONG ).show();
                    onReadUltralightTag();
                }
                else {
                    Toast.makeText(ctx, "Tag found, but doesn't seems to be a Mifare UL\nBroken UID?", Toast.LENGTH_LONG ).show();
                }
            }
            else {
                Toast.makeText(ctx, "ERROR", Toast.LENGTH_LONG ).show();
            }

        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){

        super.onResume();
        WriteModeOn();
        Intent intent = getIntent();
        if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())){
            mytag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techlist = mytag.getTechList();
            if (techlist.length != 0){
                boolean validtech=false;
                int i=0;
                while(i<techlist.length){
                    if(techlist[i].equals("android.nfc.tech.MifareUltralight")){
                        validtech=true;
                        break;
                    }
                    i++;
                }
                if (validtech){
                    Toast.makeText(ctx, "Tag found", Toast.LENGTH_LONG ).show();
                }
                else {
                    Toast.makeText(ctx, "Tag found, but doesn't seems to be a Mifare UL", Toast.LENGTH_LONG ).show();
                }
            }
            else {
                Toast.makeText(ctx, "ERROR", Toast.LENGTH_LONG ).show();
            }
            intent = intent.setAction(null);
        }
    }

    private void WriteModeOn(){

        writeMode = true;
        adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff(){

        writeMode = false;
        adapter.disableForegroundDispatch(this);
    }
    //Convert Hex to Dec
    private String HextoDec(String page) {
        String returnValue = String.valueOf(Integer.parseInt(page, 16));
        return returnValue;
    }
    //Convert Bin to Dec
    private String BintoDec(String page) {
        String returnValue = String.valueOf(Integer.parseInt(page, 2));
        return returnValue;
    }
    //Convert Dec to Hex
    private String DectoHex(String page) {
        String returnValue = Integer.toHexString(Integer.valueOf(page,10));
        return returnValue;
    }
    //Convert Bin to Hex
    private String BintoHex(String page) {
        String returnValue = Integer.toHexString(Integer.valueOf(page,2));
        return returnValue;
    }

    private String DectoBin(String page) {
        String returnValue = Integer.toBinaryString(Integer.valueOf(page,10));
        return returnValue;
    }
    private String HextoBin(String page) {
        String returnValue = Integer.toBinaryString(Integer.valueOf(page,16));
        return returnValue;
    }
    //Returns a list of saved dumps.
    private ArrayList<String> getDumpList() {

        String[] savedDumps = {""};
        ArrayList<String> dumps = new ArrayList<String>();
        savedDumps = getApplicationContext().fileList();
        for(int i=0; i<savedDumps.length; i++) {
            try {
                if(savedDumps[i].substring(savedDumps[i].lastIndexOf(".")).equals(".mfd")) {
                    dumps.add(savedDumps[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dumps;
    }

    public void onReadUltralightTag(){

        try {
            MifareUltralight ultralight = MifareUltralight.get(mytag);
            ultralight.connect();
            pg0.setText(byteArrayToHexString(ultralight.readPages(0)).substring(0, 8));
            pg1.setText(byteArrayToHexString(ultralight.readPages(1)).substring(0, 8));
            pg2.setText(byteArrayToHexString(ultralight.readPages(2)).substring(0, 8));
            pg3.setText(byteArrayToHexString(ultralight.readPages(3)).substring(0, 8));
            pg4.setText(byteArrayToHexString(ultralight.readPages(4)).substring(0, 8));
            pg5.setText(byteArrayToHexString(ultralight.readPages(5)).substring(0, 8));
            pg6.setText(byteArrayToHexString(ultralight.readPages(6)).substring(0, 8));
            pg7.setText(byteArrayToHexString(ultralight.readPages(7)).substring(0, 8));
            pg8.setText(byteArrayToHexString(ultralight.readPages(8)).substring(0, 8));
            pg9.setText(byteArrayToHexString(ultralight.readPages(9)).substring(0, 8));
            pg10.setText(byteArrayToHexString(ultralight.readPages(10)).substring(0, 8));
            pg11.setText(byteArrayToHexString(ultralight.readPages(11)).substring(0, 8));
            pg12.setText(byteArrayToHexString(ultralight.readPages(12)).substring(0, 8));
            pg13.setText(byteArrayToHexString(ultralight.readPages(13)).substring(0, 8));
            pg14.setText(byteArrayToHexString(ultralight.readPages(14)).substring(0, 8));
            pg15.setText(byteArrayToHexString(ultralight.readPages(15)).substring(0, 8));
            ultralight.close();
        } catch(Exception e) {
            Toast.makeText(ctx, "Error reading Tag..", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.emv_functions, menu);
        return true;
    }
    public void onShowUltraInfo() {
        android.app.AlertDialog.Builder ventana_emv = new android.app.AlertDialog.Builder(this);
        ventana_emv.setTitle(R.string.ultralight_info_title);
        ventana_emv.setMessage(R.string.ultralight_info);
        ventana_emv.setIcon(R.mipmap.ic_launcher);
        ventana_emv.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Ultralight_activity.this, R.string.dialog_closed, Toast.LENGTH_SHORT).show();
            }
        });
        ventana_emv.show();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.menuEMVShowInfo:
                onShowUltraInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onOpenUltraEditor(View view) {
        if (Common.isExternalStorageWritableErrorToast(this)) {
            Intent intent = new Intent(this, FileChooser.class);
            intent.putExtra(FileChooser.EXTRA_DIR,
                    Environment.getExternalStoragePublicDirectory(
                            Common.HOME_DIR) + "/" + Common.MUT_DIR);
            intent.putExtra(FileChooser.EXTRA_TITLE,
                    getString(R.string.ultralight_saved_title));
            intent.putExtra(FileChooser.EXTRA_BUTTON_TEXT,
                    getString(R.string.ultralight_saved_btn));
            intent.putExtra(FileChooser.EXTRA_ENABLE_NEW_FILE, false);
            intent.putExtra(FileChooser.EXTRA_ENABLE_DELETE_FILE, true);
            startActivityForResult(intent, FILE_CHOOSER_KEY_FILE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
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

}
