package org.example;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.io.Serializable;

public class Parallel_excl_decoder implements Serializable {

    public interface CLibrary extends Library {

        Parallel_excl_decoder.CLibrary INSTANCE = (Parallel_excl_decoder.CLibrary)Native.loadLibrary("cpplib_shared",
                Parallel_excl_decoder.CLibrary.class);

        Pointer Parallel_excl_decoder_ctor();
        //Parallel_excl_decoder* Parallel_excl_decoder_add_excl_decoder(Parallel_excl_decoder *self, const char *config_filename, int primary, char* pt_filename)
        String Parallel_excl_decoder_decode(Pointer self);

    }

    private Pointer self;

    public String name;

    public Parallel_excl_decoder()
    {
        self = Parallel_excl_decoder.CLibrary.INSTANCE.Parallel_excl_decoder_ctor();
    }

    public String decode()
    {
        return Parallel_excl_decoder.CLibrary.INSTANCE.Parallel_excl_decoder_decode(self);
    }
}
