/* Generated By:JJTree&JavaCC: Do not edit this line. SimplePCREConstants.java */
package parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface SimplePCREConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int NUMBER = 4;
  /** RegularExpression Id. */
  int IDENTIFIER = 5;
  /** RegularExpression Id. */
  int LETTER = 6;
  /** RegularExpression Id. */
  int DIGIT = 7;
  /** RegularExpression Id. */
  int COMMA = 8;
  /** RegularExpression Id. */
  int UNION = 9;
  /** RegularExpression Id. */
  int STAR = 10;
  /** RegularExpression Id. */
  int PLUS = 11;
  /** RegularExpression Id. */
  int OPTIONAL = 12;
  /** RegularExpression Id. */
  int ANY = 13;
  /** RegularExpression Id. */
  int OPEN1 = 14;
  /** RegularExpression Id. */
  int CLOSE1 = 15;
  /** RegularExpression Id. */
  int OPEN2 = 16;
  /** RegularExpression Id. */
  int CLOSE2 = 17;
  /** RegularExpression Id. */
  int QUOTE = 18;
  /** RegularExpression Id. */
  int LF = 19;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "<NUMBER>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "\",\"",
    "\"|\"",
    "\"*\"",
    "\"+\"",
    "\"?\"",
    "\".\"",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"\\\"\"",
    "\"\\n\"",
  };

}
