package org.example;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.Serializable;
import java.lang.String;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class Decoder implements Serializable {
    public Vector<Parallel_decoder> decoders;
    public Vector<Parallel_excl_decoder> paral_decoders;

    public Decoder()
    {
        decoders = new Vector<Parallel_decoder>(8);
        paral_decoders = new Vector<Parallel_excl_decoder>(64);
    }

    public Excl_decoder add_decoder(String fileName, String decoderFile, FileSystem hdfs)
    {
        int p = fileName.charAt(17) - '0';
        Parallel_decoder pDecoder = new Parallel_decoder();

        Parallel_excl_decoder[] temp = pDecoder.add_excl_decoder("testFile/perf-attr-config", p ,"testFile/"+fileName);
        decoders.add(pDecoder);
        Excl_decoder excl_decoder = new Excl_decoder();
        //打开hdfs文件
        try {
            //Configuration conf = new Configuration();
            //FileSystem fs = FileSystem.get(URI.create(decoderFile), conf);
            Path path = new Path(decoderFile);
            FSDataOutputStream out = hdfs.append(path);



            for (int i = 0; i < Arrays.stream(temp).count(); i++) {
                int place = p * 8 + i;
                temp[i].name = String.valueOf(place);
                //把temp[i].name写在文件上
                String h = temp[i].name + " ";
                out.write(h.getBytes("UTF-8"));
                excl_decoder.paral_decoder.add(temp[i]);
            }
            out.close();
        }
        catch (Exception e)
        {

        }
        return excl_decoder;
        //关闭hdfs文件
    }

    public void decode(String name, String outputFile)
    {
        int p = Integer.parseInt(name);
        //打开hdfs文件
        String answer = paral_decoders.get(p).decode();
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(outputFile), conf);
            Path path = new Path(outputFile);
            FSDataOutputStream out = fs.append(path);
            out.write(outputFile.getBytes("UTF-8"));
            String temp = "\n";
            out.write(temp.getBytes(StandardCharsets.UTF_8));
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
