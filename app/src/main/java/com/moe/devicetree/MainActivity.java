package com.moe.devicetree;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.moe.util.DtbUtil;
import android.widget.ListView;
import com.moe.devicetree.adapter.DtbAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.content.Intent;

public class MainActivity extends Activity implements ListView.OnItemClickListener{ 
private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview=findViewById(R.id.listview);
        listview.setDividerHeight(0);
        listview.setOnItemClickListener(this);
        checkFile();
        unpack();
    }

    @Override                 
        public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
        {
            startActivity(new Intent(this,DtsActivity.class).putExtra("dtb",(File)p1.getAdapter().getItem(p3)));
        }


    private void unpack(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("正在解包");
        pd.show();
        new Thread(){
            public void run(){
                if(DtbUtil.unpackImage(getFilesDir())){
                    final File[] dtb_file=DtbUtil.splitDtb(new File(getFilesDir(),"kernel_dtb"));
                    //DtbUtil.dtb2dts(dtb_file[8]);

                    runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                listview.setAdapter(new DtbAdapter(dtb_file));
                                pd.dismiss();
                            }
                        });
                }else{
                    runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                pd.dismiss();
                                new AlertDialog.Builder(MainActivity.this).setMessage("失败").show();
                            }


                        });
                }
            }
        }.start();
    }

    private void checkFile(){
        File dtc=new File(getFilesDir(),"dtc");
        if(!dtc.canExecute()){
            dtc.delete();
            try {
                InputStream input=getAssets().open("dtc");
                OutputStream output=new FileOutputStream(dtc);
                byte[] buff=new byte[256];
                int len=-1;
                while((len=input.read(buff))!=-1){
                    output.write(buff,0,len);
                }
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {}
            try {
                Process p=Runtime.getRuntime().exec("chmod 700 " + dtc.getAbsolutePath());
                p.waitFor();
                p.destroy();
            } catch (Exception e) {}
        }
        File magiskboot=new File(getFilesDir(),"magiskboot");
        if(!magiskboot.canExecute()){
            magiskboot.delete();
            try {
                InputStream input=getAssets().open("magiskboot");
                OutputStream output=new FileOutputStream(magiskboot);
                byte[] buff=new byte[256];
                int len=-1;
                while((len=input.read(buff))!=-1){
                    output.write(buff,0,len);
                }
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {}
            try {
                Process p=Runtime.getRuntime().exec("chmod 700 " + magiskboot.getAbsolutePath());
                p.waitFor();
                p.destroy();
            } catch (Exception e) {}
        }
    }
} 
