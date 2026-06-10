package com.bluecubs.xinco.core.server.persistence.controller.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PersistenceExceptionsTest {

  @Test
  void illegalOrphanException_withMessages() {
    var messages = List.of("child1", "child2");
    var ex = new IllegalOrphanException(messages);
    assertThat(ex.getMessage()).isEqualTo("child1");
    assertThat(ex.getMessages()).containsExactly("child1", "child2");
  }

  @Test
  void illegalOrphanException_withNullMessages() {
    var ex = new IllegalOrphanException(null);
    assertThat(ex.getMessage()).isNull();
    assertThat(ex.getMessages()).isEmpty();
  }

  @Test
  void nonexistentEntityException_messageOnly() {
    var ex = new NonexistentEntityException("not found");
    assertThat(ex.getMessage()).isEqualTo("not found");
    assertThat(ex.getCause()).isNull();
  }

  @Test
  void nonexistentEntityException_withCause() {
    var cause = new RuntimeException("root");
    var ex = new NonexistentEntityException("not found", cause);
    assertThat(ex.getMessage()).isEqualTo("not found");
    assertThat(ex.getCause()).isSameAs(cause);
  }

  @Test
  void preexistingEntityException_messageOnly() {
    var ex = new PreexistingEntityException("already exists");
    assertThat(ex.getMessage()).isEqualTo("already exists");
    assertThat(ex.getCause()).isNull();
  }

  @Test
  void preexistingEntityException_withCause() {
    var cause = new RuntimeException("root");
    var ex = new PreexistingEntityException("already exists", cause);
    assertThat(ex.getMessage()).isEqualTo("already exists");
    assertThat(ex.getCause()).isSameAs(cause);
  }
}
