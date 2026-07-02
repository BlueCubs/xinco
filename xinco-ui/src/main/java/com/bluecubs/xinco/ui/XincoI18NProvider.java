package com.bluecubs.xinco.ui;

import com.vaadin.flow.i18n.I18NProvider;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.springframework.stereotype.Component;

@Component
public class XincoI18NProvider implements I18NProvider {

  private static final String BUNDLE_BASE = "com/bluecubs/xinco/messages/XincoMessages";

  private static final List<Locale> PROVIDED_LOCALES =
      List.of(
          Locale.ENGLISH,
          Locale.FRENCH,
          Locale.GERMAN,
          Locale.ITALIAN,
          new Locale("nl"),
          new Locale("pl"),
          new Locale("pt"),
          new Locale("pt", "BR"),
          new Locale("es"),
          new Locale("cz"),
          Locale.SIMPLIFIED_CHINESE);

  @Override
  public List<Locale> getProvidedLocales() {
    return PROVIDED_LOCALES;
  }

  @Override
  public String getTranslation(String key, Locale locale, Object... params) {
    try {
      return ResourceBundle.getBundle(BUNDLE_BASE, locale).getString(key);
    } catch (MissingResourceException e) {
      try {
        return ResourceBundle.getBundle(BUNDLE_BASE, Locale.ENGLISH).getString(key);
      } catch (MissingResourceException ex) {
        return key;
      }
    }
  }
}
