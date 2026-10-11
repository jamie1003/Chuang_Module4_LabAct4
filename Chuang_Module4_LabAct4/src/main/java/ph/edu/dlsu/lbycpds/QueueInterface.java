package ph.edu.dlsu.lbycpds;
public interface QueueInterface {
    public void offer(Object item);
    public Object poll();
    public Object front();
    public Object back();
    public int size();
    public boolean isEmpty();
    public boolean isFull();
}