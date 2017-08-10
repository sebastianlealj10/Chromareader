package sebasdeveloper.chromareaderv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MineralActivity extends AppCompatActivity {
    Mat ima;
    Bitmap bmp;
    @BindView(R.id.image3) ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineral);
        ButterKnife.bind(this);
        ima=imread_mat();
        imwrite_mat(ima);
        showima();
    }


    public Mat imread_mat(){
        Mat imagen;
        //Se lee la foto desde la ubicacion donde fue almacenada en la memoria interna
        imagen = Imgcodecs.imread(Environment.getExternalStorageDirectory()+
                "/sebas/"+"cromapreprocesado.jpg");
        return imagen;
    }
    public void imwrite_mat(Mat imagen){
        Imgcodecs.imwrite(Environment.getExternalStorageDirectory()+
                "/sebas/"+"capamineral.jpg",imagen);
    }
    public void showima(){
        bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+
                "/sebas/"+"capamineral.jpg");
        img.setImageBitmap(bmp);
    }
}