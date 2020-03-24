package com.mydomain.mock_list;

import com.mydomain.Calculator;
import com.mydomain.sut_and_collaborator.Collaborator;
import com.mydomain.sut_and_collaborator.MySystemUnderTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockListTest {

    @Mock
    List<Integer> integerList;

    @Spy
    MySystemUnderTest systemUnderTest = new MySystemUnderTest(new Collaborator());

    @Captor
    ArgumentCaptor<String> captor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mockListWithoutAnnotations() throws Exception {
        /* given */
        final List mockedList = mock(List.class);

        when(mockedList.get(2)).thenReturn(2);
        given(mockedList.get(3)).willReturn(3);
        given(mockedList.size()).willReturn(1);

        mockedList.add(2);
        mockedList.add(3);
        mockedList.add(4);
        mockedList.add(5);

        /* when */
        final int listSize = mockedList.size();

        /* then */

        final MockingDetails mockingDetails = BDDMockito.mockingDetails(mockedList);
        assertTrue(mockingDetails.getInvocations().size() == 5);
        assertTrue(listSize == 1);
    }

    @Test
    public void useInjectedMock() throws Exception {
        given(integerList.size()).willReturn(1, 2, 3);
        integerList.add(3);
        assertTrue(integerList.size() == 1);
        integerList.add(4);
        assertTrue(integerList.size() == 2);
        integerList.add(5);
        assertTrue(integerList.size() == 3);
    }

    @Test
    public void testArguments() throws Exception {
        final String someString = "some string";
        final String notMe = "not me";
        final List<String> mockedList = (List<String>) mock(List.class);

        given(mockedList.add(someString)).willReturn(true);
        given(mockedList.add(Matchers.startsWith("can't add"))).willReturn(false);
        given(mockedList.add(Matchers.eq(notMe))).willReturn(false);

        assertTrue(mockedList.add(someString));
        assertFalse(mockedList.add("can't add 1"));
        assertFalse(mockedList.add("can't add 2"));
    }

    @Test
    public void stubVoidMethods() throws Exception {
        List<String> list = ((List<String>) mock(List.class));

        BDDMockito.willDoNothing().given(list).clear();
        BDDMockito.willThrow(Exception.class).given(list).clear();

        Mockito.doNothing().when(list).clear();
        Mockito.doThrow(Exception.class).when(list).clear();
    }

    @Test
    public void verificationExample() throws Exception {
        final Calculator calculator = mock(Calculator.class);
        verify(calculator, Mockito.never()).add(anyLong(), anyLong());
        calculator.divide(2, 5);
        verify(calculator, times(1)).divide(2, 5);
        verify(calculator, atMost(0)).multiply(anyLong(), anyLong());
        verify(calculator, never()).add(anyLong(), anyLong());
        Mockito.verifyZeroInteractions(calculator);
    }

    @Test
    public void argumentCaptor() throws Exception {
        final Collaborator collaborator = mock(Collaborator.class);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        final boolean result = new MySystemUnderTest(collaborator).doSomeStuff("my param");
        verify(collaborator).doStuff(captor.capture());
        assertTrue(captor.getValue().equals("my param"));
        assertFalse(result);
    }

    @Test
    public void spyExample() throws Exception {
        final MySystemUnderTest realSUT = new MySystemUnderTest(new Collaborator());
        final MySystemUnderTest spySUT = Mockito.spy(realSUT);
        final String argument = "Argument";
        doReturn(false).when(spySUT).doSomeStuff(argument);
        final boolean result = spySUT.doSomeStuff(argument);
        assertFalse(result);
        verify(spySUT).doSomeStuff(argument);
    }
}
