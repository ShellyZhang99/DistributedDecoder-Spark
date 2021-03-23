package org.example;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.io.Serializable;
import  java.util.Vector;
import java.util.Arrays;

public class Parallel_decoder implements Serializable {
    public interface CLibrary extends Library {

        CLibrary INSTANCE = (CLibrary)Native.load("cpplib_shared.so",
                CLibrary.class);

        Pointer Parallel_decoder_ctor();
        //Parallel_excl_decoder* Parallel_decoder_add_excl_decoder(Parallel_decoder *self, const char *config_filename, int primary, char* pt_filename)
        Pointer Parallel_decoder_add_excl_decoder(Pointer self, Pointer config_filename, int primary, Pointer pt_filename);

    }

    private Pointer self;

    public Parallel_decoder()
    {
        self = CLibrary.INSTANCE.Parallel_decoder_ctor();
    }

    public Parallel_excl_decoder[] add_excl_decoder(String config_filename, int primary, String pt_filename)
    {
        Pointer pconfig_filename = new Memory(config_filename.length()+1);
        pconfig_filename.setString(0, config_filename);
        Pointer ppt_filename = new Memory(pt_filename.length()+1);
        ppt_filename.setString(0, pt_filename);
        Pointer temp = CLibrary.INSTANCE.Parallel_decoder_add_excl_decoder(self, pconfig_filename, primary, ppt_filename);
        Pointer[] tempArray = temp.getPointerArray(0);
        return (Parallel_excl_decoder[]) Arrays.stream(tempArray).toArray();
    }
}
