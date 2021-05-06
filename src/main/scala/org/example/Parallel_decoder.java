package org.example;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import java.io.File;
import java.io.Serializable;

public class Parallel_decoder implements Serializable {

    public int add_excl_decoder(String config_filename, int primary, String pt_filename)
    {
        int result = 0;
        String fileName = "home/bigdataflow/DistributedDecoder/Test0/perf.data-aux-idx";
        fileName +=Integer.toString(primary);
        fileName +=".bin";
        File binFile = new File(fileName);
        if(binFile.exists())
            result = 1;
        return result;
    }


}
