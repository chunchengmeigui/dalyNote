package com.ex.batisdemo.service;

import com.ex.batisdemo.domain.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
public interface UserService {

    /*<AUTOGEN--BEGIN>*/

    List<User> selectPaged();

    User selectByPrimaryKey(Integer userId);

    Integer deleteByPrimaryKey(Integer userId);

    Integer insert(User user);

    Integer insertSelective(User user);

    Integer insertSelectiveIgnore(User user);

    Integer updateByPrimaryKeySelective(User user);

    Integer updateByPrimaryKey(User user);

    Integer batchInsert(List<User> list);

    Integer batchUpdate(List<User> list);

    /**
     * 存在即更新
     *
     * @return
     */
    Integer upsert(User user);

    /**
     * 存在即更新，可选择具体属性
     *
     * @param map
     * @return
     */
    Integer upsertSelective(User user);

    List<User> query(User user);

    Long queryTotal();

    Integer deleteBatch(List<Integer> list);

    /*<AUTOGEN--END>*/

}
