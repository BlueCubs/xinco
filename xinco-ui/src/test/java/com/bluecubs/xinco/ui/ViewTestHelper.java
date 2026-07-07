package com.bluecubs.xinco.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.di.Instantiator;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.lang.reflect.Field;
import java.util.stream.Stream;

class ViewTestHelper {

  static void registerI18NProvider() {
    try {
      VaadinService service = VaadinService.getCurrent();
      Field f = findInstantiatorField(service.getClass());
      f.setAccessible(true);
      Instantiator orig = (Instantiator) f.get(service);
      f.set(service, new I18NInstantiator(orig));
    } catch (Exception e) {
      throw new RuntimeException("Failed to register I18NProvider", e);
    }
  }

  private static Field findInstantiatorField(Class<?> clazz) throws NoSuchFieldException {
    for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
      try {
        return c.getDeclaredField("instantiator");
      } catch (NoSuchFieldException ignored) {
      }
    }
    throw new NoSuchFieldException("instantiator field not found");
  }

  private static class I18NInstantiator implements Instantiator {
    private final Instantiator delegate;

    I18NInstantiator(Instantiator delegate) {
      this.delegate = delegate;
    }

    @Override
    public Stream<VaadinServiceInitListener> getServiceInitListeners() {
      return delegate.getServiceInitListeners();
    }

    @Override
    public <T> T getOrCreate(Class<T> type) {
      return delegate.getOrCreate(type);
    }

    @Override
    public <T extends Component> T createComponent(Class<T> type) {
      return delegate.createComponent(type);
    }

    @Override
    public I18NProvider getI18NProvider() {
      return new XincoI18NProvider();
    }
  }
}
