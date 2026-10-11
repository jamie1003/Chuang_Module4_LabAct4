package ph.edu.dlsu.lbycpds;
public class MyQueue implements QueueInterface {
    private Object[] elements;
    private int front;
    private int rear;
    private int count;
    private int capacity;

    public MyQueue(int capacity) {
        this.capacity = capacity;
        this.elements = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.count = 0;
    }

    @Override
    public void offer(Object item) {
        if (isFull()) {
            System.out.println("Queue is full.");
            return;
        }
        rear = (rear + 1) % capacity;
        elements[rear] = item;
        count++;
    }

    @Override
    public Object poll() {
        if (isEmpty()) {
            return null;
        }
        Object item = elements[front];
        elements[front] = null;
        front = (front + 1) % capacity;
        count--;
        return item;
    }

    @Override
    public Object front() {
        if (isEmpty()) {
            return null;
        }
        return elements[front];
    }

    @Override
    public Object back() {
        if (isEmpty()) {
            return null;
        }
        return elements[rear];
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean isFull() {
        return count == capacity;
    }
}