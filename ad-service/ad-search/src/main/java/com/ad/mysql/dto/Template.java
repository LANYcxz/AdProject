package com.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {
    private String database;
    private List<JsonTable> tableList;
}
