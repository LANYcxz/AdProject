package com.ad.sender;

import com.ad.mysql.dto.MySqlRowData;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
//投递增量数据的接口
public interface ISender {
    void sender(MySqlRowData rowData);
}
