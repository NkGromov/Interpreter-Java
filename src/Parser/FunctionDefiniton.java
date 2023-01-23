package Parser;

import java.util.ArrayList;
import java.util.List;

import AST.ExpressionNode;
import AST.PrototypeNode;

public class FunctionDefiniton {
  private List<ExpressionNode> body = new ArrayList<>();
  private PrototypeNode proto;

  public FunctionDefiniton(List<ExpressionNode> body, PrototypeNode proto) {
    this.body = body;
    this.proto = proto;
  }

  public List<ExpressionNode> getBody() {
    return body;
  }

  public PrototypeNode getProto() {
    return proto;
  }
}
