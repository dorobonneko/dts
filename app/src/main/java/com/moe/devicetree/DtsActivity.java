package com.moe.devicetree;
import android.app.Activity;
import android.os.Bundle;
import java.io.File;
import com.moe.util.DtbUtil;
import java.io.FileReader;
import java.io.FileNotFoundException;
import android.widget.TextView;
import android.widget.EditText;
import android.app.ProgressDialog;
import com.moe.devicetree.widget.EditView;

public class DtsActivity extends Activity {
     private EditView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(tv=new EditView(this));
        dtb2dts((File)getIntent().getSerializableExtra("dtb"));
    }
    private void dtb2dts(final File file){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("正在解析");
        pd.show();
        new Thread(){
            public void run(){
                File dts=DtbUtil.dtb2dts(file);
                if(dts!=null)
                try {
                    FileReader fr=new FileReader(dts);
                    StringBuilder sb=new StringBuilder();
                    char[] buff=new char[512];
                    int len=-1;
                    while((len=fr.read(buff))!=-1){
                        sb.append(buff,0,len);
                    }
                    fr.close();
                    final String dts_string=sb.toString();
                    runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                pd.dismiss();
                                tv.setText(dts_string);
                            }
                        });
                    return;
                } catch (Exception e) {}
                runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            pd.dismiss();
                            finish();
                        }
                    });
            }
        }.start();
    }
    
    
}
