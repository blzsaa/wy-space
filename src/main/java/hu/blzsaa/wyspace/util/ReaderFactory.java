package hu.blzsaa.wyspace.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReaderFactory {
  public BufferedReader createNewBufferedFileReader(String s) throws FileNotFoundException {
    return new BufferedReader(new FileReader(s));
  }
}
