package com.lilma.legou.service;

import com.lilma.legou.pojo.BehaviorSequenceGoods;
import java.util.*;
import java.util.stream.Collectors;

public class DwellTimeCF {

    public static class EnhancedCFModel {
        public Map<Integer, Integer> userIndex = new HashMap<>();
        public Map<Integer, Integer> itemIndex = new HashMap<>();
        public double[][] avgDwellMatrix;
        public long[][] totalDwellMatrix;
        public int[][] visitCountMatrix;
        public List<Integer> allUsers = new ArrayList<>();
        public List<Integer> allItems = new ArrayList<>();
        public Map<Integer, Double> itemQuality = new HashMap<>();
        public Map<Integer, Set<Integer>> userViewedItems = new HashMap<>();
    }

    public static EnhancedCFModel preprocessData(List<BehaviorSequenceGoods> data) {
        EnhancedCFModel model = new EnhancedCFModel();

        // 动态收集所有用户和商品
        Set<Integer> uniqueUsers = new HashSet<>();
        Set<Integer> uniqueItems = new HashSet<>();
        for (BehaviorSequenceGoods b : data) {
            uniqueUsers.add(b.getUserId());
            uniqueItems.add(b.getGoodsId());
        }
        model.allUsers = new ArrayList<>(uniqueUsers);
        model.allItems = new ArrayList<>(uniqueItems);
        Collections.sort(model.allUsers);
        Collections.sort(model.allItems);

        // 构建索引和初始化数据结构
        for (int i = 0; i < model.allUsers.size(); i++) {
            Integer user = model.allUsers.get(i);
            model.userIndex.put(user, i);
            model.userViewedItems.put(user, new HashSet<>());
        }
        for (int i = 0; i < model.allItems.size(); i++) {
            model.itemIndex.put(model.allItems.get(i), i);
        }

        // 初始化矩阵
        int userSize = model.allUsers.size();
        int itemSize = model.allItems.size();
        model.avgDwellMatrix = new double[userSize][itemSize];
        model.totalDwellMatrix = new long[userSize][itemSize];
        model.visitCountMatrix = new int[userSize][itemSize];

        // 处理每条记录
        for (BehaviorSequenceGoods b : data) {
            Integer user = b.getUserId();
            Integer item = b.getGoodsId();
            // 动态处理未索引用户（理论上不应该发生）
            if (!model.userIndex.containsKey(user)) {
                model.allUsers.add(user);
                int newIndex = model.allUsers.size() - 1;
                model.userIndex.put(user, newIndex);
                model.userViewedItems.put(user, new HashSet<>());
            }
            int u = model.userIndex.get(user);
            int i = model.itemIndex.get(item);
            // 更新浏览记录
            model.userViewedItems.get(user).add(item);
            // 更新矩阵数据
            model.totalDwellMatrix[u][i] += b.getDwellTime();
            model.visitCountMatrix[u][i]++;
            model.avgDwellMatrix[u][i] = model.totalDwellMatrix[u][i] / (double) model.visitCountMatrix[u][i];
        }

        // 计算商品质量评分
        Map<Integer, Long> itemTotalDwell = new HashMap<>();
        Map<Integer, Integer> itemVisitCount = new HashMap<>();
        for (BehaviorSequenceGoods b : data) {
            itemTotalDwell.merge(b.getGoodsId(), b.getDwellTime(), Long::sum);
            itemVisitCount.merge(b.getGoodsId(), 1, Integer::sum);
        }
        model.itemQuality = itemTotalDwell.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() * Math.sqrt(itemVisitCount.get(e.getKey()))
                ));

        return model;
    }

    public static List<Integer> recommendForUser(EnhancedCFModel model, Integer userId) {
        final int TARGET_NUM = 4;
        Set<Integer> viewedItems = model.userViewedItems.getOrDefault(userId, new HashSet<>());
        List<Integer> recommendations = new ArrayList<>();

        // 阶段1：优先推荐用户浏览过的商品（按质量排序）
        List<Integer> viewedList = viewedItems.stream()
                .sorted((a, b) -> Double.compare(
                        model.itemQuality.getOrDefault(b, 0.0),
                        model.itemQuality.getOrDefault(a, 0.0)
                ))
                .collect(Collectors.toList());
        viewedList.forEach(item -> {
            if (!recommendations.contains(item) && recommendations.size() < TARGET_NUM) {
                recommendations.add(item);
            }
        });

        // 阶段2：添加协同过滤推荐（假设实现）
        List<Integer> cfRecommendations = getCFRecommendations(model, userId);
        cfRecommendations.forEach(item -> {
            if (!recommendations.contains(item) && recommendations.size() < TARGET_NUM) {
                recommendations.add(item);
            }
        });

        // 阶段3：用热门商品补齐
        if (recommendations.size() < TARGET_NUM) {
            model.itemQuality.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .forEach(item -> {
                        if (!recommendations.contains(item) && recommendations.size() < TARGET_NUM) {
                            recommendations.add(item);
                        }
                    });
        }

        // 阶段4：保底填充
        if (recommendations.size() < TARGET_NUM) {
            model.allItems.forEach(item -> {
                if (!recommendations.contains(item) && recommendations.size() < TARGET_NUM) {
                    recommendations.add(item);
                }
            });
        }

        return recommendations;
    }

    private static List<Integer> getCFRecommendations(EnhancedCFModel model, Integer userId) {
        //协同过滤实现
        return model.itemQuality.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}