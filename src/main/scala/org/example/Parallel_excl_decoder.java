package org.example;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.io.Serializable;

public class Parallel_excl_decoder implements Serializable {
    public interface DecoderLibrary extends Library{


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

    public String name;

    public void setName(String n){ name = n;}

    public Parallel_excl_decoder()
    {
        self = new MyPointer(DecoderLibrary.INSTANCE.Parallel_excl_decoder_ctor());
        /*self = new MyPointer(Pointer.nativeValue(temp));
        self.setPointer(0, temp);*/
    }

    public Parallel_excl_decoder(MyPointer temp, int i)
    {
        self = new MyPointer(DecoderLibrary.INSTANCE.Parallel_excl_decoder_get(temp.pointer, i));
    }

    public String decode(int i)
    {
        int temp = DecoderLibrary.INSTANCE.Parallel_excl_decoder_decode(self.pointer, i);
        if(temp<0)
            System.out.println("lafse");
        return "outputFileTemp.txt";
    }
}
