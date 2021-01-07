package me.theseems.tomshelby.pack.order;

import me.theseems.tomshelby.pack.BotPackageInfo;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class GraphPackageOrderManager implements BotPackageOrderManager {

  private class Node {
    public BotPackageInfo botPackageInfo;
    public List<Node> dependency;

    public Node(BotPackageInfo botPackageInfo) {
      this.botPackageInfo = botPackageInfo;
      this.dependency = new ArrayList<>();
    }

    public BotPackageInfo getBotPackage() {
      return botPackageInfo;
    }

    public List<Node> getDependency() {
      return dependency;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Node node = (Node) o;
      return botPackageInfo.equals(node.botPackageInfo);
    }

    @Override
    public int hashCode() {
      return Objects.hash(botPackageInfo);
    }

    @Override
    public String toString() {
      return "Node{" + "botPackageInfo=" + botPackageInfo + ", dependency=" + dependency + '}';
    }
  }

  void traverse(
      Node from, Node to, BiFunction<Node, Node, Boolean> before, BiConsumer<Node, Node> after) {
    if (!before.apply(from, to)) return;

    for (Node child : to.getDependency()) {
      traverse(to, child, before, after);
    }

    after.accept(from, to);
  }

  /**
   * Put input packages in a correct order
   *
   * @param packages to order
   * @return order result
   */
  @Override
  public BotPackageOrderResult order(Collection<BotPackageInfo> packages) {
    Map<String, Node> nodeMap = new HashMap<>();
    BotPackageOrderResult result = new BotPackageOrderResult(new ArrayList<>(), new ArrayList<>());

    // Just putting it there to further make dependency connections
    for (BotPackageInfo botPackage : packages) {
      nodeMap.put(botPackage.getName(), new Node(botPackage));
    }

    for (BotPackageInfo botPackage : packages) {
      for (String dependency : botPackage.getDependencies()) {
        // Check if we can satisfy the dependency
        if (!nodeMap.containsKey(dependency)) {
          result.getConflicts().add(new DependencyNotFoundConflict(dependency, botPackage));
          continue;
        }

        // Setting a connection (edge) from dependency to it's dependent node
        nodeMap.get(botPackage.getName()).getDependency().add(nodeMap.get(dependency));
      }
    }

    // Used nodes so not to visit node twice
    Set<Node> used = new HashSet<>();

    // Traverse over all nodes there are
    for (Node value : nodeMap.values()) {
      Set<Node> currentStack = new HashSet<>();

      // Before children traversal function
      BiFunction<Node, Node, Boolean> before =
          (from, to) -> {
            boolean contains = used.contains(to);
            used.add(to);

            if (currentStack.contains(to)) {
              result
                  .getConflicts()
                  .add(
                      new CyclicDependencyConflict(
                          (from != null ? from.botPackageInfo : null), to.botPackageInfo));
              return false;
            }

            currentStack.add(from);
            return !contains;
          };

      // After children traversal function
      BiConsumer<Node, Node> after =
          (from, to) -> result.getOrderedPackages().add(to.botPackageInfo);

      traverse(null, value, before, after);
    }

    return result;
  }
}
