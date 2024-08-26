package ru.mts.siebel.orders.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mts.siebel.orders.entity.Storage;
import ru.mts.siebel.orders.repository.StorageRepository;

import java.util.List;


@Service
public class StorageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageRepository repository;

    public List<Storage> findAll() {
        return repository.findAll();
    }

    public Storage findByProductCode(final String productCode) {
        if (size() == 0) {
            logger.info("На складе отсутсвуют товары");
        } else {
            List<Storage> storageList = findAll();
            for (Storage storage : storageList) {
                if (storage.getProductCode().equals(productCode))
                    return storage;
            }
        }
        return null;
    }

    public int size() {
        return findAll().size();
    }

    @Transactional
    public void add(final Storage storage) {
        repository.save(storage);
    }

    public boolean checkAndReserve(final String productCode, final Integer quantity) {
        Storage storage = findByProductCode(productCode);
        if (storage != null) {
            Integer storageQuantity = storage.getQuantity();
            if (quantity <= storageQuantity) {
                storage.setQuantity(storageQuantity - quantity);
                repository.save(storage);

                logger.info("Товар " + productCode + " в количестве " + quantity + " зарезервирован на складе");

                return true;
            } else {
                logger.info("Недостаточно товара {} на складе. Запрошено: {}, в наличии: {}", productCode, quantity, storageQuantity);
                return false;
            }
        } else {
            logger.info("Товар " + productCode + " не найден на складе");
        }
        return false;
    }

}
