package hu.blzsaa.wyspace.util;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class JsonConfig {
  @Produces
  @ApplicationScoped
  public JsonMapper gson() {
    return JsonMapper.builder().addModule(new JavaTimeModule()).build();
  }
}
