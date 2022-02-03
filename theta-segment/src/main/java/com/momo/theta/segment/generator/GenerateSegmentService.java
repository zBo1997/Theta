package com.momo.theta.segment.generator;


import com.momo.theta.core.factory.ThetaServiceFactory;
import com.momo.theta.segment.api.ThetaSegment;
import com.momo.theta.segment.config.SegmentConfig;


/**
 * 使用SpringApplicationContext中获取指定的生成器
 */
public class GenerateSegmentService {

    /**
     * 获取的SegmentGenerate
     * @param segmentConfig
     * @return
     */
    public static ThetaSegment getSegment(SegmentConfig segmentConfig) {
        ThetaSegment segment = ThetaServiceFactory.getService(segmentConfig.getType(), ThetaSegment.class);
        return segment;
    }

    /**
     * 根据指定类型获取指定的SegmentGenerate
     * @param segmentConfig
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> T getSegment(SegmentConfig segmentConfig, Class<T> classType) {
        T segment = ThetaServiceFactory.getService(segmentConfig.getType(), classType);
        return segment;
    }
}
