package me.theseems.tomshelby.bootstrap.builtin;

import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.PseudoText;
import com.indvd00m.ascii.render.elements.Rectangle;
import com.indvd00m.ascii.render.elements.Text;
import me.theseems.tomshelby.Main;
import me.theseems.tomshelby.bootstrap.InitBootstrap;
import org.apache.logging.log4j.Logger;

public class ConsoleInformationBootstrap implements InitBootstrap {

  private void printAsciiText(String text, int height, int width) {
    IRender render = new Render();
    IContextBuilder builder = render.newBuilder().width(width).height(height);

    builder.element(new PseudoText(text, false));
    ICanvas canvas = render.render(builder.build());
    System.out.println(canvas.getText());
  }

  private void printAsciiBox(String text, int offsetX, int height, int width) {
    IRender render = new Render();
    IContextBuilder builder = render.newBuilder().width(width + offsetX + 5).height(height);

    builder.element(new Rectangle(offsetX, 0, width, height));
    builder.element(new Text(text, offsetX, height / 2, width, height));

    ICanvas canvas = render.render(builder.build());
    System.out.println(canvas.getText());
  }

  @Override
  public void apply(Logger logger) {
    System.out.println();
    printAsciiText("TomShelbyBot", 10, 100);
    printAsciiBox(Main.TOM_BOT_VERSION, 15, 5, 30);
    System.out.println();
  }
}
