package com.ad.mysql.listener;

import com.ad.mysql.dto.BinlogRowData;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public interface Ilistener {

    void register();
// 这里我们已经把event转换成BinlogRowData类型了
    void onEvent(BinlogRowData eventData);
}
