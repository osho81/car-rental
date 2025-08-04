package com.osho.CarRental.service.repository;

import com.osho.CarRental.valueobject.ResponseTemplateVO;

public interface ExchangeServiceRepository {
    ResponseTemplateVO getExchangeService(int orderId);
}
