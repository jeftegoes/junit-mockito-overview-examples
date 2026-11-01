package com.example.models;

import com.example.models.models.ClassA;
import com.example.models.models.ClassB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ClassATest {
    @InjectMocks
    ClassA classA;

    @Mock
    ClassB classB;

    @BeforeEach
    void setup() {

    }

    @Test
    void sumTwoValuesTest() {
        Mockito.when(classB.getX()).thenReturn(20);

        assertEquals(25, classA.sumValues());
    }
}