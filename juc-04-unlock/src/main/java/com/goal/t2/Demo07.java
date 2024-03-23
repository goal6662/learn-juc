package com.goal.t2;

import lombok.ToString;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 原子更新器
 */
public class Demo07 {

    public static void main(String[] args) {

        Student student = new Student();

        AtomicReferenceFieldUpdater<Student, String> updater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");

        System.out.println(updater.compareAndSet(student, null, "Daming"));
        System.out.println(student);
    }

}

@ToString
class Student {
    public volatile String name;
}
