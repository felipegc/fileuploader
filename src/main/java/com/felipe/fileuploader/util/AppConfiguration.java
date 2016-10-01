package com.felipe.fileuploader.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppConfiguration {

  private static ResourceBundle bundle = ResourceBundle.getBundle("appsets");
  private static final Logger log = Logger.getLogger(AppConfiguration.class.getName());

  public static String get(String messageKey, Object... params) {
    String message = getMessageBundle(messageKey);
    return MessageFormat.format(message, params);
  }

  private static ResourceBundle getBundle() {
    return bundle;
  }

  private static String getMessageBundle(String keyMessage) {
    String message;
    try {
      message = getBundle().getString(keyMessage);
    } catch (MissingResourceException mre) {
      log.log(Level.SEVERE, "Could not find the key message.", mre);
      message = getBundle().getString("error.internal_error_message");
    }
    return message;
  }
}
