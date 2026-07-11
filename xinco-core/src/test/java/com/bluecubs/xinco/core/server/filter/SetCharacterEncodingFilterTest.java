package com.bluecubs.xinco.core.server.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SetCharacterEncodingFilterTest {

  private SetCharacterEncodingFilter filter;
  private FilterConfig config;
  private ServletRequest request;
  private ServletResponse response;
  private FilterChain chain;

  @BeforeEach
  void setUp() {
    filter = new SetCharacterEncodingFilter();
    config = mock(FilterConfig.class);
    request = mock(ServletRequest.class);
    response = mock(ServletResponse.class);
    chain = mock(FilterChain.class);
  }

  @Test
  void init_setsEncodingAndIgnoreTrue() throws ServletException {
    when(config.getInitParameter("encoding")).thenReturn("UTF-8");
    when(config.getInitParameter("ignore")).thenReturn("true");
    filter.init(config);
    assertThat(filter.encoding).isEqualTo("UTF-8");
    assertThat(filter.ignore).isTrue();
  }

  @Test
  void init_ignoreYes() throws ServletException {
    when(config.getInitParameter("encoding")).thenReturn("UTF-8");
    when(config.getInitParameter("ignore")).thenReturn("yes");
    filter.init(config);
    assertThat(filter.ignore).isTrue();
  }

  @Test
  void init_ignoreOtherValue_setsFalse() throws ServletException {
    when(config.getInitParameter("encoding")).thenReturn("ISO-8859-1");
    when(config.getInitParameter("ignore")).thenReturn("false");
    filter.init(config);
    assertThat(filter.ignore).isFalse();
  }

  @Test
  void init_nullIgnore_defaultsToTrue() throws ServletException {
    when(config.getInitParameter("encoding")).thenReturn("UTF-8");
    when(config.getInitParameter("ignore")).thenReturn(null);
    filter.init(config);
    assertThat(filter.ignore).isTrue();
  }

  @Test
  void doFilter_setsEncodingOnRequest() throws IOException, ServletException {
    when(config.getInitParameter("encoding")).thenReturn("UTF-8");
    when(config.getInitParameter("ignore")).thenReturn("true");
    filter.init(config);
    when(request.getCharacterEncoding()).thenReturn(null);

    filter.doFilter(request, response, chain);

    verify(request).setCharacterEncoding("UTF-8");
    verify(chain).doFilter(request, response);
  }

  @Test
  void doFilter_skipsSetWhenEncodingPresentAndNotIgnoring() throws IOException, ServletException {
    when(config.getInitParameter("encoding")).thenReturn("UTF-8");
    when(config.getInitParameter("ignore")).thenReturn("false");
    filter.init(config);
    when(request.getCharacterEncoding()).thenReturn("ISO-8859-1");

    filter.doFilter(request, response, chain);

    verify(request, never()).setCharacterEncoding(anyString());
    verify(chain).doFilter(request, response);
  }

  @Test
  void destroy_clearsState() throws ServletException {
    when(config.getInitParameter("encoding")).thenReturn("UTF-8");
    when(config.getInitParameter("ignore")).thenReturn("true");
    filter.init(config);
    filter.destroy();
    assertThat(filter.encoding).isNull();
    assertThat(filter.filterConfig).isNull();
  }
}
