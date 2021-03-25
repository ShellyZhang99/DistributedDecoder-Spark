package org.example;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.io.Serializable;

public class Parallel_excl_decoder implements Serializable {

    public interface CLibrary extends Library {

        CLibrary INSTANCE = (Parallel_excl_decoder.CLibrary)Native.load("cpplib_shared.so", CLibrary.class);

        Pointer Parallel_excl_decoder_ctor();
        //Parallel_excl_decoder* Parallel_excl_decoder_add_excl_decoder(Parallel_excl_decoder *self, const char *config_filename, int primary, char* pt_filename)
        void Parallel_excl_decoder_decode(Pointer self);

        Pointer Parallel_excl_decoder_get(int i);

    }

    public Object self;

    public String name;

    public void setName(String n){ name = n;}

    public Parallel_excl_decoder()
    {
        self = CLibrary.INSTANCE.Parallel_excl_decoder_ctor();
    }

    public Parallel_excl_decoder(int i)
    {
        self = Parallel_excl_decoder.CLibrary.INSTANCE.Parallel_excl_decoder_get(i);
    }

    public void decode()
    {
        Parallel_excl_decoder.CLibrary.INSTANCE.Parallel_excl_decoder_decode((Pointer)self);
    }
}
