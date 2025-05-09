package com.lilma.legou.service;

import com.lilma.legou.pojo.OrderRecord;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.DropoutLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SalesTrendPredictionService {
    private MultiLayerNetwork model;
    private final int featureSize;
    private final int totalGoods;  // 表示最大有效商品ID
    private final Map<Integer, Deque<Integer>> goodsHistory = new LinkedHashMap<>();

    // 配置参数
    private static final double TREND_THRESHOLD = 0.15;
    private static final int MIN_HISTORY_SIZE = 2;
    private static final int MAX_HISTORY_SIZE = 60;
    private static final double SALES_SCALE = 10000.0;

    public SalesTrendPredictionService(List<OrderRecord> orders, int goodsCount) {
        // 计算实际最大商品ID，确保覆盖自增ID的情况
        int maxOrderId = orders.stream()
                .filter(r -> r.goodsid != null)
                .mapToInt(r -> r.goodsid)
                .max()
                .orElse(0);

        this.totalGoods = Math.max(maxOrderId, goodsCount);
        this.featureSize = totalGoods + 4;
        processHistoricalData(cleanData(orders));
        trainModel(createTrainingDataset());
    }

    // 数据清洗
    private List<OrderRecord> cleanData(List<OrderRecord> rawData) {
        return rawData.stream()
                .filter(r -> r.goodsid != null && r.goodsid > 0)
                .filter(r -> r.goodsnum != null && r.goodsnum > 0)
                .filter(r -> r.createTime != null)
                .collect(Collectors.toList());
    }

    // 历史数据处理
    private void processHistoricalData(List<OrderRecord> orders) {
        orders.stream()
                .sorted(Comparator.comparing(o -> o.createTime))
                .forEachOrdered(order -> {
                    Deque<Integer> history = goodsHistory.computeIfAbsent(
                            order.goodsid,
                            k -> new ArrayDeque<>(MAX_HISTORY_SIZE)
                    );

                    while (history.size() >= MAX_HISTORY_SIZE) {
                        history.removeFirst();
                    }
                    history.addLast(order.goodsnum);
                });
    }

    // 创建训练数据集
    private List<LabeledData> createTrainingDataset() {
        List<LabeledData> dataset = new ArrayList<>();
        goodsHistory.forEach((gid, history) -> {
            List<Integer> histList = new ArrayList<>(history);
            for (int i = 1; i < histList.size(); i++) {
                List<Integer> subHistory = histList.subList(0, i);
                int label = calculateTrendLabel(subHistory);
                OrderRecord record = new OrderRecord(
                        gid,
                        histList.get(i-1),
                        LocalDateTime.now().minusDays(histList.size() - i)
                );
                dataset.add(new LabeledData(record, label));
            }
        });
        return dataset;
    }

    // 趋势标签计算
    private int calculateTrendLabel(List<Integer> history) {
        if (history.size() < MIN_HISTORY_SIZE) return 1;

        int window = Math.min(7, history.size()/2);
        int minSize = Math.min(window * 2, history.size());

        List<Integer> validHistory = history.subList(history.size()-minSize, history.size());
        List<Integer> prevPeriod = validHistory.subList(0, window);
        List<Integer> recentPeriod = validHistory.subList(validHistory.size()-window, validHistory.size());

        double prevAvg = exponentialMovingAverage(prevPeriod);
        double recentAvg = exponentialMovingAverage(recentPeriod);

        if (prevAvg == 0) return 1;
        double rate = (recentAvg - prevAvg) / prevAvg;

        if (rate >= TREND_THRESHOLD) return 2;
        if (rate <= -TREND_THRESHOLD) return 0;
        return 1;
    }

    // 指数移动平均
    private double exponentialMovingAverage(List<Integer> data) {
        double ema = data.get(0);
        double alpha = 0.3;
        for (int i = 1; i < data.size(); i++) {
            ema = alpha * data.get(i) + (1 - alpha) * ema;
        }
        return ema;
    }

    // 模型训练
    private void trainModel(List<LabeledData> trainingData) {
        INDArray features = Nd4j.zeros(trainingData.size(), featureSize);
        INDArray labels = Nd4j.zeros(trainingData.size(), 3);

        for (int i = 0; i < trainingData.size(); i++) {
            LabeledData data = trainingData.get(i);
            OrderRecord r = data.order;

            int goodsIndex = r.goodsid - 1;
            features.putScalar(i, goodsIndex, 1.0);
            features.putScalar(i, totalGoods, normalizeSales(r.goodsnum));
            features.putScalar(i, totalGoods + 1, normalizeDayOfWeek(r.createTime));
            features.putScalar(i, totalGoods + 2, normalizeMonth(r.createTime));
            features.putScalar(i, totalGoods + 3, growthRateFeature(goodsHistory.get(r.goodsid)));

            labels.putScalar(i, data.label, 1.0);
        }

        DataSet dataset = new DataSet(features, labels);
        dataset.shuffle(System.currentTimeMillis());

        model = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(123)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(0.001))
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(featureSize).nOut(128)
                        .activation(Activation.RELU)
                        .build())
                .layer(new DropoutLayer.Builder(0.3).build())
                .layer(new DenseLayer.Builder()
                        .nIn(128).nOut(64)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(64).nOut(3)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build());
        model.init();
        model.fit(dataset);
    }

    // 特征归一化方法
    private double normalizeSales(int sales) {
        return Math.log1p(sales) / Math.log1p(SALES_SCALE);
    }

    private double normalizeDayOfWeek(LocalDateTime time) {
        return time.getDayOfWeek().getValue() / 7.0;
    }

    private double normalizeMonth(LocalDateTime time) {
        return (time.getMonthValue() / 12.0) * 2 * Math.PI;
    }

    // 增长率特征
    private double growthRateFeature(Deque<Integer> history) {
        if (history.size() < 2) return 0.0;
        int last = history.getLast();
        int prev = history.stream()
                .skip(history.size() - 2)
                .findFirst()
                .orElse(last);
        return (last - prev) / (double) Math.max(prev, 1);
    }

    // 预测方法
    public String predictTrend(int goodsId) {
        // 双重校验机制
        if (goodsId < 1 || goodsId > totalGoods) {
            return "无效商品ID";
        }
        if (!goodsHistory.containsKey(goodsId)) {
            return "暂无购买记录";
        }

        Deque<Integer> history = goodsHistory.get(goodsId);
        if (history.isEmpty()) {
            return "暂无有效销售数据";
        }

        INDArray input = Nd4j.zeros(1, featureSize);
        try {
            input.putScalar(0, goodsId - 1, 1.0);
            input.putScalar(0, totalGoods, normalizeSales(history.getLast()));
            input.putScalar(0, totalGoods + 1, normalizeDayOfWeek(LocalDateTime.now()));
            input.putScalar(0, totalGoods + 2, normalizeMonth(LocalDateTime.now()));
            input.putScalar(0, totalGoods + 3, growthRateFeature(history));
        } catch (Exception e) {
            return "数据预处理异常";
        }

        INDArray output = model.output(input);
        return parsePrediction(output);
    }

    // 预测结果解析
    private String parsePrediction(INDArray output) {
        double[] probs = output.toDoubleVector();
        int maxIdx = 0;
        for (int i = 1; i < probs.length; i++) {
            if (probs[i] > probs[maxIdx]) {
                maxIdx = i;
            }
        }

        switch (maxIdx) {
            case 0: return "下降";
            case 1: return "平稳";
            case 2: return "上升";
            default: return "预测异常";
        }
    }

    // 内部数据类
    private static class LabeledData {
        final OrderRecord order;
        final int label;

        LabeledData(OrderRecord order, int label) {
            this.order = order;
            this.label = label;
        }
    }
}