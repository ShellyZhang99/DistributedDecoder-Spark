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
        Pointer Parallel_excl_decoder_decode(Pointer self);

        Pointer Parallel_excl_decoder_get(Pointer dec, int i);

    }

    public Pointer self;

    public String name;

    public void setName(String n){ name = n;}

    public Parallel_excl_decoder()
    {
       self = CLibrary.INSTANCE.Parallel_excl_decoder_ctor();
        /*self = new MyPointer(Pointer.nativeValue(temp));
        self.setPointer(0, temp);*/
    }

    public Parallel_excl_decoder(Pointer dec, int i)
    {
        self = Parallel_excl_decoder.CLibrary.INSTANCE.Parallel_excl_decoder_get(dec, i);
    }

    public String decode()
    {
        Pointer temp = Parallel_excl_decoder.CLibrary.INSTANCE.Parallel_excl_decoder_decode(self);
        return temp.getString(0);
    }
}
