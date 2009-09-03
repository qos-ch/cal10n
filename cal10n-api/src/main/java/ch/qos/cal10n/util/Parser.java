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

import java.io.Reader;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author Ceki G&uuml;lc&uuml;
 *
 */
import ch.qos.cal10n.util.Token.TokenType;

// Given tokens k,s,v, \, and EOL
// ^ denoting the null token
// here is the grammar

// E = EOL | k s V |  E | ^
// V = v EOL | v '\' Vopt
// Vopt = EOL V

public class Parser {

  final Reader reader;
  final Map<String, String> map;

  List<Token> tokenList;
  int pointer = 0;

  Parser(Reader r, Map<String, String> map) {
    this.reader = r;
    this.map = map;
    TokenStream ts = new TokenStream(reader);
    tokenList = ts.tokenize();
  }

  void parseAndPopulate() {
    E();
  }

  private void E() {
    Token t = getNextToken();
    if (t == null) {
      // we are done
      return;
    }
    switch (t.tokenType) {
    case EOL:
      break;
    case KEY:
      String k = t.getValue();
      t = getNextToken();
      if (t.tokenType != TokenType.SEPARATOR) {
        throw new IllegalStateException("Unexpected token " + t);
      }
      StringBuilder buf = new StringBuilder();
      V(buf);
      map.put(k, buf.toString());
      break;
    }
    E();
  }

  // V = v EOL | v '\' Vopt
  // Vopt = EOL V
  private void V(StringBuilder buf) {

    Token t = getNextToken();
    if (t.tokenType != TokenType.VALUE) {
      throw new IllegalStateException("Unexpected token " + t);
    }
    buf.append(t.getValue());

    t = getNextToken();
    if (t.tokenType == TokenType.EOL) {
      return;
    } else if (t.tokenType == TokenType.TRAILING_BACKSLASH) {
      Vopt(buf);

    }
  }

  private void Vopt(StringBuilder buf) {
    Token t = getNextToken();
    if (t.tokenType != TokenType.EOL) {
      throw new IllegalStateException("Unexpected token " + t);
    }
    V(buf);
  }

  private Token getNextToken() {
    if (pointer < tokenList.size()) {
      return (Token) tokenList.get(pointer++);
    }
    return null;
  }

//  private Token getCurentToken() {
//    if (pointer < tokenList.size()) {
//      return (Token) tokenList.get(pointer);
//    }
//    return null;
//  }
//
//  private void advanceTokenPointer() {
//    pointer++;
//  }
}
