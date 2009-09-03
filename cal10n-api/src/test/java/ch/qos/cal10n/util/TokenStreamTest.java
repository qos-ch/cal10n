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

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.qos.cal10n.Cal10nTestConstants;
import ch.qos.cal10n.util.Token.TokenType;

/**
 * 
 * @author Ceki G&uuml;lc&uuml;
 *
 */
public class TokenStreamTest {

  
  @Test
  public void smoke() throws FileNotFoundException {
    FileReader fr = new FileReader(Cal10nTestConstants.TEST_CLASSES+"/parser/smoke.properties");
    TokenStream ts = new TokenStream(fr);
    List<Token> tokenList = ts.tokenize();
    
    List<Token> witness = new ArrayList<Token>();
    witness.add(new Token(TokenType.KEY, "K0"));
    witness.add(new Token(TokenType.SEPARATOR, "="));
    witness.add(new Token(TokenType.VALUE, "V0"));
    witness.add(Token.EOL);

    witness.add(new Token(TokenType.KEY, "K1"));
    witness.add(new Token(TokenType.SEPARATOR, "="));
    witness.add(new Token(TokenType.VALUE, "V1"));
    witness.add(Token.EOL);
    assertEquals(witness, tokenList);
  }
  
  @Test
  public void medium() throws FileNotFoundException {
    FileReader fr = new FileReader(Cal10nTestConstants.TEST_CLASSES+"/parser/medium.properties");
    TokenStream ts = new TokenStream(fr);
    List<Token> tokenList = ts.tokenize();
    
   // K0=V0 \
   //  X
   // # comment
   // K1=V1
    
    
    List<Token> witness = new ArrayList<Token>();
    witness.add(new Token(TokenType.KEY, "K0"));
    witness.add(new Token(TokenType.SEPARATOR, "="));
    witness.add(new Token(TokenType.VALUE, "V0 "));
    witness.add(Token.TRAILING_BACKSLASH);
    witness.add(Token.EOL);
    witness.add(new Token(TokenType.VALUE, "X"));
    witness.add(Token.EOL);
    witness.add(Token.EOL);
    
    witness.add(new Token(TokenType.KEY, "K1"));
    witness.add(new Token(TokenType.SEPARATOR, "="));
    witness.add(new Token(TokenType.VALUE, "V1"));
    witness.add(Token.EOL);
    assertEquals(witness, tokenList);
  }

}
