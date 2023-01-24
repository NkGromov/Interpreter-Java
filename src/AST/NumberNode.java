package AST;

import java.util.Map;

import Parser.FunctionDefiniton;
import Token.Token;

public class NumberNode extends ExpressionNode {
  private Token number;
  public NumberNode(Token number){
    this.number = number;
  }
  
  @Override
  public Integer applyNode(Map<String, Integer> scope, Map<String, FunctionDefiniton> fnDefinitions){
    return Integer.parseInt(this.number.getText());
  }

  public Token getNumber() {
    return number;
  }
}
