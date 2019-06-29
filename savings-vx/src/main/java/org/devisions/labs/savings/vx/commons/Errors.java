package org.devisions.labs.savings.vx.commons;

/**
 * Standard error codes dictionary.
 *
 * @author devisions
 */
public enum Errors {

  /** the operation is unknown */
  UNKNOWN_REQUEST(0, "The request is unknown."),

  /** a database related error */
  INTERNAL_ERROR(1, "An internal error was encountered.");

  public final int code;

  public final String message;

  /** internal c'tor used for adding details to the enum elements */
  Errors(int code, String message) {
    this.code = code;
    this.message = message;
  }

}
