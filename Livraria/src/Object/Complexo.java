package Object;

import java.io.*;
public class Complexo implements Serializable
{ double a, b;
  public Complexo()
  {  }
  public double modulo()
  { return Math.sqrt(a*a + b*b); }
}
