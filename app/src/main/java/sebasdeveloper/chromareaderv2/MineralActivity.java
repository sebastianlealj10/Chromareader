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
        procesarcroma();
    }
    public void procesarcroma() {
        ima=imread_mat("capa3");
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
        String nombre=a+".jpg";
        bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+
                "/sebas/"+nombre);
        img.setImageBitmap(bmp);
    }
}
