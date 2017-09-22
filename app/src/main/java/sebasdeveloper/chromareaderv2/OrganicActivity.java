package sebasdeveloper.chromareaderv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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

public class OrganicActivity extends AppCompatActivity {
    Mat ima;
    Bitmap bmp;
    @BindView(R.id.imageButton1) ImageView img1;
    @BindView(R.id.imageButton2) ImageView img2;
    @BindView(R.id.imageButton3) ImageView img3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organic);
        ButterKnife.bind(this);
        procesarcroma();
    }
    public void procesarcroma() {
        ima=imread_mat("capa1");
        ima=fillholes(ima,100000000);
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
    public Mat fillholes(Mat tempp,int areas){
        tempp.zeros(ima.size(),ima.type());
        Point a= new Point(0,0);
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(tempp,contours,hierarchy,Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_NONE);
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            Mat area=contours.get(contourIdx);
            double area2=Imgproc.contourArea(area);
            if (area2<areas) {
                Imgproc.drawContours(tempp, contours,contourIdx, new Scalar(0, 0,0),-1,8,hierarchy,0,a);
                Log.d("area", String.valueOf(area2));
                //  temp2=contours.get(contourIdx);
            }
        }
        return tempp;
    }
}

