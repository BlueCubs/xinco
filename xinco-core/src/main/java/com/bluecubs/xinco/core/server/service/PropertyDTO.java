package com.bluecubs.xinco.core.server.service;

import java.io.Serializable;
import java.util.Objects;

public final class PropertyDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String key;
  private final String value;

  public PropertyDTO(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String key() {
    return key;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PropertyDTO)) {
      return false;
    }
    PropertyDTO that = (PropertyDTO) o;
    return Objects.equals(key, that.key) && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    return "PropertyDTO[key=" + key + ", value=" + value + "]";
  }
}
