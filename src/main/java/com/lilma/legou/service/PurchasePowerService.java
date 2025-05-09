package com.lilma.legou.service;

import com.lilma.legou.pojo.PurchaseOrder;
import org.apache.commons.math3.ml.clustering.*;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.random.JDKRandomGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class PurchasePowerService {
    private final PurchaseClassifier classifier;
    private final Map<Integer, UserPurchaseProfile> userProfileMap;

    public PurchasePowerService(List<PurchaseOrder> orders) {
        FeatureEngineer engineer = new FeatureEngineer();
        List<UserPurchaseProfile> profiles = engineer.generateFeatures(orders);

        if (profiles.isEmpty()) {
            this.classifier = null;
            this.userProfileMap = Collections.emptyMap();
            return;
        }

        new FeatureNormalizer().normalize(profiles);

        PurchaseClusterer clusterer = new PurchaseClusterer();
        // 强制固定3个聚类中心
        List<CentroidCluster<PurchaseClusterer.ClusterableProfile>> clusters =
                clusterer.clusterUsers(profiles, 3);

        this.classifier = new PurchaseClassifier(clusters);
        this.userProfileMap = profiles.stream()
                .collect(Collectors.toMap(UserPurchaseProfile::getUserId, p -> p));
    }

    public String getPurchasePowerLevel(Integer userId) {
        if (classifier == null) return "LOW";
        UserPurchaseProfile profile = userProfileMap.get(userId);
        return profile != null ? classifier.classifyUser(profile) : "LOW";
    }

    public static class UserPurchaseProfile {
        private Integer userId;
        private BigDecimal totalSpent;
        private Integer orderCount;
        private BigDecimal avgOrderValue;
        private BigDecimal maxOrderValue;
        private Double purchaseFrequency;
        private double[] normalizedFeatures;

        // Getters and Setters
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public BigDecimal getTotalSpent() { return totalSpent; }
        public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }
        public Integer getOrderCount() { return orderCount; }
        public void setOrderCount(Integer orderCount) { this.orderCount = orderCount; }
        public BigDecimal getAvgOrderValue() { return avgOrderValue; }
        public void setAvgOrderValue(BigDecimal avgOrderValue) { this.avgOrderValue = avgOrderValue; }
        public BigDecimal getMaxOrderValue() { return maxOrderValue; }
        public void setMaxOrderValue(BigDecimal maxOrderValue) { this.maxOrderValue = maxOrderValue; }
        public Double getPurchaseFrequency() { return purchaseFrequency; }
        public void setPurchaseFrequency(Double purchaseFrequency) { this.purchaseFrequency = purchaseFrequency; }
        public double[] getNormalizedFeatures() { return normalizedFeatures; }
        public void setNormalizedFeatures(double[] normalizedFeatures) { this.normalizedFeatures = normalizedFeatures; }
    }

    public static class FeatureEngineer {
        public List<UserPurchaseProfile> generateFeatures(List<PurchaseOrder> orders) {
            Map<Integer, List<PurchaseOrder>> userOrders = orders.stream()
                    .collect(Collectors.groupingBy(PurchaseOrder::getUserId));
            return userOrders.entrySet().stream().map(entry -> {
                UserPurchaseProfile profile = new UserPurchaseProfile();
                List<PurchaseOrder> userOrdersList = entry.getValue();
                profile.setUserId(entry.getKey());
                BigDecimal total = userOrdersList.stream()
                        .map(PurchaseOrder::getTotalprice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                profile.setTotalSpent(total);
                int count = userOrdersList.size();
                profile.setOrderCount(count);
                profile.setAvgOrderValue(count > 0 ?
                        total.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP) :
                        BigDecimal.ZERO);
                profile.setMaxOrderValue(userOrdersList.stream()
                        .map(PurchaseOrder::getTotalprice)
                        .max(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO));
                LocalDateTime firstOrder = userOrdersList.stream()
                        .map(PurchaseOrder::getCreateTime)
                        .min(LocalDateTime::compareTo)
                        .orElse(LocalDateTime.now());
                LocalDateTime lastOrder = userOrdersList.stream()
                        .map(PurchaseOrder::getCreateTime)
                        .max(LocalDateTime::compareTo)
                        .orElse(LocalDateTime.now());

                long days = ChronoUnit.DAYS.between(firstOrder, lastOrder);
                days = days == 0 ? 1 : days;
                profile.setPurchaseFrequency(count * 1.0 / days);

                return profile;
            }).collect(Collectors.toList());
        }
    }

    public static class FeatureNormalizer {
        public void normalize(List<UserPurchaseProfile> profiles) {
            double[] totals = profiles.stream()
                    .mapToDouble(p -> p.getTotalSpent().doubleValue())
                    .toArray();
            double[] averages = profiles.stream()
                    .mapToDouble(p -> p.getAvgOrderValue().doubleValue())
                    .toArray();
            double[] frequencies = profiles.stream()
                    .mapToDouble(p -> p.getPurchaseFrequency())
                    .toArray();
            StandardScaler totalScaler = new StandardScaler(totals);
            StandardScaler avgScaler = new StandardScaler(averages);
            StandardScaler freqScaler = new StandardScaler(frequencies);
            profiles.forEach(p -> p.setNormalizedFeatures(new double[]{
                    totalScaler.normalize(p.getTotalSpent().doubleValue()),
                    avgScaler.normalize(p.getAvgOrderValue().doubleValue()),
                    freqScaler.normalize(p.getPurchaseFrequency())
            }));
        }

        private static class StandardScaler {
            private final double mean;
            private final double std;

            public StandardScaler(double[] data) {
                this.mean = Arrays.stream(data).average().orElse(0);
                this.std = Math.sqrt(Arrays.stream(data)
                        .map(d -> Math.pow(d - mean, 2))
                        .average().orElse(0));
            }

            public double normalize(double value) {
                return (value - mean) / (std == 0 ? 1 : std);
            }
        }
    }

    public static class PurchaseClusterer {
        private static final long FIXED_SEED = 0xCAFEBABE;

        public List<CentroidCluster<ClusterableProfile>> clusterUsers(
                List<UserPurchaseProfile> profiles, int clusters) {

            List<ClusterableProfile> clusterableProfiles = profiles.stream()
                    .map(p -> new ClusterableProfile(p, p.getNormalizedFeatures()))
                    .collect(Collectors.toList());

            JDKRandomGenerator rand = new JDKRandomGenerator();
            rand.setSeed(FIXED_SEED);

            KMeansPlusPlusClusterer<ClusterableProfile> clusterer =
                    new KMeansPlusPlusClusterer<>(
                            3, // 固定3个聚类
                            100,
                            new EuclideanDistance(),
                            rand
                    );

            return clusterer.cluster(clusterableProfiles);
        }

        private static class ClusterableProfile implements Clusterable {
            private final UserPurchaseProfile profile;
            private final double[] point;

            public ClusterableProfile(UserPurchaseProfile profile, double[] point) {
                this.profile = profile;
                this.point = point;
            }

            @Override
            public double[] getPoint() { return point; }
            public UserPurchaseProfile getProfile() { return profile; }
        }
    }

    public static class PurchaseClassifier {
        private final List<double[]> clusterCenters;

        public PurchaseClassifier(List<CentroidCluster<PurchaseClusterer.ClusterableProfile>> clusters) {
            this.clusterCenters = clusters.stream()
                    .map(c -> c.getCenter().getPoint())
                    .collect(Collectors.toList());
        }

        public String classifyUser(UserPurchaseProfile user) {
            double[] features = user.getNormalizedFeatures();
            int closestClusterIndex = findClosestCluster(features);
            return getLabelByCompositeScore(closestClusterIndex);
        }

        private int findClosestCluster(double[] features) {
            int closest = 0;
            double minDistance = Double.MAX_VALUE;
            for (int i = 0; i < clusterCenters.size(); i++) {
                double distance = euclideanDistance(features, clusterCenters.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = i;
                }
            }
            return closest;
        }

        private String getLabelByCompositeScore(int clusterIndex) {
            // 计算综合得分（权重：总消费60%，平均消费30%，频率10%）
            List<ClusterScore> scores = new ArrayList<>();
            for (int i = 0; i < clusterCenters.size(); i++) {
                double[] center = clusterCenters.get(i);
                double score = 0.6 * center[0] + 0.3 * center[1] + 0.1 * center[2];
                scores.add(new ClusterScore(i, score));
            }
            // 按得分排序
            scores.sort(Comparator.comparingDouble(ClusterScore::score).reversed());
            // 找到当前cluster对应的ClusterScore
            ClusterScore target = null;
            for (ClusterScore s : scores) {
                if (s.index() == clusterIndex) {
                    target = s;
                    break;
                }
            }
            if (target == null) {
                return "LOW";
            }
            // 根据排序位置确定标签
            int position = scores.indexOf(target);
            switch (position) {
                case 0:
                    return "HIGH";
                case 1:
                    return "MEDIUM";
                default:
                    return "LOW";
            }
        }

        private double euclideanDistance(double[] a, double[] b) {
            double sum = 0;
            for (int i = 0; i < a.length; i++) {
                sum += Math.pow(a[i] - b[i], 2);
            }
            return Math.sqrt(sum);
        }

        private static class ClusterScore {
            private final int index;
            private final double score;

            public ClusterScore(int index, double score) {
                this.index = index;
                this.score = score;
            }

            public int index() { return index; }
            public double score() { return score; }
        }
    }
}