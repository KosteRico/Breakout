package com.kosterico.screens;

import com.badlogic.gdx.Screen;

import java.util.Stack;

public class ScreenManager {

    private Stack<Screen> stackScreens;

    public ScreenManager() {
        this.stackScreens = new Stack<Screen>();
    }

    public void push(Screen s) {
        stackScreens.push(s);
    }

    Screen pop() {
        Screen r = stackScreens.peek();
        r.dispose();
        stackScreens.pop();
        return r;
    }

    public Screen getPeek () {
        return stackScreens.peek();
    }

    public void set(Screen s) {
        pop();
        stackScreens.push(s);
    }
}
