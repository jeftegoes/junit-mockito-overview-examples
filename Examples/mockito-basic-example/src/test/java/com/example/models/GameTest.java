package com.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GameTest {
    @InjectMocks
    Game game;

    @Mock
    Player player;

    @BeforeEach
    void setup() {

    }

    @Test
    void gameWeaponAttackTest() {
        Mockito.when(player.getWeapon()).thenReturn("Sword");

        assertEquals("Player attack with: Sword", game.attack());
    }
}