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
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.sql.Blob;

import static java.lang.Math.abs;
import static org.opencv.core.Core.CMP_GT;
import static org.opencv.core.CvType.CV_16S;
import static org.opencv.core.CvType.CV_32S;
import static org.opencv.core.CvType.CV_64F;

public class PreprocessingActivity extends AppCompatActivity {
    Intent intent;
    //Bindeo para el imageview
    @BindView(R.id.image2) ImageView img2;
    @BindView(R.id.textView) TextView txt;
    Bitmap bmp;
    Mat ima;
    Mat imasinfondo;
    Mat capa2;
    Mat capa1;

    //Clase donde se crea el layout y se inicializa la libreria ButterKnife
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preprocessing);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String nombre = intent.getStringExtra(CoreActivity.Nombre);
        String lugar = intent.getStringExtra(CoreActivity.Lugar);
        String descripcion = intent.getStringExtra(CoreActivity.Descripcion);
        txt.setText("Nombre:"+nombre+"\n"+"Lugar:"+lugar+"\n"+"Descripción:"+descripcion);
        ima=imread_mat();
        imasinfondo=deletebackground();
       capa2=segcapa2();
        // ima=rotateima(ima);
        imwrite_mat(capa2);
        showima();
    }
    public Mat deletebackground(){
        Mat temp = ima;
        int rows=ima.rows();
        int cols=ima.cols();
        int ch = ima.channels();
        double[] datocolor={0,0,0};
        {
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                {
                    double[] pix = ima.get(i, j);
                    if (abs(pix[0] -  pix[1])  < 10 ) {
                        temp.put(i, j, datocolor);}
                }
            }
        }

        return temp;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.preprocessing,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.diagnostico) {
            intent = new Intent(this, MineralActivity.class);
            startActivity(intent);
        }
        if (id == R.id.procesamiento) {
            intent = new Intent(this, OrganicActivity.class);
            startActivity(intent);
        }
        if (id == R.id.derechos) {

        }
        return super.onOptionsItemSelected(item);
    }



    public Mat segcapa2()
    {   Mat comp= ima;
        Mat temp= null;
        int rows=imasinfondo.rows();
        int cols=imasinfondo.cols();
        Core.extractChannel(imasinfondo,comp,0);
        temp=comp;
        double[] capab={255};
        double[] capan={0};

        {
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                {
                    double[] pix = comp.get(i, j);
                    if (pix[0]>45 && pix[0]<95  ) {
                        temp.put(i, j, capab);
                    }
                    else
                        temp.put(i, j, capan);
                }
            }
        }
        Mat temp2=temp;
        Mat temp3=temp;
        Mat temp4=temp;
     int nLabels=Imgproc.connectedComponents(temp,temp2,4,Imgproc.CC_STAT_AREA);
     //   Log.d("areas", String.valueOf(nLabels));
       // Imgproc.threshold(temp,temp2,0,255,0);
     //   Log.d("regiones", String.valueOf(nLabels));
       double areas= Imgproc.contourArea(temp2);
       Log.d("areas", String.valueOf(areas));

        return temp2;

    }
    public Mat segcapa3()
    {   Mat comp= ima;
        Mat temp= null;
        int rows=imasinfondo.rows();
        int cols=imasinfondo.cols();
        Core.extractChannel(imasinfondo,comp,2);
        temp=comp;
        double[] capab={255};
        double[] capan={0};

        {
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                {
                    double[] pix = comp.get(i, j);
                    if (pix[0]>160  ) {
                        temp.put(i, j, capab);
                    }
                    else
                        temp.put(i, j, capan);
                }
            }
        }

        return temp;
    }

    public Mat segcapa1()
    {   Mat comp= ima;
        Mat temp= null;
        int rows=imasinfondo.rows();
        int cols=imasinfondo.cols();
        Core.extractChannel(imasinfondo,comp,2);
        temp=comp;
        double[] capab={255};
        double[] capan={0};

        {
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                {
                    double[] pix = comp.get(i, j);
                    if (pix[0]>110 && pix[0]<160 ) {
                        temp.put(i, j, capab);
                    }
                    else
                        temp.put(i, j, capan);
                }
            }
        }

        return temp;
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
