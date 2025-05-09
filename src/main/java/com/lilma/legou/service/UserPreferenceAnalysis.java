package com.lilma.legou.service;

import com.lilma.legou.pojo.BehaviorSequenceCategory;
import org.apache.commons.math3.ml.clustering.*;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.random.JDKRandomGenerator;
import java.util.*;
import java.util.stream.Collectors;

public class UserPreferenceAnalysis {

    public static class CategoryFeature {
        public Integer userId;
        public Integer categoryId;
        public double totalDwellTime;
        public int visitCount;
        public double avgDwellTime;
        public double dwellTimeRatio;
        public double visitCountRatio;

        public CategoryFeature(Integer userId, Integer categoryId) {
            this.userId = userId;
            this.categoryId = categoryId;
        }

        @Override
        public String toString() {
            return String.format("Category[%d] 访问:%d次 总时长:%.1f 平均:%.1f",
                    categoryId, visitCount, totalDwellTime, avgDwellTime);
        }
    }

    public static class PreferenceClusterer {
        private final int baseClusters;
        private final long randomSeed;
        private final double[] featureWeights;

        public PreferenceClusterer(int baseClusters) {
            this(baseClusters, 0xCAFEBABE, new double[]{0.15, 0.15, 0.70});
        }

        public PreferenceClusterer(int baseClusters, long randomSeed) {
            this(baseClusters, randomSeed, new double[]{0.15, 0.15, 0.70});
        }

        public PreferenceClusterer(int baseClusters, long randomSeed, double[] featureWeights) {
            validateWeights(featureWeights);
            this.baseClusters = baseClusters;
            this.randomSeed = randomSeed;

            // 标准化权重
            double sum = Arrays.stream(featureWeights).sum();
            this.featureWeights = Arrays.stream(featureWeights)
                    .map(w -> w / sum)
                    .toArray();
        }

        private void validateWeights(double[] weights) {
            if (weights.length != 3) {
                throw new IllegalArgumentException("必须提供3个特征权重");
            }
            if (Arrays.stream(weights).anyMatch(w -> w < 0)) {
                throw new IllegalArgumentException("权重值必须为非负数");
            }
        }

        public Map<Integer, String> analyzeUserPreference(Integer userId, List<CategoryFeature> features) {
            Objects.requireNonNull(features, "特征列表不能为空");

            // 单类目快速处理
            if (features.size() == 1) {
                return handleSingleCategory(userId, features.get(0));
            }

            if (features.isEmpty()) {
                System.out.println("警告：空特征列表，返回空结果");
                return Collections.emptyMap();
            }

            // 特征计算
            calculateRatios(features);

            // 数据预处理
            List<double[]> transformedFeatures = transformFeatures(features);
            RobustScaler scaler = new RobustScaler();
            scaler.fit(transformedFeatures);
            List<double[]> scaledFeatures = scaler.transform(transformedFeatures);

            // 构建数据点
            List<DoublePoint> points = scaledFeatures.stream()
                    .map(DoublePoint::new)
                    .collect(Collectors.toList());

            // 执行聚类
            List<CentroidCluster<DoublePoint>> clusters = performClustering(points);

            // 解释结果
            Map<Integer, String> result = interpretClusters(clusters, features, scaledFeatures);

            // 应用业务规则
            applyBusinessRules(result, features);

            return result;
        }

        private Map<Integer, String> handleSingleCategory(Integer userId, CategoryFeature feature) {
            System.out.printf("用户 %d 仅访问类目 %d，标记为高兴趣\n", userId, feature.categoryId);
            return Collections.singletonMap(feature.categoryId, "高兴趣");
        }

        private void calculateRatios(List<CategoryFeature> features) {
            double totalDwell = features.stream().mapToDouble(f -> f.totalDwellTime).sum();
            int totalVisits = features.stream().mapToInt(f -> f.visitCount).sum();

            features.forEach(f -> {
                f.dwellTimeRatio = safeDivide(f.totalDwellTime, totalDwell);
                f.visitCountRatio = safeDivide(f.visitCount, totalVisits);
                f.avgDwellTime = safeDivide(f.totalDwellTime, f.visitCount);
            });
        }

        private List<double[]> transformFeatures(List<CategoryFeature> features) {
            return features.stream()
                    .map(f -> new double[]{
                            Math.cbrt(f.dwellTimeRatio * 1000),  // 立方根变换
                            Math.cbrt(f.visitCountRatio * 1000),
                            Math.log1p(f.avgDwellTime)          // 对数变换
                    })
                    .collect(Collectors.toList());
        }

        private List<CentroidCluster<DoublePoint>> performClustering(List<DoublePoint> points) {
            JDKRandomGenerator rand = new JDKRandomGenerator();
            rand.setSeed(randomSeed);

            int actualClusters = Math.max(1, Math.min(baseClusters, points.size()));

            return new KMeansPlusPlusClusterer<DoublePoint>(
                    actualClusters,
                    100,
                    new EuclideanDistance(),
                    rand
            ).cluster(points);
        }

        private Map<Integer, String> interpretClusters(List<CentroidCluster<DoublePoint>> clusters,
                                                       List<CategoryFeature> features,
                                                       List<double[]> scaledFeatures) {
            // 计算聚类得分
            List<ClusterInfo> clusterInfos = clusters.stream()
                    .map(cluster -> {
                        double[] center = ((DoublePoint)cluster.getCenter()).getPoint();
                        double score = calculateClusterScore(center);
                        return new ClusterInfo(center, score, cluster.getPoints().size());
                    })
                    .sorted(Comparator.comparingDouble(ClusterInfo::getScore).reversed())
                    .collect(Collectors.toList());

            // 分配标签
            String[] labels = generateLabels(clusterInfos.size());
            Map<Integer, String> labelMap = new HashMap<>();
            for (int i = 0; i < clusterInfos.size(); i++) {
                labelMap.put(i, labels[i]);
            }

            // 分配类别到聚类
            Map<Integer, String> result = new LinkedHashMap<>();
            EuclideanDistance dist = new EuclideanDistance();

            for (int i = 0; i < features.size(); i++) {
                double[] point = scaledFeatures.get(i);
                CategoryFeature feature = features.get(i);

                int nearestCluster = findNearestCluster(point, clusterInfos, dist);
                String label = labelMap.get(nearestCluster);

                result.put(feature.categoryId, label);
                printAssignmentDetails(feature, clusterInfos.get(nearestCluster));
            }

            return result;
        }

        private double calculateClusterScore(double[] center) {
            double score = 0;
            for (int i = 0; i < center.length; i++) {
                score += center[i] * featureWeights[i];
            }
            return score;
        }

        private String[] generateLabels(int clusterCount) {
            if (clusterCount == 1) return new String[]{"高兴趣"};
            if (clusterCount == 2) return new String[]{"高兴趣", "低兴趣"};

            String[] labels = new String[clusterCount];
            labels[0] = "高兴趣";
            if (clusterCount > 2) labels[1] = "中等兴趣";
            for (int i = 2; i < clusterCount; i++) {
                labels[i] = "低兴趣";
            }
            return labels;
        }

        private int findNearestCluster(double[] point, List<ClusterInfo> clusters, EuclideanDistance dist) {
            int nearest = 0;
            double minDistance = Double.MAX_VALUE;

            for (int i = 0; i < clusters.size(); i++) {
                double distance = dist.compute(point, clusters.get(i).getCenter());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = i;
                }
            }
            return nearest;
        }

        private void applyBusinessRules(Map<Integer, String> result, List<CategoryFeature> features) {
            features.forEach(f -> {
                // 规则1：单次访问但总时长超过3000
                if (f.visitCount == 1 && f.totalDwellTime > 3000) {
                    result.put(f.categoryId, "高兴趣");
                    System.out.printf("[业务规则] 类别%d单次长时访问(%.1f秒)升级\n", f.categoryId, f.totalDwellTime);
                }

                // 规则2：平均时长超过2000且访问>=2次
                else if (f.avgDwellTime > 2000 && f.visitCount >= 2) {
                    result.put(f.categoryId, "高兴趣");
                    System.out.printf("[业务规则] 类别%d深度访问(平均%.1f秒)升级\n", f.categoryId, f.avgDwellTime);
                }

                // 规则3：总时长超过5000且访问>=3次
                if (f.totalDwellTime > 5000 && f.visitCount >= 3) {
                    result.put(f.categoryId, "高兴趣");
                    System.out.printf("[业务规则] 类别%d高频长时访问升级\n", f.categoryId);
                }
            });
        }

        private void printAssignmentDetails(CategoryFeature feature, ClusterInfo cluster) {
            System.out.println("----------------------------");
            System.out.printf("类别 %d 分配到聚类：\n", feature.categoryId);
            System.out.printf("| 聚类中心：%s\n", Arrays.toString(cluster.getCenter()));
            System.out.printf("| 聚类得分：%.2f\n", cluster.getScore());
            System.out.printf("| 包含样本：%d个\n", cluster.getSize());
            System.out.println(feature);
        }

        private double safeDivide(double numerator, double denominator) {
            return denominator == 0 ? 0 : numerator / denominator;
        }

        private static class ClusterInfo {
            private final double[] center;
            private final double score;
            private final int size;

            public ClusterInfo(double[] center, double score, int size) {
                this.center = center;
                this.score = score;
                this.size = size;
            }

            public double[] getCenter() { return center; }
            public double getScore() { return score; }
            public int getSize() { return size; }
        }
    }

    public static class RobustScaler {
        private double[] medians;
        private double[] iqrs;

        public void fit(List<double[]> data) {
            int dimensions = data.get(0).length;
            medians = new double[dimensions];
            iqrs = new double[dimensions];

            for (int d = 0; d < dimensions; d++) {
                final int dim = d;
                double[] values = data.stream()
                        .mapToDouble(arr -> arr[dim])
                        .sorted()
                        .toArray();

                // 计算中位数
                medians[d] = values[values.length / 2];

                // 计算IQR
                int q1Index = values.length / 4;
                int q3Index = values.length * 3 / 4;
                double q1 = values[q1Index];
                double q3 = values[q3Index];
                iqrs[d] = Math.max(q3 - q1, 0.1);  // IQR下限保护
            }
        }

        public List<double[]> transform(List<double[]> data) {
            return data.stream()
                    .map(arr -> {
                        double[] transformed = new double[arr.length];
                        for (int i = 0; i < arr.length; i++) {
                            transformed[i] = (arr[i] - medians[i]) / iqrs[i];
                        }
                        return transformed;
                    })
                    .collect(Collectors.toList());
        }
    }

    public static class DataProcessor {
        public Map<Integer, List<CategoryFeature>> processData(List<BehaviorSequenceCategory> sequences) {
            Map<Integer, Map<Integer, CategoryFeature>> userMap = new HashMap<>();

            for (BehaviorSequenceCategory seq : sequences) {
                if (!validateRecord(seq)) continue;

                userMap.putIfAbsent(seq.userId, new HashMap<>());
                Map<Integer, CategoryFeature> categoryMap = userMap.get(seq.userId);

                categoryMap.putIfAbsent(seq.categoryId, new CategoryFeature(seq.userId, seq.categoryId));
                CategoryFeature feature = categoryMap.get(seq.categoryId);

                feature.totalDwellTime += seq.dwellTime;
                feature.visitCount += 1;
            }

            // 计算平均时长
            userMap.values().forEach(categoryMap ->
                    categoryMap.values().forEach(f ->
                            f.avgDwellTime = safeDivide(f.totalDwellTime, f.visitCount)
                    )
            );

            return userMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> new ArrayList<>(e.getValue().values())
                    ));
        }

        private boolean validateRecord(BehaviorSequenceCategory seq) {
            if (seq.userId == null || seq.categoryId == null || seq.dwellTime == null || seq.dwellTime < 0) {
                System.out.println("无效记录：" + seq);
                return false;
            }
            return true;
        }

        private double safeDivide(double a, double b) {
            return b == 0 ? 0 : a / b;
        }
    }
}