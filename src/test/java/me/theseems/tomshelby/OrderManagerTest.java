package me.theseems.tomshelby;

import me.theseems.tomshelby.pack.BotPackageInfo;
import me.theseems.tomshelby.pack.order.BotPackageOrderResult;
import me.theseems.tomshelby.pack.order.CyclicDependencyConflict;
import me.theseems.tomshelby.pack.order.GraphPackageOrderManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManagerTest {

  private static Map<String, TestPackageInfo> packageInfoMap = new HashMap<>();

  static class TestPackageInfo implements BotPackageInfo {
    private final String name;
    private final List<String> dependencies;
    private boolean isOn;

    public TestPackageInfo(String name, List<String> dependencies) {
      this.name = name;
      this.dependencies = dependencies;
    }

    public TestPackageInfo(String name, String... dependencies) {
      this.name = name;
      this.dependencies = Arrays.asList(dependencies);
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getAuthor() {
      return "testAuthor";
    }

    @Override
    public String getVersion() {
      return "testVersion";
    }

    @Override
    public String getDescription() {
      return "testDescription";
    }

    @Override
    public List<String> getDependencies() {
      return dependencies;
    }

    @Override
    public String toString() {
      return "TestPackageInfo{" + "name='" + name + '\'' + ", dependencies=" + dependencies + '}';
    }

    void turnOn() {
      packageInfoMap.put(name, this);
      for (String dependency : getDependencies()) {
        if (!packageInfoMap.containsKey(dependency) || !packageInfoMap.get(dependency).isOn)
          throw new IllegalStateException(
              "Dependency of '" + name + "' under the name '" + dependency + "' is not satisfied!");
      }
      this.isOn = true;
    }
  }

  @Test
  public void simpleOrderManager_simpleTest() {
    // ⇗ A ⇖
    // B    C

    TestPackageInfo a = new TestPackageInfo("A");
    TestPackageInfo b = new TestPackageInfo("B", "A");
    TestPackageInfo c = new TestPackageInfo("C", "A");

    GraphPackageOrderManager manager = new GraphPackageOrderManager();
    BotPackageOrderResult result = manager.order(Arrays.asList(a, b, c));

    Assertions.assertTrue(result.getConflicts().isEmpty());
    for (BotPackageInfo orderedPackage : result.getOrderedPackages()) {
      TestPackageInfo info = (TestPackageInfo) orderedPackage;
      info.turnOn();
    }
  }

  @Test
  public void simpleOrderManager_cyclicTest1() {
    // A
    // ⇵
    // B

    TestPackageInfo a = new TestPackageInfo("A", "B");
    TestPackageInfo b = new TestPackageInfo("B", "A");

    GraphPackageOrderManager manager = new GraphPackageOrderManager();
    BotPackageOrderResult result = manager.order(Arrays.asList(a, b));

    Assertions.assertFalse(result.getConflicts().isEmpty());
    Assertions.assertTrue(
        result.getConflicts().iterator().next() instanceof CyclicDependencyConflict);
  }

  @Test
  public void simpleOrderManager_cyclicTest2() {
    // ⇗ A ⇖
    // B ⇒ C

    TestPackageInfo a = new TestPackageInfo("A");
    TestPackageInfo b = new TestPackageInfo("B", "A", "C");
    TestPackageInfo c = new TestPackageInfo("C", "A");

    GraphPackageOrderManager manager = new GraphPackageOrderManager();
    BotPackageOrderResult result = manager.order(Arrays.asList(a, b, c));

    Assertions.assertTrue(result.getConflicts().isEmpty());
    for (BotPackageInfo orderedPackage : result.getOrderedPackages()) {
      TestPackageInfo info = (TestPackageInfo) orderedPackage;
      info.turnOn();
    }
  }

  @Test
  public void simpleOrderManager_cyclicTest3() {
    // ⇙ A ⇖
    // B ⇒ C

    TestPackageInfo a = new TestPackageInfo("A", "B");
    TestPackageInfo b = new TestPackageInfo("B", "C");
    TestPackageInfo c = new TestPackageInfo("C", "A");

    GraphPackageOrderManager manager = new GraphPackageOrderManager();
    BotPackageOrderResult result = manager.order(Arrays.asList(a, b, c));

    Assertions.assertFalse(result.getConflicts().isEmpty());
    Assertions.assertTrue(
        result.getConflicts().iterator().next() instanceof CyclicDependencyConflict);
  }

  @Test
  public void simpleOrderManager_cyclicTest4() {
    // Picture can be found here: https://i.imgur.com/twp97KX.png
    TestPackageInfo one = new TestPackageInfo("1");
    TestPackageInfo two = new TestPackageInfo("2", "1");
    TestPackageInfo three = new TestPackageInfo("3", "1", "2");
    TestPackageInfo four = new TestPackageInfo("4", "2", "3");
    TestPackageInfo five = new TestPackageInfo("5", "3");

    GraphPackageOrderManager manager = new GraphPackageOrderManager();
    BotPackageOrderResult result = manager.order(Arrays.asList(one, two, three, four, five));

    Assertions.assertTrue(result.getConflicts().isEmpty());
    for (BotPackageInfo orderedPackage : result.getOrderedPackages()) {
      TestPackageInfo info = (TestPackageInfo) orderedPackage;
      info.turnOn();
    }
  }
}
