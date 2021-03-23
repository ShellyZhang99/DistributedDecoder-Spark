package org.example;

import java.io.Serializable;
import java.util.Vector;

public class Excl_decoder implements Serializable {
    public Vector<Parallel_excl_decoder> paral_decoder;

    public Excl_decoder()
    {
        paral_decoder = new Vector<Parallel_excl_decoder>(8);
    }

}
