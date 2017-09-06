package sebasdeveloper.chromareaderv2;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.permission;

public class CoreActivity extends AppCompatActivity {
    //Clase para inicializar la libreria opencv
    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =0 ;

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
    }

    Uri imageUri;
    Intent intent;
    Bitmap bmp;
    Bitmap bmp2;
    Mat ima;
    @BindView(R.id.image)
    ImageView img;
    @BindView(R.id.button2)
    Button btn2;
    final static int captureimage=0;
    final static int loadimage=1;
    public final static String Nombre = "nombre";
    public final static String Lugar= "lugar";
    public final static String Descripcion= "descripcion";
    @Override
    //Clase donde se crea el layout y se inicializa la libreria ButterKnife
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        ButterKnife.bind(this);

    }
    //Opcional
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.capturar) {
            //Creamos el Intent para llamar a la Camara
            Intent cameraIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //Creamos una carpeta en la memeria del terminal
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "sebas");
            imagesFolder.mkdirs();
            //añadimos el nombre de la imagen
            File image = new File(imagesFolder, "cromaoriginal.jpg");
            Uri uriSavedImage = Uri.fromFile(image);
            //Le decimos al Intent que queremos grabar la imagen
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriSavedImage);
            //Lanzamos la aplicacion de la camara con retorno (forResult)

            startActivityForResult(cameraIntent, captureimage);
            return true;
        }
        if (id == R.id.cargar) {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, loadimage);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Clase donde se muestra la foto en el imageview provenga de la camara o de la galeria
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Condicion para la camara
        if(resultCode == Activity.RESULT_OK  &&  requestCode == captureimage)
        {
            //funciones del opencv
            ima =imread_mat();
            //no rotar si es en movil
            ima=rotateima(ima);
            //
            imwrite_mat(ima);
            showima();
            btn2.setEnabled(true);
        }
        //Condicion para la galeria
        if(resultCode == Activity.RESULT_OK  &&  requestCode == loadimage)
        {
            imageUri = data.getData();
            //se contruye la imagen en bitmap a partir del uri de la imagen
            try {
                bmp2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //se graba el bitmap en un directorio externo
            try {
                FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory()+
                        "/sebas/"+"cromaoriginal.jpg");
                bmp2.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            img.setImageBitmap(bmp2);

        }
    }
    // Boton donde se lanza la nueva actividad
    @OnClick(R.id.button2)
    public void processingphoto(View view)
    {
        Intent intent = new Intent(this, PreprocessingActivity.class);
        EditText editText1 = (EditText) findViewById(R.id.editText1);
        String nombre = editText1.getText().toString();
        intent.putExtra(Nombre, nombre);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String lugar = editText2.getText().toString();
        intent.putExtra(Lugar, lugar);
        EditText editText3 = (EditText) findViewById(R.id.editText3);
        String descripcion = editText3.getText().toString();
        intent.putExtra(Descripcion, descripcion);
        startActivity(intent);
    }
    public Mat imread_mat(){
        Mat imagen;
        //Se lee la foto desde la ubicacion donde fue almacenada en la memoria interna
        imagen = Imgcodecs.imread(Environment.getExternalStorageDirectory()+
                "/sebas/"+"foto1.jpg");
        return imagen;
    }
    public void imwrite_mat(Mat imagen){
        Imgcodecs.imwrite(Environment.getExternalStorageDirectory()+
                "/sebas/"+"foto1.jpg",imagen);
    }
    public void showima(){
        bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+
                "/sebas/"+"foto1.jpg");
        img.setImageBitmap(bmp);
    }
    public Mat rotateima(Mat imagen){
        Core.transpose(imagen,imagen);
        return imagen;
    }
}
