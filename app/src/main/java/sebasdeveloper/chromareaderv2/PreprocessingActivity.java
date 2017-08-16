package sebasdeveloper.chromareaderv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class PreprocessingActivity extends AppCompatActivity {
    Intent intent;
    //Bindeo para el imageview
    @BindView(R.id.image2) ImageView img2;
    @BindView(R.id.textView2) TextView txt1;
    Bitmap bmp;
    Mat ima;
    Mat imagray;
    double[] datocolor={0,0,0};
    double[] datogray={0};
    //Clase donde se crea el layout y se inicializa la libreria ButterKnife
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preprocessing);
        ButterKnife.bind(this);
        ima=imread_mat();
        imagray=deletebackground();
       // ima=rotateima(ima);
       // imwrite_mat(ima);
        imwrite_mat(imagray);
        showima();
    }
    @OnClick(R.id.button8)
    public void processingmineral(View view)
    {
        intent = new Intent(this, MineralActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button9)
    public void processingorganic(View view)
    {
        intent = new Intent(this, OrganicActivity.class);
        startActivity(intent);
    }
    public Mat deletebackground(){
/*/
        int rows=ima.rows();
        int cols=ima.cols();
        int ch = ima.channels();

     //   double[] pix=ima.get(100,100);
      //  txt1.setText(Double.toString(pix[0])+" "+Double.toString(pix[1])+" "+Double.toString(pix[2]));

        {
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                 {
                    for (int k=0; k<ch; k++) {
                    double[] pix = ima.get(i, j);
                        if (pix[0] < 200 || pix[1] < 200 || pix[2] < 200 ) {
                        ima.put(i, j, datocolor);}
                    }
                 }
            }
        }

/*/
        Mat imaGray1 = new Mat();
        Imgproc.cvtColor(ima, imaGray1, Imgproc.COLOR_BGR2GRAY);

        int rows=imaGray1.rows();
        int cols=imaGray1.cols();
            for (int i=0; i<rows; i++) {
                for (int j=0; j<cols; j++) {
                    double[] pix = imaGray1.get(i, j);
                    if (pix[0] < 200) {
                        imaGray1.put(i, j, datogray);
                    }
                }
            }
        return imaGray1;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.preprocessing,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.capturar) {

        }
        if (id == R.id.cargar) {

        }

        return super.onOptionsItemSelected(item);
    }

    public Mat rotateima(Mat imagen){
        Core.transpose(imagen,imagen);
        return imagen;
    }

    public Mat imread_mat(){
        Mat imagen;
        //Se lee la foto desde la ubicacion donde fue almacenada en la memoria interna
        imagen = Imgcodecs.imread(Environment.getExternalStorageDirectory()+
                "/sebas/"+"cromaoriginal.jpg");
        return imagen;
    }
    public void imwrite_mat(Mat imagen){
        Imgcodecs.imwrite(Environment.getExternalStorageDirectory()+
                "/sebas/"+"cromapreprocesado.jpg",imagen);
    }
    public void showima(){
        bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+
                "/sebas/"+"cromapreprocesado.jpg");
        img2.setImageBitmap(bmp);
    }
}