package AST;

import java.util.Map;

import Token.Token;

public class VariableNode extends ExpressionNode {
  private Token variable;

  public VariableNode(Token variable) {
    super();
    this.variable = variable;
  }
  
  @Override
  public Integer applyNode(Map<String, Integer> scope){
    return scope.get(this.variable.getText());
  }

  public Token getVariable() {
    return variable;
  }
}
