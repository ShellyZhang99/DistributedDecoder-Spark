package org.decoder;
import java.lang.Object;
public class JNITest {
    public Object decoder;
    public native Object getDecoder(String fileName);
}
