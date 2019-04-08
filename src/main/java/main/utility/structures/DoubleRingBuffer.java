package main.utility.structures;

import com.google.common.util.concurrent.AtomicDoubleArray;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * custom implementation for a ring buffer, thanks to Xaanit for all the help
 */
public class DoubleRingBuffer  {

    private final int max;
    private int size;
    private final AtomicInteger pointer = new AtomicInteger(0);
    private final AtomicDoubleArray buffer;


    public DoubleRingBuffer() {
        this(30);
    }

    public DoubleRingBuffer(final int max) {
        buffer = new AtomicDoubleArray(max);
        this.max = max;
    }

    public double[] push(double element) {
        if(full()) shift();
        buffer.set(size == max ? max - 1 : size, element);
        if(!full()) size++;
        return toArray();
    }

    public double pop() {
        if(pointer.get() == max || pointer.get() == size()) {
            pointer.set(0);
        }
        return buffer.get(pointer.getAndIncrement());
    }

    public int size() {
        return size;
    }

    public boolean full() {
        return size() == max;
    }

    private void shift() {
        pointer.set(pointer.get() == 0 ? 0 : pointer.get() - 1);
        for(int i = 1; i < size; i++) {
            buffer.set(i - 1, buffer.get(i));
        }
    }

    public double[] toArray() {
        double[] arr = new double[size];
        for(int i = 0; i < size; i++) {
            arr[i] = buffer.get(i);
        }
        return arr;
    }
}