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
    public interface CLibrary extends Library {

        CLibrary INSTANCE = (CLibrary)Native.load("cpplib_shared.so",
                CLibrary.class);

        Pointer Parallel_decoder_ctor();
        //Parallel_excl_decoder* Parallel_decoder_add_excl_decoder(Parallel_decoder *self, const char *config_filename, int primary, char* pt_filename)
        void Parallel_decoder_add_excl_decoder(Pointer self, Pointer config_filename, int primary, Pointer pt_filename);

    }

    public Object self;

    public Parallel_decoder()
    {
        self = CLibrary.INSTANCE.Parallel_decoder_ctor();

    }

    public Excl_decoder add_excl_decoder(String config_filename, int primary, String pt_filename)
    {
        Pointer pconfig_filename = new Memory(config_filename.length()+1);
        pconfig_filename.setString(0, config_filename);
        Pointer ppt_filename = new Memory(pt_filename.length()+1);
        ppt_filename.setString(0, pt_filename);
        CLibrary.INSTANCE.Parallel_decoder_add_excl_decoder((Pointer)self, pconfig_filename, primary, ppt_filename);
        Excl_decoder excl_decoders = new Excl_decoder();
        for(int i=0; i<8; i++)
        {
            excl_decoders.paral_decoder.add(new Parallel_excl_decoder(i));
        }
        return excl_decoders;
    }
}
