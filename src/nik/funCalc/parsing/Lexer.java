package nik.funCalc.parsing;

import java.io.*;
import java.util.*;

/**
 * @author nik
 */
public class Lexer {
  private static final Map<Character, TokenType> SYMBOLS = new HashMap<Character, TokenType>();
  static {
    SYMBOLS.put('+', TokenType.PLUS);
    SYMBOLS.put('-', TokenType.MINUS);
    SYMBOLS.put('*', TokenType.MULT);
    SYMBOLS.put('/', TokenType.DIV);
    SYMBOLS.put('(', TokenType.LPAREN);
    SYMBOLS.put(')', TokenType.RPAREN);
    SYMBOLS.put(',', TokenType.COMMA);
    SYMBOLS.put(';', TokenType.SEMICOLON);
    SYMBOLS.put('=', TokenType.ASSIGN);
    SYMBOLS.put('{', TokenType.LBRACE);
    SYMBOLS.put('}', TokenType.RBRACE);
  }
  private PushbackReader myReader;
  private String myToken;
  private TokenType myTokenType;
  private Stack<TokenType> myTypeBuffer = new Stack<TokenType>();
  private Stack<String> myTokenBuffer = new Stack<String>();

  public Lexer(Reader reader) {
    myReader = new PushbackReader(reader);
  }

  public TokenType nextToken() {
    if (!myTokenBuffer.isEmpty()) {
      myTokenType = myTypeBuffer.pop();
      myToken = myTokenBuffer.pop();
      return myTokenType;
    }
    try {
      int ch = myReader.read();
      while (ch != -1 && Character.isWhitespace(ch)) {
        ch = myReader.read();
      }

      if (ch == -1) {
        return token("", TokenType.EOF);
      }

      if (SYMBOLS.containsKey((char) ch)) {
        return token(String.valueOf((char) ch), SYMBOLS.get((char)ch));
      }

      if ('0' <= ch && ch <= '9') {
        StringBuilder buffer = new StringBuilder();
        buffer.append((char) ch);
        while ((ch = myReader.read()) != -1 && '0' <= ch && ch <= '9') {
          buffer.append((char) ch);
        }
        if (ch != -1) {
          myReader.unread(ch);
        }
        return token(buffer.toString(), TokenType.INT);
      }
      if (Character.isJavaIdentifierStart(ch)) {
        StringBuilder buffer = new StringBuilder();
        buffer.append((char) ch);
        while ((ch = myReader.read()) != -1 && Character.isJavaIdentifierPart(ch)) {
          buffer.append((char) ch);
        }
        if (ch != -1) {
          myReader.unread(ch);
        }
        return token(buffer.toString(), TokenType.IDENTIFIER);
      }
      throw new ParsingException("Invalid symbol: "+(char) ch);
    }
    catch (IOException e) {
      throw new ParsingException(e);
    }
  }

  public TokenType getTokenType() {
    return myTokenType;
  }

  private TokenType token(String token, final TokenType type) {
    myToken = token;
    myTokenType = type;
    return type;
  }

  public String getToken() {
    return myToken;
  }

  public void pushBack() {
    pushBack(myToken, myTokenType);
  }

  public void pushBack(final String token, final TokenType tokenType) {
    myTokenBuffer.push(token);
    myTypeBuffer.push(tokenType);
  }
}
