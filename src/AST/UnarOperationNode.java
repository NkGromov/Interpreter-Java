package AST;

import Token.Token;

public class UnarOperationNode extends ExpressionNode {
  private Token operator;
  private ExpressionNode operand;

  public UnarOperationNode(Token operator, ExpressionNode operand){
    this.operator = operator;
    this.operand = operand;
  }

  public ExpressionNode getOperand() {
    return operand;
  }

  public Token getOperator() {
    return operator;
  }
  
}
