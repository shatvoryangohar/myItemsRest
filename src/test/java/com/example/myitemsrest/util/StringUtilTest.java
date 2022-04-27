package com.example.myitemsrest.util;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
class StringUtilTest {

    private StringUtil stringUtil = Mockito.mock(StringUtil.class);

    @Test
    void trim() {
       Mockito.when(stringUtil.trim("  poxox ")).thenReturn("poxos");
        String trim = stringUtil.trim("  poxos ");
        assertEquals("poxos", trim);
    }

    @Test
    void trim_null() {
        String trim = stringUtil.trim(null);
        assertNotNull(trim);
        assertEquals("", trim);
    }

    @Test
    void trim_ok() {
        String trim = stringUtil.trim("po xos");
        assertEquals("po xos", trim);
    }

    @Test
    void revert() {
        String name = "poxos";
        String revertedName = stringUtil.revert(name);
        assertEquals("soxop", revertedName);
    }

    @Test
    void revert_null() {
        String revert = stringUtil.revert(null);
        assertNull(revert);
    }
}