package com.moe.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DtbUtil {
    private static final byte[] HEADER=new byte[]{(byte)0xd0,(byte)0x0d,(byte)0xfe,(byte)0xed};
    private static final String END="\n";
    

    public static File[] splitDtb(File dtb){
        byte[] buff=new byte[4];
        List<File> list=new ArrayList<File>();
        try {
            long length=dtb.length(),read=0,index=0;
            DataInputStream fis=new DataInputStream(new FileInputStream(dtb));
            while(read<length){
                fis.readFully(buff);
                read+=4;
                if(Arrays.equals(buff,HEADER)){
                    fis.readFully(buff);
                    read+=4;

                    int size=(((((((buff[0]&0xff)<<8)|(buff[1]&0xff))<<8)|(buff[2]&0xff))<<8)|(buff[3]&0xff))-8;
                    File dtb_index=new File(dtb.getParent(),"dtb."+index);
                    index++;
                    list.add(dtb_index);
                    byte[] buffer=new byte[size];
                    fis.readFully(buffer);
                    read+=size;
                    FileOutputStream fos=new FileOutputStream(dtb_index);
                    fos.write(HEADER);
                    fos.write(buff);
                    fos.write(buffer);
                    fos.flush();
                    fos.close();
                }
                else{
                    break;
                }
            }
        } catch (IOException e) {}
        return list.toArray(new File[list.size()]);
    }
    public static File dtb2dts(File file){
        ProcessBuilder pb=new ProcessBuilder();
        pb.command("su");
        pb.directory(file.getParentFile());
        String[] name=file.getName().split("\\.");
        
        File dts=new File(file.getParent(),"dts."+name[1]);
        try {
            Process p=pb.start();
            OutputStream output=p.getOutputStream();
            StringBuilder sb=new StringBuilder();
            sb.append("./dtc -I dtb -O dts ");
            sb.append(file.getName());
            sb.append(" -o ");
            sb.append(dts.getName());
            sb.append(END);
            sb.append("exit;\n");
            output.write(sb.toString().getBytes());
            output.flush();
            p.waitFor();
            p.destroy();

        } catch (Exception e) {
            return null;
        }
        return dts;
    }
    public static boolean unpackImage(File dir){
        ProcessBuilder pb=new ProcessBuilder();
        pb.command("su");
        pb.directory(dir);
        File boot_img=new File(dir,"boot.img");
        try {
            Process p=pb.start();
            OutputStream output=p.getOutputStream();
            StringBuilder sb=new StringBuilder();
            sb.append("dd if=/dev/block/by-name/boot of=");
            sb.append(boot_img.getAbsolutePath());
            sb.append(END);
            sb.append("./magiskboot unpack ");
            sb.append(boot_img.getAbsolutePath());
            sb.append(END);
            sb.append("exit;\n");
            output.write(sb.toString().getBytes());
            output.flush();
            p.waitFor();
            p.destroy();
            return true;
        } catch (Exception e) {

        }
        return false;
    }
}
