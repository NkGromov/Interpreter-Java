package AST;

import Token.Token;

public class VariableNode extends ExpressionNode {
  private Token variable;

  public VariableNode(Token variable) {
    super();
    this.variable = variable;
  }

  public Token getVariable() {
    return variable;
  }
}
