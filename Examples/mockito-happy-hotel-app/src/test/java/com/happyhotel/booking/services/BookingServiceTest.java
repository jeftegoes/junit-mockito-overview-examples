package com.happyhotel.booking.services;

import com.happyhotel.booking.BookingDAO;
import com.happyhotel.booking.BookingRequest;
import com.happyhotel.booking.MailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                LocalDateTime.now().toLocalDate().minusDays(3),
                LocalDateTime.now().toLocalDate(),
                1,
                true);
    }

    @Test
    void should_CalculateCorrectPrice_When_CorrectInput() {
        double expectedPrice = 150;

        double actual = this.bookingService.calculatePrice(bookingRequest);

        assertEquals(expectedPrice, actual);
    }
}
