package org.example;

import com.sun.jna.Pointer;

import java.io.*;

public class MyPointer implements Externalizable {

    public long peer;

    public Pointer pointer;

    public MyPointer()
    {
    }


    public MyPointer(long peer2) {
        peer = peer2;
        pointer = Pointer.createConstant(peer2);
    }

    public MyPointer(Pointer p)
    {
        pointer = p;
        peer = Pointer.nativeValue(p);
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(peer);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.peer = in.readLong();
        this.pointer = Pointer.createConstant(this.peer);
    }
}
