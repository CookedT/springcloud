package com.atguigu.springcloud.lb;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class MyLB implements LoadBalancer {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement() {
        int current;
        int next;

        do {
            current = this.atomicInteger.get();
            next = (current >= Integer.MAX_VALUE) ? 0 : current + 1;

            //compareAndSet(int expect, int update) 注意前面是期望值，后面是更改值别放错了
        } while (!this.atomicInteger.compareAndSet(current, next));
        log.info("当前访问次数: {}", next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        int index = getAndIncrement();
        index = index % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
