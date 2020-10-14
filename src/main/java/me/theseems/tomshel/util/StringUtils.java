package me.theseems.tomshel.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {
  public static String[] skipOne(String[] original) {
    List<String> stringList =
        new ArrayList<>(Arrays.asList(original).subList(1, original.length));
    return stringList.toArray(new String[]{});
  }
}
