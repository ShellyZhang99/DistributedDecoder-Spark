package org.example;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.io.Serializable;

public class Parallel_excl_decoder implements Serializable {
    /*public interface DecoderLibrary extends Library {


        DecoderLibrary INSTANCE = (DecoderLibrary) Native.load("/usr/lib/cpplib_shared.so", DecoderLibrary.class);

        Pointer Parallel_excl_decoder_ctor();
        //Parallel_excl_decoder* Parallel_excl_decoder_add_excl_decoder(Parallel_excl_decoder *self, const char *config_filename, int primary, char* pt_filename)
        int Parallel_excl_decoder_decode(Pointer self, int i);

        Pointer Parallel_excl_decoder_get(Pointer temp, int i);

        Pointer Parallel_decoder_ctor();
        //Parallel_excl_decoder* Parallel_decoder_add_excl_decoder(Parallel_decoder *self, const char *config_filename, int primary, char* pt_filename)
        int Parallel_decoder_add_excl_decoder(Pointer self, Pointer config_filename, int primary, Pointer pt_filename);


    }*/

    public String name;

    public void setName(String n)
    {
        name = n;
    }

    public String decode(SimpDecoder extraDecoder,int i)
    {


        /*Pointer tempPointer = DecoderLibrary.INSTANCE.Parallel_decoder_ctor();
        int temp = DecoderLibrary.INSTANCE.Parallel_excl_decoder_decode(tempPointer, i);*/

        extraDecoder.decode(i);
        return "/home/bigdataflow/DistributedDecoder/Test0/outputFileTemp"+Integer.toString(i)+".txt";
    }
}
