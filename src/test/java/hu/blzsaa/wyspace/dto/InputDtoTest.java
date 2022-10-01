package hu.blzsaa.wyspace.dto;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InputDtoTest {
  @Test
  void testBean() {
    assertThat(
        InputDto.class,
        allOf(
            hasValidBeanConstructor(),
            hasValidGettersAndSetters(),
            hasValidBeanHashCode(),
            hasValidBeanEquals(),
            hasValidBeanToString()));
  }
}
