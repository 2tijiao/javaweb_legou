package com.lilma.legou.service;

import com.lilma.legou.pojo.AnomalOrder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AnomalyDetectionService {

    @Getter
    public enum AnomalyType {
        UNIT_PRICE_EXCEEDED("单价超过阈值"),
        QUANTITY_EXCEEDED("购买数量超过阈值"),
        TOTAL_PRICE_EXCEEDED("订单总价超过阈值");

        private final String description;

        AnomalyType(String description) {
            this.description = description;
        }

    }

    @Getter
    public static class AnomalyDetail {
        // Getter 方法
        private final Integer orderId;
        private final AnomalyType anomalyType;
        private final String anomalyContent;

        public AnomalyDetail(Integer orderId, AnomalyType anomalyType, String anomalyContent) {
            this.orderId = orderId;
            this.anomalyType = anomalyType;
            this.anomalyContent = anomalyContent;
        }

        @Override
        public String toString() {
            return String.format("订单ID: %d | 异常类型: %s | 异常详情: %s",
                    orderId, anomalyType.getDescription(), anomalyContent);
        }
    }

    public List<AnomalyDetail> detectAnomalies(List<AnomalOrder> orders) {
        List<AnomalyDetail> anomalies = new ArrayList<>();

        for (AnomalOrder order : orders) {
            List<AnomalyDetail> orderAnomalies = checkOrderAnomalies(order);
            anomalies.addAll(orderAnomalies);
        }

        return anomalies;
    }

    private List<AnomalyDetail> checkOrderAnomalies(AnomalOrder order) {
        List<AnomalyDetail> details = new ArrayList<>();

        // 检测单价异常
        if (order.getGoodsprice().compareTo(SalesThresholds.MAX_UNIT_PRICE) > 0) {
            String content = String.format("当前单价 %.2f 元，阈值 %.2f 元",
                    order.getGoodsprice(),
                    SalesThresholds.MAX_UNIT_PRICE);
            details.add(new AnomalyDetail(
                    order.getId(),
                    AnomalyType.UNIT_PRICE_EXCEEDED,
                    content
            ));
        }

        // 检测数量异常
        if (order.getGoodsnum() > SalesThresholds.MAX_QUANTITY) {
            String content = String.format("当前数量 %d 件，阈值 %d 件",
                    order.getGoodsnum(),
                    SalesThresholds.MAX_QUANTITY);
            details.add(new AnomalyDetail(
                    order.getId(),
                    AnomalyType.QUANTITY_EXCEEDED,
                    content
            ));
        }

        // 检测总价异常
        BigDecimal calculatedTotal = order.getGoodsprice().multiply(
                BigDecimal.valueOf(order.getGoodsnum())
        );
        if (calculatedTotal.compareTo(SalesThresholds.MAX_TOTAL_PRICE) > 0) {
            String content = String.format("计算总价 %.2f 元，阈值 %.2f 元",
                    calculatedTotal,
                    SalesThresholds.MAX_TOTAL_PRICE);
            details.add(new AnomalyDetail(
                    order.getId(),
                    AnomalyType.TOTAL_PRICE_EXCEEDED,
                    content
            ));
        }

        return details;
    }

    // 阈值配置类
    private static class SalesThresholds {
        static final BigDecimal MAX_UNIT_PRICE = new BigDecimal("99999.99");
        static final int MAX_QUANTITY = 99;
        static final BigDecimal MAX_TOTAL_PRICE = new BigDecimal("499999.00");
    }
}