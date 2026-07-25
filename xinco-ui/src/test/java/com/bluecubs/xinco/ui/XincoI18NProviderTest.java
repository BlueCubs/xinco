package com.bluecubs.xinco.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XincoI18NProviderTest {

  private XincoI18NProvider provider;

  @BeforeEach
  void setup() {
    provider = new XincoI18NProvider();
  }

  // ---- getProvidedLocales ----

  @Test
  void getProvidedLocales_returns11Locales() {
    assertEquals(11, provider.getProvidedLocales().size());
  }

  @Test
  void getProvidedLocales_containsExpectedLocales() {
    List<Locale> locales = provider.getProvidedLocales();
    assertTrue(locales.contains(Locale.ENGLISH), "English");
    assertTrue(locales.contains(Locale.FRENCH), "French");
    assertTrue(locales.contains(Locale.GERMAN), "German");
    assertTrue(locales.contains(Locale.ITALIAN), "Italian");
    assertTrue(locales.contains(Locale.of("nl")), "Dutch");
    assertTrue(locales.contains(Locale.of("pl")), "Polish");
    assertTrue(locales.contains(Locale.of("pt")), "Portuguese");
    assertTrue(locales.contains(Locale.of("pt", "BR")), "Brazilian Portuguese");
    assertTrue(locales.contains(Locale.of("es")), "Spanish");
    assertTrue(locales.contains(Locale.of("cz")), "Czech");
    assertTrue(locales.contains(Locale.SIMPLIFIED_CHINESE), "Simplified Chinese");
  }

  @Test
  void getProvidedLocales_returnsImmutableList() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> provider.getProvidedLocales().add(Locale.JAPANESE));
  }

  // ---- getTranslation: English ----

  @Test
  void getTranslation_english_returnsCorrectValue() {
    assertEquals("Folder", provider.getTranslation("general.folder", Locale.ENGLISH));
  }

  @Test
  void getTranslation_english_filenameKey() {
    assertEquals("File Name", provider.getTranslation("general.filename", Locale.ENGLISH));
  }

  @Test
  void getTranslation_english_languageKey() {
    assertEquals("Language", provider.getTranslation("general.language", Locale.ENGLISH));
  }

  // ---- getTranslation: German ----

  @Test
  void getTranslation_german_returnsLocalisedValue() {
    // German bundle has "general.folder = folder" (lowercase)
    String result = provider.getTranslation("general.folder", Locale.GERMAN);
    assertNotNull(result);
    assertFalse(result.isBlank());
  }

  @Test
  void getTranslation_german_languageKey_differentFromEnglish() {
    String en = provider.getTranslation("general.language", Locale.ENGLISH);
    String de = provider.getTranslation("general.language", Locale.GERMAN);
    // German has lowercase "language"; English has "Language"
    assertNotEquals(en, de, "German translation should differ from English");
  }

  // ---- getTranslation: fallback to English ----

  @Test
  void getTranslation_missingKeyInLocale_fallsBackToEnglish() {
    // Use a locale that exists in the provider but where a key may be absent
    // XincoMessages_cz.properties may not have all keys
    // If the key is present in English, we should get the English value or the locale value
    // Either way, not the key itself — because English IS the fallback
    String result = provider.getTranslation("general.folder", Locale.of("cz"));
    assertNotNull(result);
    assertFalse(result.isBlank(), "should not return empty for a key present in English");
    assertNotEquals(
        "general.folder", result, "should not return raw key when English fallback exists");
  }

  @Test
  void getTranslation_unknownLocale_fallsBackToEnglish() {
    // Klingon is not a provided locale — no bundle exists, falls back to English
    String result = provider.getTranslation("general.folder", Locale.of("tlh"));
    assertEquals("Folder", result, "unknown locale falls back to English bundle");
  }

  // ---- getTranslation: fallback to key ----

  @Test
  void getTranslation_missingKeyInAllBundles_returnsKey() {
    String key = "this.key.does.not.exist.anywhere";
    String result = provider.getTranslation(key, Locale.ENGLISH);
    assertEquals(key, result, "absent key should be returned as-is");
  }

  @Test
  void getTranslation_missingKeyInAllBundles_unknownLocale_returnsKey() {
    String key = "no.such.key.xyz";
    String result = provider.getTranslation(key, Locale.of("tlh"));
    assertEquals(key, result, "absent key with unknown locale should return key");
  }

  // ---- params (unused in current impl, should not crash) ----

  @Test
  void getTranslation_withParams_doesNotThrow() {
    assertDoesNotThrow(
        () -> provider.getTranslation("general.folder", Locale.ENGLISH, "param1", 42));
  }
}
