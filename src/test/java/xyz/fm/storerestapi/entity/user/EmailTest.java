package xyz.fm.storerestapi.entity.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void emailEncrypt_1() {
        Email email = new Email("abcde123@domain.com");
        assertEquals("abcd****@domain.com", email.encrypt());
    }

    @Test
    void emailEncrypt_2() {
        Email email = new Email("abc@domain.com");
        assertEquals("a**@domain.com", email.encrypt());
    }

}