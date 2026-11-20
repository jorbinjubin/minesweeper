
package com.example;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class Main {

    public static void main(String[] args) throws Exception {

        Screen screen = new DefaultTerminalFactory()
                .createScreen();

        screen.startScreen();

        MultiWindowTextGUI gui =
                new MultiWindowTextGUI(screen);

        BasicWindow window = new BasicWindow("Minesweeper");

        Panel panel = new Panel();
        panel.setLayoutManager(
                new LinearLayout(Direction.VERTICAL)
        );

        panel.addComponent(
                new Label("Welcome to Lanterna!")
        );

        panel.addComponent(
                new Label("My first terminal GUI")
        );

        panel.addComponent(
                new Button("Exit", window::close)
        );

        window.setComponent(panel);

        gui.addWindowAndWait(window);

        screen.stopScreen();
    }
}
