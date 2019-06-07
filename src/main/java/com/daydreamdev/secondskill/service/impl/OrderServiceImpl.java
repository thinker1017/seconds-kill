package com.daydreamdev.secondskill.service.impl;

import com.daydreamdev.secondskill.dao.StockOrderMapper;
import com.daydreamdev.secondskill.pojo.Stock;
import com.daydreamdev.secondskill.pojo.StockOrder;
import com.daydreamdev.secondskill.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @auther G.Fukang
 * @date 6/7 12:44
 */
@Transactional(rollbackFor = Exception.class)
@Service(value = "OrderService")
public class OrderServiceImpl implements OrderService{

    @Autowired
    private StockServiceImpl stockService;

    @Autowired
    private StockOrderMapper orderMapper;

    @Override
    public int createWrongOrder(int sid) {
       Stock stock = checkStock(sid);
       saleStock(stock);
       int id = createOrder(stock);

       return id;
    }

    /**
     * 校验库存
     */
    private Stock checkStock(int sid) {
        Stock stock = stockService.getStockById(sid);
        if (stock.getSale().equals(stock.getCount())) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    /**
     * 扣库存
     */
    private int saleStock(Stock stock){
        stock.setSale(stock.getSale() + 1);
        return stockService.updateStockById(stock);
    }

    /**
     * 创建订单
     */
    private int createOrder(Stock stock) {
        StockOrder order = new StockOrder();
        order.setSid(stock.getId());
        order.setName(stock.getName());
        order.setCreateTime(new Date());
        int id = orderMapper.insertSelective(order);
        return id;
    }
}