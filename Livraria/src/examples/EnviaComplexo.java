package examples;

import java.io.*;   import java.net.*;

public class EnviaComplexo {

public static void main (String args [])
 {
    try
    {
        Socket s = new Socket("localhost", 6789);
        ObjectOutputStream out = new ObjectOutputStream(new DataOutputStream(s.getOutputStream()));
        Complexo c = new Complexo();
        c.a = 3;
        c.b = 5;
        out.writeObject(c);
        s.close();
    }
    catch(Exception e2){}

  }

}
