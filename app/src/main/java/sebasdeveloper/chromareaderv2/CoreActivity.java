package sebasdeveloper.chromareaderv2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.jeffersonrojas.materialpermissions.library.PermissionManager;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Math.abs;

public class CoreActivity extends AppCompatActivity {
    //Clase para inicializar la libreria opencv
    private static final String TAG = "MainActivity";
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
    }
    Uri imageUri;
    Bitmap bmp;
    Bitmap bmp2;
    Mat ima;
    @BindView(R.id.image)
    ImageView img;
    @BindView(R.id.button2)
    Button btn2;
    final static int captureimage = 0;
    final static int loadimage = 1;
    public final static String Nombre = "nombre";
    public final static String Lugar= "lugar";
    public final static String Descripcion= "descripcion";
    public final static String blue="azul";
    public final static String green="verde";
    public final static String red="red";
    int bt;
    int gt;
    int rt;
    PermissionManager permissionManager;
    @Override
    //Clase donde se crea el layout y se inicializa la libreria ButterKnife
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //llamado a la biblioteca de permisos
        permissionManager = new PermissionManager(this);
        permissionManager.addPermission(Manifest.permission.CAMERA);
        permissionManager.addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    //Opcional
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//metodo sobreescrito para la libreria de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionManager.permissionsResult(requestCode, grantResults)) {
            tomaFoto();
        }
    }
//antes de llamar las funciones de carga o captura de imagenes se piden los permisos
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.capturar) {
                if (permissionManager.havePermission()) {
                    tomaFoto();
                } else {
                    permissionManager.requestPermissions();
                }
            return true;
        }
        if (id == R.id.cargar) {
            if (permissionManager.havePermission()) {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, loadimage);
            } else {
                permissionManager.requestPermissions();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void tomaFoto() {
        //Creamos el Intent para llamar a la Camara
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
    }

    //Clase donde se muestra la foto en el imageview provenga de la camara o de la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Condicion para la camara
        if (resultCode == Activity.RESULT_OK && requestCode == captureimage) {
            //funciones del opencv
            ima = imread_mat();
            //no rotar si es en movil
            ima = rotateima(ima);
            //funcion para eliminar el fondo
            deletebackground();
            imwrite_mat(ima);
            showima();
            btn2.setEnabled(true);
        }
        //Condicion para la galeria
        if (resultCode == Activity.RESULT_OK && requestCode == loadimage) {
            imageUri = data.getData();
            //se contruye la imagen en bitmap a partir del uri de la imagen
            try {
                bmp2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //se graba el bitmap en un directorio externo
            try {
                FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() +
                        "/sebas/" + "cromaoriginal.jpg");
                bmp2.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //funciones del opencv
            ima = imread_mat();
            //funcion para eliminar el fondo
            deletebackground();
            imwrite_mat(ima);
            showima();
            btn2.setEnabled(true);
        }
    }
    // Boton donde se lanza la nueva actividad
    @OnClick(R.id.button2)
    public void processingphoto(View view) {
        Intent intent = new Intent(this, PreprocessingActivity.class);
        EditText editText1 = (EditText) findViewById(R.id.editText1);
        String nombre = editText1.getText().toString();
        intent.putExtra(Nombre, nombre);
        intent.putExtra(blue,bt);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String lugar = editText2.getText().toString();
        intent.putExtra(Lugar, lugar);
        intent.putExtra(green,gt);
        EditText editText3 = (EditText) findViewById(R.id.editText3);
        String descripcion = editText3.getText().toString();
        intent.putExtra(Descripcion, descripcion);
        intent.putExtra(red,rt);
        startActivity(intent);
    }

    public Mat imread_mat() {
        Mat imagen;
        //Se lee la foto desde la ubicacion donde fue almacenada en la memoria interna
        imagen = Imgcodecs.imread(Environment.getExternalStorageDirectory() +
                "/sebas/" + "cromaoriginal.jpg");
        return imagen;
    }

    public void imwrite_mat(Mat imagen) {
        Imgcodecs.imwrite(Environment.getExternalStorageDirectory() +
                "/sebas/" + "cromasinfondo.jpg", imagen);
    }

    public void showima() {
        bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +
                "/sebas/" + "cromasinfondo.jpg");
        img.setImageBitmap(bmp);
    }

    public Mat rotateima(Mat imagen) {
        Core.transpose(imagen, imagen);
        return imagen;
    }
    //funcion encargada de eliminar el borde blanco del papel en el croma
    public void deletebackground(){
        int rows=ima.rows();
        int cols=ima.cols();
        double cromab=0.0;
        double cromag=0.0;
        double cromar=0.0;
        double numcapa1=0.0;
        int ch = ima.channels();
        double[] datocolor={0,0,0};
        {
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                {
                    double[] pix = ima.get(i, j);
                    //el ruido de la imagen corresponde a pixeles muy cercanos entre ellos mismos
                    if (abs(pix[0] -  pix[1])  < 10 ) {
                        ima.put(i, j, datocolor);

                    }
                    else{
                        cromab=pix[0]+cromab;
                        cromag=pix[1]+cromag;
                        cromar=pix[2]+cromar;
                        numcapa1=numcapa1+1;
                    }
                }
            }
        }
        bt= (int) (cromab/numcapa1);
        gt= (int) (cromag/numcapa1);
        rt= (int) (cromar/numcapa1);
        Log.d("rgb", String.valueOf(rt+" "+gt+" "+bt));
    }
}