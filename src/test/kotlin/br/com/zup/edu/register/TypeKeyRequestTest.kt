package br.com.zup.edu.register

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TypeKeyRequestTest {

    @Nested
    inner class KeyRandomTest {
        @Test
        internal fun `must be valid when random key was null or empty`() {
            val typeKey = TypeKeyRequest.RANDOM

            assertTrue(typeKey.validate(null))
            assertTrue(typeKey.validate(""))
        }

        @Test
        internal fun `must not be valid when random key has value`() {
            val typeKey = TypeKeyRequest.RANDOM

            assertFalse(typeKey.validate("some value"))
        }
    }

    @Nested
    inner class KeyCpfTest {
        @Test
        internal fun `must be valid when cpf was valid number`() {
            val typeKey = TypeKeyRequest.CPF

            assertTrue(typeKey.validate("12312320820"))
        }

        @Test
        internal fun `must not be valid when cpf was invalid number`() {
            val typeKey = TypeKeyRequest.CPF

            assertFalse(typeKey.validate("123"))
            assertFalse(typeKey.validate("some string"))
        }

        @Test
        internal fun `must not be valid when cpf wont informed`() {
            val typeKey = TypeKeyRequest.CPF

            assertFalse(typeKey.validate(null))
            assertFalse(typeKey.validate(""))
        }
    }

    @Nested
    inner class KeyPhoneTest {
        @Test
        internal fun `must be valid when phone was valid number`() {
            val typeKey = TypeKeyRequest.PHONE

            assertTrue(typeKey.validate("+55912441348"))
        }

        @Test
        internal fun `must not be valid when phone was invalid number`() {
            val typeKey = TypeKeyRequest.PHONE

            assertFalse(typeKey.validate("55912441348"))
            assertFalse(typeKey.validate("+55s12441348"))
        }

        @Test
        internal fun `must not be valid when phone wont informed`() {
            val typeKey = TypeKeyRequest.PHONE

            assertFalse(typeKey.validate(null))
            assertFalse(typeKey.validate(""))
        }
    }

    @Nested
    inner class KeyEmailTest {
        @Test
        internal fun `must be valid when email was valid`() {
            val typeKey = TypeKeyRequest.EMAIL

            assertTrue(typeKey.validate("foo@mail.com"))
        }

        @Test
        internal fun `must not be valid when email was invalid`() {
            val typeKey = TypeKeyRequest.EMAIL

            assertFalse(typeKey.validate("foomailcom"))
            assertFalse(typeKey.validate("@"))
        }

        @Test
        internal fun `must not be valid when email wont informed`() {
            val typeKey = TypeKeyRequest.EMAIL

            assertFalse(typeKey.validate(null))
            assertFalse(typeKey.validate(""))
        }
    }

}