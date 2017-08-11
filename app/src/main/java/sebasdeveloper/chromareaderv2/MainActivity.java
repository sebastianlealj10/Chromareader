package sebasdeveloper.chromareaderv2;
//clases que se usan en esta actividad

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

//Clase de la vista para la actividad principal
public class MainActivity extends AppCompatActivity
{   Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.button4)
    public void lanzarapp(View view)
        {
            intent = new Intent(this, CoreActivity.class);
            startActivity(intent);
        }
}
