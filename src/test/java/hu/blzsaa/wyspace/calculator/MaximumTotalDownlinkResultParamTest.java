package hu.blzsaa.wyspace.calculator;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import org.junit.jupiter.api.Test;

class MaximumTotalDownlinkResultParamTest {
  @Test
  void testBean() {
    assertThat(
        MaximumTotalDownlinkResultParam.class,
        allOf(
            hasValidBeanConstructor(),
            hasValidGettersAndSetters(),
            hasValidBeanHashCode(),
            hasValidBeanEquals(),
            hasValidBeanToString()));
  }
}
