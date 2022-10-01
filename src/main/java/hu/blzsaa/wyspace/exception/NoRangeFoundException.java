package hu.blzsaa.wyspace.exception;

public class NoRangeFoundException extends RuntimeException {
  public NoRangeFoundException() {
    super("could not found maximum range");
  }
}
