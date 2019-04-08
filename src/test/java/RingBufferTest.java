import main.utility.structures.DoubleRingBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RingBufferTest {
    private DoubleRingBuffer buffer;

    @BeforeEach
    void beforeEach() {
        buffer = new DoubleRingBuffer(10);
    }

    @Test
    void push3() {
        buffer.push(1);
        buffer.push(2);
        buffer.push(3);

        assertEquals(3, buffer.size());
    }

    @Test
    void push11() {
        for (int i = 0; i < 11; i++) buffer.push(i);

        assertEquals(10, buffer.size());

        for (int i = 1; i < 11; i++) {
            assertEquals(i, buffer.pop());
        }
    }
}
