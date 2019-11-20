package interview.study.thread;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: xujh
 * @Date: 2019/11/8 14:04
 * @Description:
 */

class User{
    String username;
    int age;

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}

/**
 * 原子类AtomicInteger的ABA问题谈谈?原子更新引用知道吗
 * 原子引用AtomicReference
 */
public class AtomicReferenceDemo {

    public static void main(String[] args) {
        User z3 = new User("z3",18);
        User li4 = new User("li4",24);

        AtomicReference<User> atomicReference = new AtomicReference<User> ();
        atomicReference.set(z3);

        System.out.println(atomicReference.compareAndSet(z3,li4)+"\t"+atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3,li4)+"\t"+atomicReference.get().toString());
    }
}
