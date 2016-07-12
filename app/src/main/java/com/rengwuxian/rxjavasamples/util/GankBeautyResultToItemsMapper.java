// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.util;

import com.rengwuxian.rxjavasamples.model.GankBeauty;
import com.rengwuxian.rxjavasamples.model.GankBeautyResult;
import com.rengwuxian.rxjavasamples.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Func1;
//单列维护拉出转化数据
public class GankBeautyResultToItemsMapper implements Func1<GankBeautyResult, List<Item>> {

    private static GankBeautyResultToItemsMapper INSTANCE = new GankBeautyResultToItemsMapper();

    private GankBeautyResultToItemsMapper() {

    }

    public static GankBeautyResultToItemsMapper getInstance() {
        return INSTANCE;
    }

    // map时候会被回调
    @Override
    public List<Item> call(GankBeautyResult gankBeautyResult) { //外面传进来的数据  获取以后
        List<GankBeauty> gankBeauties = gankBeautyResult.beauties;

        List<Item> items = new ArrayList<>(gankBeauties.size());//新的集合 重新改变数据

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        for (GankBeauty gankBeauty : gankBeauties) {
            Item item = new Item();
            try {
                Date date = inputFormat.parse(gankBeauty.createdAt);
                item.description = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                item.description = "unknown date";
            }
            item.imageUrl = gankBeauty.url;
            items.add(item);
        }
        return items;// 返回的集合最后被回调的 给onnext方法获取到然后observe 返回类型就应该是List<Item>
    }
}
