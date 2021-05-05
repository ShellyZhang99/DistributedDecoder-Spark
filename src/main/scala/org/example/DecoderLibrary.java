package org.example;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.io.Serializable;

public interface DecoderLibrary extends Library, Serializable {


        public DecoderLibrary INSTANCE = (DecoderLibrary) Native.load("cpplib_shared.so", DecoderLibrary.class);

        Pointer Parallel_excl_decoder_ctor();
        //Parallel_excl_decoder* Parallel_excl_decoder_add_excl_decoder(Parallel_excl_decoder *self, const char *config_filename, int primary, char* pt_filename)
        int Parallel_excl_decoder_decode(Pointer self, int i);

        Pointer Parallel_excl_decoder_get(Pointer temp, int i);

    Pointer Parallel_decoder_ctor();
    //Parallel_excl_decoder* Parallel_decoder_add_excl_decoder(Parallel_decoder *self, const char *config_filename, int primary, char* pt_filename)
    int Parallel_decoder_add_excl_decoder(Pointer self, Pointer config_filename, int primary, Pointer pt_filename);


}
