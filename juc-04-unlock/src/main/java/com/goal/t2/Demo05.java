package com.goal.t2;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * 解决ABA问题
 */
@Slf4j(topic = "mark")
public class Demo05 {

    public static void main(String[] args) throws InterruptedException {

        GarbageBag bag = new GarbageBag("装满了垃圾");

        AtomicMarkableReference<GarbageBag> ref =
                new AtomicMarkableReference<>(bag, true);

        log.debug("start...");
        GarbageBag prev = ref.getReference();
        log.debug(prev.toString());

        TimeUnit.SECONDS.sleep(1);
        log.debug("想要换一只新垃圾袋？");
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);

        log.debug("更换了吗？{}", success);
        log.debug(ref.getReference().toString());
    }


}

@Setter
@ToString
@AllArgsConstructor
class GarbageBag {
    String desc;
}