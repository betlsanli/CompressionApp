import java.util.ArrayList;

public class MinPQueue<T extends Comparable<T>> {
    private ArrayList<T> list;

    public MinPQueue(){
        list = new ArrayList<>();
    }
    public MinPQueue(int cap){
        list = new ArrayList<>(cap);
    }
    public boolean isEmpty(){
        return list.size() == 0;
    }
    public int size(){
        return list.size();
    }
    private int parent(int loc){
        return (loc-1)/2;
    }
    private int left(int loc){
        return 2*loc + 1;
    }
    private int right(int loc){
        return 2*loc + 2;
    }
    private void heapUp(int loc){
        while(loc > 0){
            if( list.get(loc).compareTo(list.get(parent(loc)) ) < 0){
                T temp = list.get(loc);
                list.set(loc,list.get(parent(loc)));
                list.set(parent(loc), temp);

                loc = parent(loc);
            }
            else break;
        }
    }
    public void add(T item){
        list.add(item);
        heapUp(list.size() - 1);
    }
    private int minChildIdx(int loc){
        if(right(loc) < list.size()){
            if(list.get(right(loc)).compareTo(list.get(left(loc))) < 0){
                return right(loc);
            }
            else return left(loc);
        }
        else if(left(loc) < list.size())
            return left(loc);
        else return -1;
    }
    private void heapDown(int loc){
        while(loc < list.size()){
            int newLoc = minChildIdx(loc);
            if(newLoc == -1) return;
            else if(list.get(loc).compareTo(list.get(newLoc)) > 0){
                T temp = list.get(loc);
                list.set(loc, list.get(newLoc));
                list.set(newLoc,temp);

                loc = newLoc;
            }
            else return;
        }

    }

    public T poll(){
        if(list.size() == 0)
            throw new RuntimeException("Priority Queue is empty!");
        T item = list.get(0);
        list.set(0,list.get(list.size()-1));
        list.remove(list.size()-1);
        heapDown(0);
        return item;
    }
    public String toString(){
        if(list.isEmpty()){
            return "List is empty";
        }
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            str.append(list.get(i)).append(" ");
        }
        return str.toString();
    }

}
