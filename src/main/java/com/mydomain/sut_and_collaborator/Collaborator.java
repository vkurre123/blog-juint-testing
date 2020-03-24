package com.mydomain.sut_and_collaborator;

public class Collaborator {
    public boolean doStuff(String withArgument) {
        return withArgument.contains("some-thing");
    }
}
