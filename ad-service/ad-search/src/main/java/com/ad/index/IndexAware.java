package com.ad.index;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
//索引的接口
public interface IndexAware<K,V> {
    V get(K key);
    void add(K key,V value);
    void update(K key,V value);
    void delete(K key,V value);
}
