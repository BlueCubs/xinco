package com.bluecubs.xinco.core.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Calendar;
import org.junit.jupiter.api.Test;

class XincoCoreLogServerBuilderTest {

  @Test
  void defaultConstructor_createsBuilder() {
    XincoCoreLogServerBuilder builder = new XincoCoreLogServerBuilder();
    assertThat(builder).isNotNull();
  }

  @Test
  void setters_returnSameInstance_forFluentChaining() {
    XincoCoreLogServerBuilder builder = new XincoCoreLogServerBuilder();
    assertThat(builder.setXincoCoreDataId(1)).isSameAs(builder);
    assertThat(builder.setXincoCoreUserId(2)).isSameAs(builder);
    assertThat(builder.setOpCode(3)).isSameAs(builder);
    assertThat(builder.setOperationDate(Calendar.getInstance())).isSameAs(builder);
    assertThat(builder.setOperationDescription("test")).isSameAs(builder);
    assertThat(builder.setVersionHigh(1)).isSameAs(builder);
    assertThat(builder.setVersionMid(0)).isSameAs(builder);
    assertThat(builder.setVersionLow(0)).isSameAs(builder);
    assertThat(builder.setVersionPostFix("")).isSameAs(builder);
  }

  @Test
  void createXincoCoreLogServer_withAllFields_buildsSuccessfully() {
    assertThatNoException()
        .isThrownBy(
            () -> {
              XincoCoreLogServer log =
                  new XincoCoreLogServerBuilder()
                      .setXincoCoreDataId(1)
                      .setXincoCoreUserId(1)
                      .setOpCode(1)
                      .setOperationDate(Calendar.getInstance())
                      .setOperationDescription("checkout")
                      .setVersionHigh(1)
                      .setVersionMid(0)
                      .setVersionLow(0)
                      .setVersionPostFix("SNAPSHOT")
                      .createXincoCoreLogServer();

              assertThat(log).isNotNull();
              assertThat(log.getXincoCoreDataId()).isEqualTo(1);
              assertThat(log.getXincoCoreUserId()).isEqualTo(1);
              assertThat(log.getOpCode()).isEqualTo(1);
              assertThat(log.getOpDescription()).isEqualTo("checkout");
              assertThat(log.getVersion()).isNotNull();
              assertThat(log.getVersion().getVersionHigh()).isEqualTo(1);
              assertThat(log.getVersion().getVersionPostfix()).isEqualTo("SNAPSHOT");
            });
  }

  @Test
  void createXincoCoreLogServer_nullDate_usesCurrentTime() {
    assertThatNoException()
        .isThrownBy(
            () -> {
              XincoCoreLogServer log =
                  new XincoCoreLogServerBuilder()
                      .setXincoCoreDataId(1)
                      .setXincoCoreUserId(1)
                      .setOpCode(2)
                      .setOperationDate(null)
                      .setOperationDescription("undo-checkout")
                      .createXincoCoreLogServer();
              assertThat(log).isNotNull();
              assertThat(log.getOpDatetime()).isNotNull();
            });
  }
}
