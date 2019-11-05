/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.thread.future;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class FutureTest {

    SubRepository subRepository = new SubRepository();

    private BatchSubModel bulkInsertSingleThread(Set<EntInfoDO> set, String userId) throws ExecutionException, InterruptedException {
        BatchSubModel batchSubModel = new BatchSubModel();
        List<SubDO> subList = Lists.newArrayList();
        List<SubDO> subOpList = Lists.newArrayList();
        List<SubDO> existList = Lists.newArrayList();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Map<String, List<SubDO>>>> futureList = Lists.newArrayList();
        List<EntInfoDO> list = Lists.newArrayList(set);
        int chunkNum = 10;
        int chunkSize = list.size() / chunkNum + 1;
        log.info("chunkNum={}, chunkSize={}", chunkNum, chunkSize);
        for (int i = 0; i < 10; i++) {
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize > list.size() ? list.size() : (i + 1) * chunkSize;
            log.info("start={}, end={}", start, end);
            List<EntInfoDO> chunkList = list.subList(start, end);
            Map<String, List<SubDO>> map = batchSave(chunkList, userId);
            subList.addAll(map.get("add"));
            existList.addAll(map.get("exist"));
        }
        log.info("all future completed");

        batchSubModel.getExists().addAll(existList);
        batchSubModel.getOrder().addAll(subList);
        return batchSubModel;
    }

    private BatchSubModel bulkInsertWithFuture(Set<EntInfoDO> set, String userId) throws ExecutionException, InterruptedException {
        BatchSubModel batchSubModel = new BatchSubModel();
        List<SubDO> subList = Lists.newArrayList();
        List<SubDO> existList = Lists.newArrayList();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Map<String, List<SubDO>>>> futureList = Lists.newArrayList();
        List<EntInfoDO> list = Lists.newArrayList(set);
        int chunkNum = 10;
        int chunkSize = list.size() / chunkNum + 1;
        log.info("chunkNum={}, chunkSize={}", chunkNum, chunkSize);
        for (int i = 0; i < 10; i++) {
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize > list.size() ? list.size() : (i + 1) * chunkSize;
            log.info("start={}, end={}", start, end);
            List<EntInfoDO> chunkList = list.subList(start, end);
            Future<Map<String, List<SubDO>>> future = executorService.submit(new Callable<Map<String, List<SubDO>>>() {
                @Override
                public Map<String, List<SubDO>> call() throws Exception {
                    return batchSave(chunkList, userId);
                }
            });
            futureList.add(future);
        }
        executorService.shutdown();

        log.info("all future completed");
        for (Future<Map<String, List<SubDO>>> future : futureList) {
            Map<String, List<SubDO>> map = future.get();
            subList.addAll(map.get("add"));
            existList.addAll(map.get("exist"));
        }
        batchSubModel.getExists().addAll(existList);
        batchSubModel.getOrder().addAll(subList);
        return batchSubModel;
    }

    private BatchSubModel bulkInsertWithCompletionService(Set<EntInfoDO> set, String userId) throws ExecutionException, InterruptedException {
        BatchSubModel batchSubModel = new BatchSubModel();
        List<SubDO> subList = Lists.newArrayList();
        List<SubDO> existList = Lists.newArrayList();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletionService completionService = new ExecutorCompletionService(executorService);
        List<Future<Map<String, List<SubDO>>>> futureList = Lists.newArrayList();
        List<EntInfoDO> list = Lists.newArrayList(set);
        int chunkNum = 10;
        int chunkSize = list.size() / chunkNum + 1;
        log.info("chunkNum={}, chunkSize={}", chunkNum, chunkSize);
        for (int i = 0; i < 10; i++) {
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize > list.size() ? list.size() : (i + 1) * chunkSize;
            log.info("start={}, end={}", start, end);
            List<EntInfoDO> chunkList = list.subList(start, end);
            Future<Map<String, List<SubDO>>> future = completionService.submit(new Callable<Map<String, List<SubDO>>>() {
                @Override
                public Map<String, List<SubDO>> call() throws Exception {
                    return batchSave(chunkList, userId);
                }
            });
            futureList.add(future);
        }
        executorService.shutdown();

        log.info("all future completed");
        for (Future<Map<String, List<SubDO>>> future : futureList) {
            Map<String, List<SubDO>> map = future.get();
            subList.addAll(map.get("add"));
            existList.addAll(map.get("exist"));
        }
        batchSubModel.getExists().addAll(existList);
        batchSubModel.getOrder().addAll(subList);
        return batchSubModel;
    }

    private BatchSubModel bulkInsertWithFutureTask(Set<EntInfoDO> set, String userId) throws ExecutionException, InterruptedException {
        BatchSubModel batchSubModel = new BatchSubModel();
        List<SubDO> subList = Lists.newArrayList();
        List<SubDO> existList = Lists.newArrayList();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Map<String, List<SubDO>>>> futureList = Lists.newArrayList();
        List<EntInfoDO> list = Lists.newArrayList(set);
        int chunkNum = 10;
        int chunkSize = list.size() / chunkNum + 1;
        log.info("chunkNum={}, chunkSize={}", chunkNum, chunkSize);
        for (int i = 0; i < 10; i++) {
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize > list.size() ? list.size() : (i + 1) * chunkSize;
            log.info("start={}, end={}", start, end);
            List<EntInfoDO> chunkList = list.subList(start, end);
            Task task = new Task(chunkList, userId);
            FutureTask futureTask = new FutureTask(task);
            Future<Map<String, List<SubDO>>> future = (Future<Map<String, List<SubDO>>>) executorService.submit(futureTask);
            futureList.add(future);
        }
        executorService.shutdown();

        log.info("all future completed");
        for (Future<Map<String, List<SubDO>>> future : futureList) {
            Map<String, List<SubDO>> map = future.get();
            subList.addAll(map.get("add"));
            existList.addAll(map.get("exist"));
        }
        batchSubModel.getExists().addAll(existList);
        batchSubModel.getOrder().addAll(subList);
        return batchSubModel;
    }

    public Map<String, List<SubDO>> batchSave(List<EntInfoDO> set, String userId) throws InterruptedException {
        Map<String, List<SubDO>> map = Maps.newHashMap();
        List<SubDO> subList = Lists.newArrayList();
        List<SubDO> existList = Lists.newArrayList();
        List<SubDO> subOpList = Lists.newArrayList();
        if (!set.isEmpty()) {
            for (EntInfoDO entInfoDO : set) {
                SubDO subDO = subRepository.getSub(entInfoDO.getEntId(), userId);
                if (subDO == null) {
                    subDO = new SubDO(entInfoDO.getEntId(), userId);
                    subOpList.add(subDO);
                    if (subOpList.size() == 1000) {
                        subRepository.insertAll(subOpList);
                        subList.addAll(subOpList);
                        subOpList.clear();
                    }
                } else {
                    existList.add(subDO);
                }
            }
            if (subOpList.size() > 0) {
                subRepository.insertAll(subList);
                subList.addAll(subOpList);
                subOpList = null;
            }
            log.info("{} process sub {}", Thread.currentThread().getName(), subList.size());

            map.put("add", subList);
            map.put("exist", existList);
        }
        return map;
    }

    class Task implements Callable<Map<String, List<SubDO>>> {
        List<EntInfoDO> list;
        String userId;

        public Task(List<EntInfoDO> list, String userId) {
            this.list = list;
            this.userId = userId;
        }

        @Override
        public Map<String, List<SubDO>> call() throws Exception {
            return batchSave(list, userId);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Set<EntInfoDO> set = Sets.newHashSet();
        for (int i = 0; i < 50001; i++) {
            set.add(new EntInfoDO(i + ""));
        }
        log.info("set size={}", set.size());
        long start = System.currentTimeMillis();
        BatchSubModel batchSubModel = new FutureTest().bulkInsertSingleThread(set, "1");
        log.info("cost {}ms, total={}, added={}, exist={}, notfound={}", (System.currentTimeMillis() - start),
                batchSubModel.getOrder().size() + batchSubModel.getExists().size() + batchSubModel.getNotFound().size(),
                batchSubModel.getOrder().size(),
                batchSubModel.getExists().size(),
                batchSubModel.getNotFound().size());

        long start1 = System.currentTimeMillis();
        BatchSubModel batchSubModel1 = new FutureTest().bulkInsertWithFuture(set, "1");
        log.info("cost {}ms, total={}, added={}, exist={}, notfound={}", (System.currentTimeMillis() - start1),
                batchSubModel1.getOrder().size() + batchSubModel1.getExists().size() + batchSubModel1.getNotFound().size(),
                batchSubModel1.getOrder().size(),
                batchSubModel1.getExists().size(),
                batchSubModel1.getNotFound().size());

        long start2 = System.currentTimeMillis();
        BatchSubModel batchSubModel2 = new FutureTest().bulkInsertWithCompletionService(set, "1");
        log.info("cost {}ms, total={}, added={}, exist={}, notfound={}", (System.currentTimeMillis() - start2),
                batchSubModel2.getOrder().size() + batchSubModel2.getExists().size() + batchSubModel2.getNotFound().size(),
                batchSubModel2.getOrder().size(),
                batchSubModel2.getExists().size(),
                batchSubModel2.getNotFound().size());

        long start3 = System.currentTimeMillis();
        BatchSubModel batchSubModel3 = new FutureTest().bulkInsertWithCompletionService(set, "1");
        log.info("cost {}ms, total={}, added={}, exist={}, notfound={}", (System.currentTimeMillis() - start3),
                batchSubModel3.getOrder().size() + batchSubModel3.getExists().size() + batchSubModel3.getNotFound().size(),
                batchSubModel3.getOrder().size(),
                batchSubModel3.getExists().size(),
                batchSubModel3.getNotFound().size());
    }
}
