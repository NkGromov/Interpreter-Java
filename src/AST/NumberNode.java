package AST;

import Token.Token;

public class NumberNode extends ExpressionNode {
  private Token number;
  public NumberNode(Token number){
    this.number = number;
  }

  public Token getNumber() {
    return number;
  }
}
