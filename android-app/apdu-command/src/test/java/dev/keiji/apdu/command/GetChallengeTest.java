package dev.keiji.apdu.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class GetChallengeTest {

    private final Random rand = new Random();

    @Test
    public void test1() {
        byte[] expected = new byte[]{0x00, (byte) 0xB4, (byte) 0x00, (byte) 0x00, 0x08};

        GetChallenge command = new GetChallenge(
                0x00, 8
        );
        byte[] actual = command.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test2() {
        byte[] expected = new byte[]{0x00, (byte) 0xB4, (byte) 0x00, (byte) 0x00, (byte) 0xFF};

        GetChallenge command = new GetChallenge(
                0x00, 255
        );
        byte[] actual = command.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test2_exception0() {
        try {
            GetChallenge command = new GetChallenge(
                    0x00, 256
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test2_exception1() {
        try {
            GetChallenge command = new GetChallenge(
                    0x00, 0
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test2_exception2() {
        try {
            GetChallenge command = new GetChallenge(
                    0x00, -1
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
