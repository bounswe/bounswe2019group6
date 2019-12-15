package cmpe451.group6.rest.equipment.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.dto.EquipmentHistoryDTO;
import cmpe451.group6.rest.equipment.dto.EquipmentMetaWrapper;
import cmpe451.group6.rest.equipment.dto.EquipmentResponseDTO;
import cmpe451.group6.rest.equipment.model.EquipmentType;
import cmpe451.group6.rest.equipment.model.HistoricalValue;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.equipment.repository.HistoricalValueRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    Logger logger = Logger.getLogger(EquipmentService.class.getName());

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    HistoricalValueRepository historicalValueRepository;

    @Autowired
    EquipmentUpdateService equipmentUpdateService;

    @Autowired
    private ModelMapper modelMapper;

    public EquipmentResponseDTO getEquipment(String code){
        Equipment equipment = equipmentRepository.findByCode(code);
        if(equipment == null){
            throw new CustomException("No such an equipment found", HttpStatus.BAD_REQUEST);
        }
        List<HistoricalValue> history = historicalValueRepository.findAllByEquipment_Code(code);
        history.sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));
        List<EquipmentHistoryDTO> hist = history.stream().map(h ->
                (modelMapper.map(h,EquipmentHistoryDTO.class))).collect(Collectors.toList());

        return new EquipmentResponseDTO(modelMapper.map(equipment,EquipmentResponseDTO.EquipmentDTO.class),hist);
    }

    /**
     * Calculates the change ratio of the specified currency, respected to current
     * value and yesterday's closing
     * 
     * @param code
     * @return change ratio (in percent)
     */
    public double getDailyChange(String code) {

        EquipmentResponseDTO equipment = this.getEquipment(code);

        List<EquipmentHistoryDTO> hist = equipment.getHistoricalValues();

        if (hist.isEmpty()) {
            throw new CustomException("historical values not initialized", HttpStatus.NOT_ACCEPTABLE);
        }

        int histSize = hist.size();
        

        double currentVal = equipment.getEquipment().getCurrentValue();
        double prevVal = hist.get(histSize - 1).getClose();

        return ((currentVal - prevVal) * 100.0 / prevVal);
    }

    public EquipmentMetaWrapper getCurrencies(){
        return rearrangeResponse(concatenate(EquipmentConfig.CURRENCIES_BATCH_1,
                EquipmentConfig.CURRENCIES_BATCH_2));
    }

    public EquipmentMetaWrapper getCryptoCurrencies(){
        return rearrangeResponse(concatenate(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_1,
                EquipmentConfig.CRYPTO_CURRENCIES_BATCH_2));
    }

    public EquipmentMetaWrapper getStocks(){
        return rearrangeResponse(concatenate(EquipmentConfig.STOCKS_BATCH_1,
                EquipmentConfig.STOCKS_BATCH_2));
    }

    public void forceInit(String code, String type){
        logger.warning("Forcefully initializing " + code);
        equipmentUpdateService.initializeEquipment(new String[]{code},stringToType(type));
    }

    public void forceUpdate(String code, String type){
        logger.warning("Forcefully updating: " + code);
        equipmentUpdateService.updateLatestValues(new String[]{code},stringToType(type));
    }

    public void forceLoadHistory(String code, String type){
        logger.warning("Forcefully loading history: " + code);
        equipmentUpdateService.loadEquipmentHistory(new String[]{code},stringToType(type),false);
    }

    private <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    private EquipmentType stringToType(String type){
        if(type.equals("CURRENCY")) return EquipmentType.CURRENCY;
        if(type.equals("CRYPTO_CURRENCY")) return EquipmentType.CRYPTO_CURRENCY;
        if(type.equals("STOCK")) return EquipmentType.STOCK;
        throw new CustomException("Invalid Equipment type: " + type, HttpStatus.NOT_ACCEPTABLE);
    }

    private EquipmentMetaWrapper rearrangeResponse(String[] equipments){

        List<EquipmentMetaWrapper.EquipmentMeta> metaList = new ArrayList<>();
        for (String code: equipments) {
            Equipment current = equipmentRepository.findByCode(code);
            if (current == null) continue;
            double currentVal = current.getCurrentValue();
            double currentStock = current.getCurrentStock();
            EquipmentMetaWrapper.EquipmentInfo info = new EquipmentMetaWrapper.EquipmentInfo(currentVal,currentStock);
            EquipmentMetaWrapper.EquipmentMeta meta = new EquipmentMetaWrapper.EquipmentMeta(code,info);
            metaList.add(meta);
        }

        return new EquipmentMetaWrapper(metaList,EquipmentConfig.BASE_CURRENCY_CODE);
    }

}
