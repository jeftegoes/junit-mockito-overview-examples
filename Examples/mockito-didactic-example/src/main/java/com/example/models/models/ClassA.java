package com.example.models.models;

public class ClassA {
    private ClassB classB;
    private int y = 5;

    public ClassA(ClassB classB) {
        this.classB = classB;
    }

    public int sumValues() {
        int x = classB.getX();
        return this.y + x;
    }
}
