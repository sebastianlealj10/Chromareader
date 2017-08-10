package sebasdeveloper.chromareaderv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class PreprocessingActivity extends AppCompatActivity {
    Intent intent;
    //Bindeo para el imageview
    @BindView(R.id.image2) ImageView img2;
    @BindView(R.id.textView2) TextView txt1;
    Bitmap bmp;
    Mat ima;
    Mat imagray;
    double[] dato={0,0,0};
    //Clase donde se crea el layout y se inicializa la libreria ButterKnife
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preprocessing);
        ButterKnife.bind(this);
        ima=imread_mat();
        deletebackground();
       // ima=rotateima(ima);
        imwrite_mat(ima);
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
    public void deletebackground(){
       // Imgproc.cvtColor(ima, imagray, Imgproc.COLOR_RGB2GRAY);
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
                        ima.put(i, j, dato);}
                    }
                 }
            }
        }
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