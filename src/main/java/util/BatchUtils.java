package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhi on 2016/11/29.
 */
public class BatchUtils<T> {
    /**
     * 因为某种原因，在批量插入时，对插入列表进行拆分
     * @param loo
     * @param ioo
     * @return
     */
    public static <T> List<List<T>> split(List<T> loo, int ioo){
        int idNoNum = loo.size();
        int times = idNoNum / ioo; // 循环次数
        int lastNum = idNoNum % ioo;
        if(lastNum > 0){
            times = times + 1; // 说明不能整除，要多循环一次
        }

        int firstIndex = 0;
        List< List<T>> result = new ArrayList< List<T>>();
        for(int i=0; i<times; i++){
            List<T> item;
            if(i == times -1){
                // 能否整除决定了最后一次的个数
                item = loo.subList(firstIndex, firstIndex + (lastNum == 0 ? ioo : lastNum));
            }else{
                item = loo.subList(firstIndex, firstIndex + ioo);
            }
            firstIndex += ioo;
            result.add(item);
        }
        return result;
    }

    public static void main(String args[]){
        List<String> loo = new ArrayList<>();
        for(int i=0; i< 3; i++){
            loo.add("string___" + i);

        }
        List<List<String>> foo = split(loo, 5);
        System.out.print(foo);
    }
}
