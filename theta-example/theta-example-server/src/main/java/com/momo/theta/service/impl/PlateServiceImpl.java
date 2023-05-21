package com.momo.theta.service.impl;

import com.momo.theta.model.ExtParam;
import com.momo.theta.model.ImageMat;
import com.momo.theta.model.LocationPoint;
import com.momo.theta.model.PlateColor;
import com.momo.theta.model.PlateImage;
import com.momo.theta.model.PlateInfo;
import com.momo.theta.model.PlateInfoRepVO;
import com.momo.theta.model.PlateInfoReqVO;
import com.momo.theta.model.PlateLayout;
import com.momo.theta.model.PlateLocation;
import com.momo.theta.model.RecognitionInfo;
import com.momo.theta.extract.PlateExtractor;
import com.momo.theta.service.PlateService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("plateServiceImpl")
public class PlateServiceImpl implements PlateService {

  @Resource
  private PlateExtractor plateExtractor;


  @Override
  public List<PlateInfoRepVO> recognition(PlateInfoReqVO plateInfoReq) {
    //模型检测车牌信息
    ImageMat imageMat = null;
    PlateImage plateImage = null;
    try {
      int topK = (null == plateInfoReq.getLimit() || plateInfoReq.getLimit() <= 0) ? 5
          : plateInfoReq.getLimit();
      ExtParam extParam = ExtParam.build().setTopK(topK);
      imageMat = ImageMat.fromBase64(plateInfoReq.getImage());
      plateImage = plateExtractor.extract(imageMat, extParam, new HashMap<>());
    } finally {
      if (null != imageMat) {
        imageMat.release();
      }
    }
    if (null == plateImage) {
      throw new RuntimeException("PlateExtractor extract error");
    }
    //转换模型结果，并输出
    List<PlateInfoRepVO> plates = new ArrayList<>();
    if (null != plateImage.PlateInfos() && !plateImage.PlateInfos().isEmpty()) {
      for (PlateInfo plateInfo : plateImage.PlateInfos()) {
        //检测信息
        PlateInfoRepVO plate = new PlateInfoRepVO();
        plate.setScore((float) Math.floor(plateInfo.score * 1000000) / 10000);
        PlateLocation location = new PlateLocation();
        location.setLeftTop(new LocationPoint(plateInfo.box.leftTop.x, plateInfo.box.leftTop.y));
        location.setRightTop(new LocationPoint(plateInfo.box.rightTop.x, plateInfo.box.rightTop.y));
        location.setRightBottom(
            new LocationPoint(plateInfo.box.rightBottom.x, plateInfo.box.rightBottom.y));
        location.setLeftBottom(
            new LocationPoint(plateInfo.box.leftBottom.x, plateInfo.box.leftBottom.y));
        plate.setLocation(location);

        //识别信息
        RecognitionInfo recognition = new RecognitionInfo();
        recognition.setLayout(plateInfo.single ? PlateLayout.SINGLE : PlateLayout.DOUBLE);
        recognition.setPlateNo(plateInfo.parseInfo.plateNo);
        recognition.setPlateColor(PlateColor.valueOfName(plateInfo.parseInfo.plateColor));
        plate.setRecognition(recognition);
        //添加车牌
        plates.add(plate);
      }
    }
    return plates;
  }
}
