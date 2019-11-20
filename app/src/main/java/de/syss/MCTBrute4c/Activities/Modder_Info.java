package de.syss.MCTBrute4c.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import de.syss.MCTBrute4c.R;

public class Modder_Info extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modder__info);
    }

    public void browser1 (View view){
        Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://nokisdemox.blogspot.com/"));
        startActivity(browserIntent);
    }
    public void browser2 (View view){
        Intent browserIntent2=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/NokisDemoxDonations"));
        startActivity(browserIntent2);
    }
    public void browser3 (View view){
        Intent browserIntent3=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCeOmaqADC9SOa7WmXKbWc1A?sub_confirmation=1"));
        startActivity(browserIntent3);
    }
    public void browser4 (View view){
        Intent browserIntent4=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=au.id.micolous.farebot"));
        startActivity(browserIntent4);
    }
    public void browser5 (View view){
        Intent browserIntent5=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=de.syss.MifareClassicTool"));
        startActivity(browserIntent5);
    }
    public void browser6 (View view){
        Intent browserIntent6=new Intent(Intent.ACTION_VIEW, Uri.parse("https://nokisresolver-app.uptodown.com/android"));
        startActivity(browserIntent6);
    }
    public void browser7 (View view){
        Intent browserIntent7=new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MatusKysel/EMVemulator"));
        startActivity(browserIntent7);
    }
    public void browser8 (View view){
        Intent browserIntent8=new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/0140454/nfc-uid-emulator"));
        startActivity(browserIntent8);
    }
    public void browser9 (View view){
        Intent browserIntent9=new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/securenetwork/NFCulT"));
        startActivity(browserIntent9);
    }
    public void browser10 (View view){
        Intent browserIntent10=new Intent(Intent.ACTION_VIEW, Uri.parse("https://insta-inspector.uptodown.com/android/descargar"));
        startActivity(browserIntent10);
    }
}
