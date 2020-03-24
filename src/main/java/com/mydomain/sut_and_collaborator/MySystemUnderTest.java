package com.mydomain.sut_and_collaborator;

public class MySystemUnderTest {
    private Collaborator collaborator;

    public MySystemUnderTest(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public boolean doSomeStuff(String withArgument) {
        return collaborator.doStuff(withArgument);
    }
}
