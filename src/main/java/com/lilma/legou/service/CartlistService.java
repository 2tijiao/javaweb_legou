package com.lilma.legou.service;

import com.lilma.legou.pojo.CartList;

public interface CartlistService {
    CartList[] getlist(Integer userid);

    void delete(Integer id);

    boolean finish(CartList[] lists);

    void addCartlist(Integer goodsid, Integer goodsnum, Integer userid);

}
