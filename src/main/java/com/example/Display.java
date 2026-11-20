package com.example;

import java.io.*;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.w3c.dom.Text;

public class Display {
    public static void main(String[] args) throws IOException {
        try (Screen sc = new DefaultTerminalFactory().createScreen()) {
            sc.startScreen();

            sc.clear();
            int cX = 0, cY = 0; // NOTE: Remember to convert between xy and rc
            Game g = new Game(20, 20);
            boolean isInitialized = false;
            while (!g.end) {
                KeyStroke key = sc.readInput();
                if(key != null) {
                    char c = key.getCharacter();
                    switch(c) {
                        case 'h':
                            cX--;
                            break;
                        case 'j':
                            cY++;
                            break;
                        case 'k':
                            cY--;
                            break;
                        case 'l':
                            cX++;
                            break;
                        case 'f':
                            // TODO: Flag selected cell
                            break;
                        case ' ':
                            if(!isInitialized) {
                                g.init(cY, cX);
                                isInitialized = true;
                            } else {
                                g.uncover(cY, cX);
                            }
                            break;
                    }
                }
                // Print char[][] with cursor pos
                printBoard(g, sc, cX, cY);
                sc.refresh();
            }
            sc.refresh();
        }

    }

    public static void printBoard(Game g, Screen sc, int cX, int cY) {
        char[][] board = g.getBoard();
        TextGraphics tg = sc.newTextGraphics();
        for(int i = 0; i < g.h; i++) {
            for(int j = 0; j < g.w; j++) {
                if(j == cX && i == cY) {
                    tg.setForegroundColor(TextColor.ANSI.BLACK);
                    tg.setBackgroundColor(TextColor.ANSI.WHITE);
                } else {
                    tg.setForegroundColor(TextColor.ANSI.WHITE);
                    tg.setBackgroundColor(TextColor.ANSI.BLACK);
                }
                if(board[i][j] == 'B') {
                    tg.setBackgroundColor(TextColor.ANSI.RED);
                }
                tg.putString(j, i, board[i][j] + "");
            }
        }
    }
}