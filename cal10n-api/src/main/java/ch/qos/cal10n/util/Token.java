/*
 * Copyright (c) 2009 QOS.ch All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS  IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.qos.cal10n.util;

/**
 * 
 * @author Ceki G&uuml;lc&uuml;
 *
 */
public class Token {

  final static Token EOL = new Token(TokenType.EOL);
  final static Token TRAILING_BACKSLASH = new Token(TokenType.TRAILING_BACKSLASH);
  
  enum TokenType {
    KEY,
    SEPARATOR,
    VALUE,
    TRAILING_BACKSLASH,
    EOL;
  }
  
  final TokenType tokenType;
  final String value;
  

  Token(TokenType tokenType) {
    this(tokenType, null);
  }
  
  Token(TokenType tokenType, String value) {
    this.tokenType = tokenType;
    this.value = value;
  }
  
  
  public TokenType getTokenType() {
    return tokenType;
  }

  public String getValue() {
    return value;
  }

  public String toString() {
    if (value == null) {
      return "Token(" + tokenType + ")";
    } else {
      return "Token(" + tokenType + ", \"" + value + "\")";
    }
  }

  @Override
  public int hashCode() {
    return tokenType.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Token other = (Token) obj;
    
    if (!tokenType.equals(other.tokenType))
      return false;
    
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }
  
  
}
