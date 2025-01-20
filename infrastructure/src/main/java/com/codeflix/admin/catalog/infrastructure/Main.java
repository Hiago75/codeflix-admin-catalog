package com.codeflix.admin.catalog.infrastructure;

import com.codeflix.admin.catalog.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        System.out.println(new UseCase().execute());
    }
}
