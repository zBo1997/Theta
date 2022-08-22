package com.momo.theta.mq.queue;

import com.alibaba.fastjson.JSON;
import com.momo.theta.mq.queue.data.MqData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class FileStore {

    private FileStore() {
    }

    private static final ReentrantLock lock = new ReentrantLock();


    /**
     * 分页按行读取文件
     *
     * @param path  文件路径
     * @param point 起始未知，首次查询为0
     * @param size  分页大小
     * @return
     */
    public static Map<String, Object> read(String path, long point, int size) {
        Map<String, Object> map = new HashMap<>(2);
        ArrayList<MqData> mqDataList = new ArrayList<>();

        String line;
        File file = new File(path);
        if (!file.exists()) {
            map.put("point", point);
            map.put("data", mqDataList);
            return map;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(point);
            for (int i = 0; i < size; i++) {
                line = raf.readLine();
                if (StringUtils.isEmpty(line)) {
                    break;
                }
                byte[] mqDataBase64 = Base64.getDecoder().decode(line);
                String mqDataJson = new String(mqDataBase64, StandardCharsets.UTF_8);
                mqDataList.add(JSON.parseObject(mqDataJson, MqData.class));
            }
            point = raf.getFilePointer();
            map.put("point", point);
            map.put("data", mqDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 写入文件
     *
     * @param mqData mq消息
     */
    public static void write(String path, MqData mqData) {

        File file = new File(path);
        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            log.error("创建文件目录失败，请检查权限");
            return;
        }

        lock.lock();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
            String mqDataBase64 = Base64.getEncoder().encodeToString(JSON.toJSONString(mqData).getBytes(StandardCharsets.UTF_8));
            bufferedWriter.write(mqDataBase64);
            bufferedWriter.newLine();
            log.info("写入文件完成 key={}", mqData.getKeys());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void delete(String path) {
        File file = new File(path);
        if (file.exists() && file.delete()) {
            log.info("文件删除成功 path={}", path);
        }
    }

}
