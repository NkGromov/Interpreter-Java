package Parser;

import java.util.ArrayList;
import java.util.List;

import AST.ExpressionNode;

public class FunctionDefiniton {
  private List<ExpressionNode> body = new ArrayList<>();

  public FunctionDefiniton(List<ExpressionNode> body) {
    this.body = body;
  }

  public List<ExpressionNode> getBody() {
    return body;
  }
}
