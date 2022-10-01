package hu.blzsaa.wyspace.util;

import com.google.gson.Gson;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class GsonConfig {
  @Produces
  @ApplicationScoped
  public Gson gson() {
    return new Gson();
  }
}
