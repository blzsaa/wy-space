package hu.blzsaa.wyspace.util;

import java.io.*;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReaderFactory {
  public BufferedReader createNewBufferedFileReader(InputStream inputStream) {
    return new BufferedReader(new InputStreamReader(inputStream));
  }
}
