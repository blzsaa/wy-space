package hu.blzsaa.wyspace.dto;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import com.google.code.beanmatchers.BeanMatchers;
import java.time.LocalTime;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultDtoTest {

  @BeforeEach
  void setUp() {
    BeanMatchers.registerValueGenerator(
        () -> LocalTime.of(new Random().nextInt(24), new Random().nextInt(60)), LocalTime.class);
  }

  @Test
  void testBean() {
    assertThat(
        ResultDto.class,
        allOf(
            hasValidBeanConstructor(),
            hasValidGettersAndSetters(),
            hasValidBeanHashCode(),
            hasValidBeanEquals(),
            hasValidBeanToString()));
  }
}
