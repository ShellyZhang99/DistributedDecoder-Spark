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


    public void add_decoder(String fileName, String decoderFile)
    {
        try {
            int p = fileName.charAt(17) - '0';
                Configuration conf = new Configuration();
                conf.set("fs.defaultFS", "hdfs://master:9000");
                FileSystem hdfs = FileSystem.get(URI.create("hdfs://master:9000"), conf);
                Path path = new Path(decoderFile);
                FSDataOutputStream out = hdfs.append(path);
                for (int i = 0; i < 8; i++) {
                    int place = p * 8 + i;
                    String name = String.valueOf(place);
                    //把temp[i].name写在文件上
                    String h = name + " ";
                    fileWriterMethod("/home/bigdataflow/DistributedDecoder/Test0/outputFileTemp.txt", "\n"+h+"\n");
                    out.write(h.getBytes("UTF-8"));
                }
                out.close();
        }
        catch (Exception e)
        {
            try
            {
                fileWriterMethod("/home/bigdataflow/DistributedDecoder/Test0/outputFileTemp.txt", "\n"+e.getMessage());

            }
            catch(Exception e2)
            {
                System.out.println(e2.getMessage());
            }
        }
        //关闭hdfs文件
    }

    public static void fileWriterMethod(String filepath, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filepath)) {
            fileWriter.append(content);
        }
    }

    public void decode(ExtraDecoder extraDecoder, String name, String outputFile)
    {
        try {
        int p = Integer.parseInt(name);
        Parallel_excl_decoder pdecoder = new Parallel_excl_decoder();
        String temp = "";
            temp = pdecoder.decode(extraDecoder, p);

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
            }
            out.close();
        }
        catch (Exception e)
        {
            try
            {
                fileWriterMethod("/home/bigdataflow/DistributedDecoder/Test0/outputFileTemp.txt", "\n"+e.getMessage());

            }
            catch(Exception e2)
            {
                System.out.println(e2.getMessage());
            }
        }
        //写进文件
        //关闭hdfs文件
    }
}
