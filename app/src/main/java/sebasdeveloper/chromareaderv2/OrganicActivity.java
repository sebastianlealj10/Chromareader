package sebasdeveloper.chromareaderv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static sebasdeveloper.chromareaderv2.PreprocessingActivity.Areas1;
import static sebasdeveloper.chromareaderv2.PreprocessingActivity.Areas2;

public class OrganicActivity extends AppCompatActivity {
    Mat ima;
    Mat temp;
    Bitmap bmp;
    @BindView(R.id.imageButton1) ImageView img1;
    @BindView(R.id.imageButton2) ImageView img2;
    @BindView(R.id.imageButton3) ImageView img3;
    @BindView(R.id.textView5) TextView textarea1;
    @BindView(R.id.textView11) TextView textarea2;
    @BindView(R.id.textView22) TextView textarea3;
    String Areatotal1;
    String Areatotal2;
    String Areatotal3;
    double areatotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organic);
        ButterKnife.bind(this);
        procesarcroma();
        //se reciben las areas hayadas en la otra actividad
        Intent intent = getIntent();
        Areatotal1 = intent.getStringExtra(PreprocessingActivity.Areas1).toString();
        Double area1=Double.parseDouble(Areatotal1);
        Areatotal2 = intent.getStringExtra(PreprocessingActivity.Areas2).toString();
        Double area2=Double.parseDouble(Areatotal2);
        Areatotal3 = intent.getStringExtra(PreprocessingActivity.Areas3).toString();
        Double area3=Double.parseDouble(Areatotal3);
    }
    public void procesarcroma() {
        ima=imread_mat("cromaoriginal");
        temp=componente(2);
        areacroma();
        imwrite_mat(temp,"areacroma");
        showima("capa1");
        showima("capa2");
        showima("capa3");
    }
    public Mat imread_mat(String a){
        Mat imagen;
        String nombre=a+".jpg";
        //Se lee la foto desde la ubicacion donde fue almacenada en la memoria interna
        imagen = Imgcodecs.imread(Environment.getExternalStorageDirectory()+
                "/sebas/"+nombre);
        return imagen;
    }
    //se muestra cada capa
    public void showima(String a){
        if (a=="capa1") {
            String nombre = a + ".jpg";
            bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +
                    "/sebas/" + nombre);
            img1.setImageBitmap(bmp);

        }
        if (a=="capa2") {
            String nombre = a + ".jpg";
            bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +
                    "/sebas/" + nombre);
            img2.setImageBitmap(bmp);
        }
        if (a=="capa3") {
            String nombre = a + ".jpg";
            bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +
                    "/sebas/" + nombre);
            img3.setImageBitmap(bmp);
        }
    }
    public void imwrite_mat(Mat imagen,String a){
        String nombre=a+".jpg";
        Imgcodecs.imwrite(Environment.getExternalStorageDirectory()+
                "/sebas/"+nombre,imagen);
    }
    //listener de los imagebutton
    @OnClick(R.id.imageButton1)
    public void datoscapa1(View view) {
        textarea1.setText(Areatotal1);
    }
    @OnClick(R.id.imageButton2)
    public void datoscapa2(View view) {
        textarea2.setText(Areatotal2);
    }
    @OnClick(R.id.imageButton3)
    public void datoscapa3(View view) {
        textarea3.setText(Areatotal3);
    }

    public Mat componente(int c){
        Mat imagen=ima;
        List<Mat> canales = new ArrayList<Mat>();
        imagen.zeros(ima.size(),ima.type());
        Core.split(ima,canales);
        imagen=canales.get(c);
        return imagen;
    }

    public void areacroma(){
        areatotal=0;
        Point a= new Point(0,0);
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        int rows=temp.rows();
        int cols=temp.cols();
        double[] capab={255};
        double[] capan={0};
        for (int i=0; i<rows; i++)
        {
            for (int j=0; j<cols; j++)
            {
                double[] pix = temp.get(i, j);
                if (pix[0]>100 ) {
                    temp.put(i, j, capab);
                }
                if ((i>rows/3 && i<2*rows/3) && (j>cols/3 && j<2*cols/3 )) {
                temp.put(i, j, capab);
            }

            }
        }

        Imgproc.findContours(temp,contours,hierarchy,Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_NONE);
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            Mat area=contours.get(contourIdx);
            double area2=Imgproc.contourArea(area);
            if (area2>10000) {
                areatotal=area2;
                Log.d("area", String.valueOf(areatotal));
            }


        }
    }

}

