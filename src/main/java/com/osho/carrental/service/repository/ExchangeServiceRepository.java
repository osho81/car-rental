package com.osho.carrental.service.repository;

import com.osho.carrental.valueobject.ResponseTemplateVO;

public interface ExchangeServiceRepository {
    ResponseTemplateVO getExchangeService(int orderId);
}
