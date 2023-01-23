package AST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Parser.FunctionDefiniton;

public class FnCallNode extends ExpressionNode {
  private String name;
  private List<Integer> arguments = new ArrayList<>();

  public FnCallNode(String name, List<Integer> arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  @Override
  public Integer applyNode(Map<String, Integer> scope, Map<String, FunctionDefiniton> fnDefinitions) {
    Integer result = 0;
    List<ExpressionNode> body = fnDefinitions.get(name).getBody();
    List<String> definitionArguments = fnDefinitions.get(name).getProto().getArguments();
    for (int i = 0; i < arguments.size(); i++) {
      scope.put(definitionArguments.get(i), arguments.get(i));
    }
    for (ExpressionNode node : body)
      result = node.applyNode(scope);

    return result;
  }

  public List<Integer> getArguments() {
    return arguments;
  }

  public String getName() {
    return name;
  }

}
