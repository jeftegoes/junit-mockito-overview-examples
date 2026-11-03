package com.happyhotel.booking.services;

import com.happyhotel.booking.BookingDAO;
import com.happyhotel.booking.BookingRequest;
import com.happyhotel.booking.BusinessException;
import com.happyhotel.booking.MailSender;
import com.happyhotel.booking.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RoomService roomService;

    @Mock
    private BookingDAO bookingDAO;

    @Mock
    private MailSender mailSender;

    BookingRequest bookingRequest;

    @BeforeEach
    void setup() {
        this.bookingRequest = new BookingRequest("1",
                LocalDate.of(2026, Month.JANUARY, 7),
                LocalDate.of(2026, Month.JANUARY, 10),
                1,
                true);
    }

    @Test
    void should_CalculateCorrectPrice_When_CorrectInput() {
        double expectedPrice = 150;

        double actual = this.bookingService.calculatePrice(bookingRequest);

        assertEquals(expectedPrice, actual);
    }

    @Test
    void should_CountAvailablePlaces_When_OneRoomAvailable() {
        int expected = 2;

        List<Room> rooms = List.of(
                new Room("1", 2)
        );

        when(this.roomService.getAvailableRooms()).thenReturn(rooms);

        int actual = this.bookingService.getAvailablePlaceCount();

        assertEquals(expected, actual);
    }

    @Test
    void should_CountAvailablePlaces_When_MultipleRoomsAvailable() {
        int expectedFirstCall = 10;
        int expectedSecondCall = 5;
        int expectedThirdCall = 0;

        when(this.roomService.getAvailableRooms())
                .thenReturn(Collections.singletonList(new Room("1", 10)))
                .thenReturn(Collections.singletonList(new Room("1", 5)))
                .thenReturn(List.of());

        int actualFirst = this.bookingService.getAvailablePlaceCount();
        int actualSecond = this.bookingService.getAvailablePlaceCount();
        int actualThird = this.bookingService.getAvailablePlaceCount();

        assertAll(
                () -> assertEquals(expectedFirstCall, actualFirst),
                () -> assertEquals(expectedSecondCall, actualSecond),
                () -> assertEquals(expectedThirdCall, actualThird)
        );
    }

    @Test
    void should_ThrowException_When_NoRoomAvailable() {
        when(roomService.findAvailableRoomId(any(BookingRequest.class))).thenThrow(BusinessException.class);

        Executable executable = () -> this.bookingService.makeBooking(bookingRequest);

        assertThrows(BusinessException.class, executable);
    }
}