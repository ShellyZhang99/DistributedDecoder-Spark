package org.example;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.web.WebHdfsFileSystem;

import java.io.*;
import java.lang.String;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class Decoder implements Serializable {
    public Vector<Parallel_decoder> decoders;
    public Vector<Parallel_excl_decoder> paral_decoders;

    public Decoder() {

        decoders = new Vector<Parallel_decoder>(8);
        paral_decoders = new Vector<Parallel_excl_decoder>(64);
    }

    public Excl_decoder add_decoder(String fileName, String decoderFile)
    {
        int p = fileName.charAt(17) - '0';
        Parallel_decoder pDecoder = new Parallel_decoder();

        Excl_decoder excl_decoder = pDecoder.add_excl_decoder("testFile/perf-attr-config", p ,"testFile/"+fileName);
        Vector<Parallel_excl_decoder> temp = excl_decoder.paral_decoder;
        decoders.add(pDecoder);
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://172.24.5.137:9000");

        //打开hdfs文件
        try {
            //Configuration conf = new Configuration();
            //FileSystem fs = FileSystem.get(URI.create(decoderFile), conf);
            FileSystem hdfs = FileSystem.get(URI.create("hdfs://172.24.5.137:9000"), conf);
            Path path = new Path(decoderFile);
            FSDataOutputStream out = hdfs.append(path);


            Iterator<Parallel_excl_decoder> ite = temp.iterator();
            for (int i=0; i < temp.size()&& ite.hasNext(); i++) {
                int place = p * 8 + i;
                String name = String.valueOf(place);
                ite.next().setName(name);
                //把temp[i].name写在文件上
                String h = name + " ";
                out.write(h.getBytes("UTF-8"));
            }
            out.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return excl_decoder;
        //关闭hdfs文件
    }

    public void decode(String name, String outputFile)
    {
        int p = Integer.parseInt(name);
        //打开hdfs文件
        Iterator<Parallel_excl_decoder> ite = paral_decoders.iterator();
        Parallel_excl_decoder pdecoder = null;
        while(ite.hasNext())
        {
            Parallel_excl_decoder t = ite.next();
            if(t.name.equals(name))
            {
                pdecoder = t;
                break;
            }
        }
        String temp = "";
        if(pdecoder != null)
            temp = pdecoder.decode(p);
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(outputFile), conf);
            Path path = new Path(outputFile);
            FSDataOutputStream out = fs.append(path);
            File file = new File(temp);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    out.write(lineTxt.getBytes(StandardCharsets.UTF_8));
                }
                br.close();
            } else {
                System.out.println("文件不存在!");
            }
            out.close();
        }
        catch (Exception e)
        {

        }
        //写进文件
        //关闭hdfs文件
    }

    public void add_parallel_decoders(List<Excl_decoder> decoders)
    {
        Iterator<Excl_decoder> iter = decoders.iterator();
        while(iter.hasNext()) {
            paral_decoders.addAll(iter.next().paral_decoder);
        }
    }
}
