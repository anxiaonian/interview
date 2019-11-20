package interview.study.collection;
import	java.util.concurrent.ConcurrentHashMap;
import java.util.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Auther: xujh
 * @Date: 2019/11/8 14:57
 * @Description: 集合类不安全问题
 *                 ArrayList
 * 我们知道ArrayList是线程不安全,请编写一个不安全的案例并给出解决方案
 */
public class CollectionNotSafeDemo {

    public static void main(String[] args) {

        Map<String,String> map =new ConcurrentHashMap<String, String> ();//Collections.synchronizedMap(new HashMap<String, String> ()); //new HashMap<>();
        for (int i =1; i < 30; i++){
            new Thread(()->{
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0,8));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }

    //Set
    private static void setNotSafe() {
        Set<String> set = new CopyOnWriteArraySet();//Collections.synchronizedSet(new HashSet<>()); //new HashSet<> ();
        for (int i =1; i < 30; i++){
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }

    //List
    private static void ArrayListNotSafe() {
        //List<String> list = new ArrayList<> ();
        //List<String> list = new Vector<>();
        //List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList();

        for (int i =1; i < 30; i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
        //出现java.util.ConcurrentModificationException 并发修改异常
        /**
         * 1，故障出现
         *  出现java.util.ConcurrentModificationException 并发修改异常
         *
         * 2，出现原因
         *      并发修改时出现争抢导致数据不一致
         * 3，解决方案
         *      3.1 使用 new Vector<>()
         *      3.2 使用 Collections.synchronizedList(new ArrayList<>());
         *      3.3 使用 new CopyOnWriteArrayList   写时复制
         *
         */

        /**  CopyOnWriteArrayList源码
         * Creates a list containing the elements of the specified
         * collection, in the order they are returned by the collection's
         * iterator.
         *
         * @param c the collection of initially held elements
         * @throws NullPointerException if the specified collection is null

        public CopyOnWriteArrayList(Collection<? extends E> c) {
        Object[] elements;
        if (c.getClass() == CopyOnWriteArrayList.class)
        elements = ((CopyOnWriteArrayList<?>)c).getArray();
        else {
        elements = c.toArray();
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elements.getClass() != Object[].class)
        elements = Arrays.copyOf(elements, elements.length, Object[].class);
        }
        setArray(elements);
        }

         */
    }
}
