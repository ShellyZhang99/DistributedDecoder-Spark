package org.example;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.io.Serializable;
import  java.util.Vector;
import java.util.Arrays;

public class Parallel_decoder implements Serializable {
    public interface DecoderLibrary extends Library {


        DecoderLibrary INSTANCE = (DecoderLibrary) Native.load("/home/bigdataflow/DistributedDecoder/cpplib_shared.so", DecoderLibrary.class);

        Pointer Parallel_excl_decoder_ctor();
        //Parallel_excl_decoder* Parallel_excl_decoder_add_excl_decoder(Parallel_excl_decoder *self, const char *config_filename, int primary, char* pt_filename)
        int Parallel_excl_decoder_decode(Pointer self, int i);

        Pointer Parallel_excl_decoder_get(Pointer temp, int i);

        Pointer Parallel_decoder_ctor();
        //Parallel_excl_decoder* Parallel_decoder_add_excl_decoder(Parallel_decoder *self, const char *config_filename, int primary, char* pt_filename)
        int Parallel_decoder_add_excl_decoder(Pointer self, Pointer config_filename, int primary, Pointer pt_filename);


    }
    public MyPointer self;

    public Parallel_decoder()
    {
        self = new MyPointer(DecoderLibrary.INSTANCE.Parallel_decoder_ctor());
    }

    public Excl_decoder add_excl_decoder(String config_filename, int primary, String pt_filename)
    {
        Pointer pconfig_filename = new Memory(config_filename.length()+1);
        pconfig_filename.setString(0, config_filename);
        Pointer ppt_filename = new Memory(pt_filename.length()+1);
        ppt_filename.setString(0, pt_filename);
        int result = DecoderLibrary.INSTANCE.Parallel_decoder_add_excl_decoder(self.pointer, pconfig_filename, primary, ppt_filename);

        Excl_decoder excl_decoders = new Excl_decoder();
        if(result >= 0)
        {
            for(int i=0; i<8; i++)
            {
                excl_decoders.paral_decoder.add(new Parallel_excl_decoder(self, i));
            }
        }
        return excl_decoders;
    }


}
