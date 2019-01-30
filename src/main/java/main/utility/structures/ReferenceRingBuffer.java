package main.utility.structures;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ReferenceRingBuffer<T> {

    private final int max;
    private int size;
    private final AtomicInteger pointer = new AtomicInteger(0);
    private final AtomicReferenceArray<T> buffer;

    public ReferenceRingBuffer(final int max) {
        buffer = new AtomicReferenceArray<>(max);
        this.max = max;
    }


    public T[] push(T element) {
        if(full()) shift();
        buffer.set(size == max ? max - 1 : size, element);
        if(!full()) size++;
        return toArray();
    }


    public T pop() {
        if(pointer.get() >= max || pointer.get() >= size()) {
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

    public T[] toArray() {
        T[] arr = (T[]) new Object[size];
        for(int i = 0; i < size; i++) {
            arr[i] = buffer.get(i);
        }
        return arr;
    }

    private void shift() {
        pointer.set(pointer.get() == 0 ? 0 : pointer.get() - 1);
        for(int i = 1; i < size; i++) {
            buffer.set(i - 1, buffer.get(i));
        }
    }
}