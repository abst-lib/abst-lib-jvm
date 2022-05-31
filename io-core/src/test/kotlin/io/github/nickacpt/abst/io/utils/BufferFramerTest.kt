package io.github.nickacpt.abst.io.utils

import org.junit.jupiter.api.Test
import org.msgpack.core.MessagePack.Code
import org.msgpack.core.buffer.MessageBuffer
import kotlin.test.assertContentEquals

internal class BufferFramerTest {

    @Test
    fun `frame message`() {
        val bytes = byteArrayOf(1, 2, 3, 4, 5)
        val framed = BufferFramer.frame(bytes)

        assertContentEquals(byteArrayOf(Code.BIN8, bytes.size.toByte(), *bytes), framed)
    }

    @Test
    fun `unframe message`() {
        val expected = byteArrayOf(1, 2, 3, 4, 5)
        val bytes = byteArrayOf(Code.BIN8, expected.size.toByte(), *expected)

        val unframed = BufferFramer.unframe(MessageBuffer.wrap(bytes))

        assertContentEquals(expected, unframed)
    }

    @Test
    fun `can unframe framed`() {
        val expected = byteArrayOf(1, 2, 3, 4, 5)
        val framed = BufferFramer.frame(expected)
        val unframed = BufferFramer.unframe(MessageBuffer.wrap(framed))

        assertContentEquals(expected, unframed)
    }
}