package com.example.andresvasquez.lab4intents;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String LOG = MainActivity.class.getSimpleName();

    private Button marcarButton;
    private Button geoButton;
    private Button webButton;
    private Button mailButton;
    private Button whatsappButton;
    private Button fotoButton;
    private Button atrasButton;
    private ImageView fotoImageView;


    private String ruta_foto;

    private String numero_tel="60507900";
    private Context context;


    private static int RC_TAKE_PICTURE = 101;
    private static int RC_PERMISSIONS = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Declaramos los componentes
        marcarButton =(Button)findViewById(R.id.marcarButton);
        geoButton =(Button)findViewById(R.id.geoButton);
        webButton =(Button)findViewById(R.id.webButton);
        mailButton =(Button)findViewById(R.id.mailButton);
        fotoButton =(Button)findViewById(R.id.fotoButton);
        atrasButton =(Button)findViewById(R.id.salirButton);
        whatsappButton = (Button)findViewById(R.id.whatsappButton);
        fotoImageView =(ImageView)findViewById(R.id.fotoImageView);

        marcarButton.setText("Marcar a: "+numero_tel);
        marcarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+numero_tel));
                startActivity(a);
            }
        });

        geoButton.setText("ir al Tekhne");
        geoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent a= new Intent(Intent.ACTION_VIEW, Uri.parse("geo:-16.513042,-68.123600?z=19&q=-16.513042,-68.123600"));
                startActivity(a);
            }
        });

        webButton.setText("ir a la página del Tekhne");
        webButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("http://itacad.net/"));
                //Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pendientedemigracion.ucm.es/info/tecnomovil/documentos/android.pdf"));
                startActivity(a);
            }
        });

        mailButton.setText("Enviar email a Andres");
        mailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"andres.vasquez.a@hotmail.com","a.vasquez@jakare.net"});
                i.putExtra(Intent.EXTRA_SUBJECT,"Mensaje de prueba");
                i.putExtra(Intent.EXTRA_TEXT,"Enviado por android");
                startActivity(Intent.createChooser(i, "Seleccione la aplicacion de mailButton."));
            }
        });


        whatsappButton.setText("Whatsapp");
        whatsappButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Curso de Android Yuju!!!");
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Log.e(LOG,"Whatsapp no instalado.");
                }
            }
        });

        fotoButton.setText("Tomar foto");
        fotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pedirPermisoEscritura();
            }
        });

        atrasButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void pedirPermisoEscritura(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                    RC_PERMISSIONS);
            } else {
            lanzarCamara();
        }
    }

    private void lanzarCamara(){
        ruta_foto= Environment.getExternalStorageDirectory() + "/mifoto123.jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri output = FileProvider.getUriForFile(MainActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(ruta_foto));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, RC_TAKE_PICTURE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if(requestCode == RC_PERMISSIONS){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lanzarCamara();
            } else {
                Log.e(LOG,"Permiso denegado");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        File file = new File(ruta_foto);
        if (file.exists())
        {
            //Bitmap bit_ =Bitmap.createScaledBitmap(BitmapFactory.decodeFile(ruta_foto), 400, 300,false);
            fotoImageView.setImageBitmap(BitmapFactory.decodeFile(ruta_foto));
        }
    }
}