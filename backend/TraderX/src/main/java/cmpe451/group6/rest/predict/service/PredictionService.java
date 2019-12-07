package cmpe451.group6.rest.predict.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.model.HistoricalValue;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.equipment.repository.HistoricalValueRepository;
import cmpe451.group6.rest.predict.dto.PredictionDTO;
import cmpe451.group6.rest.predict.dto.PredictionStatsDTO;
import cmpe451.group6.rest.predict.model.Prediction;
import cmpe451.group6.rest.predict.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredictionService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    HistoricalValueRepository historicalValueRepository;

    @Autowired
    PredictionRepository predictionRepository;

    public String createNewPrediction(String username, PredictionDTO dto){
        User requester = userRepository.findByUsername(username);
        if (requester == null) throw new CustomException("No such a user found.", HttpStatus.NOT_ACCEPTABLE); // 406
        if (dto == null) throw new CustomException("DTO cannot be null.", HttpStatus.NOT_ACCEPTABLE); // 406
        Equipment equipment = equipmentRepository.findByCode(dto.getEquipmentCode());
        if (equipment == null) throw new CustomException("No such equipment.", HttpStatus.NOT_ACCEPTABLE); // 406
        Date today = extractTime(new Date()); // w/o time, only date
        if (predictionRepository.findByUser_UsernameAndPredictionDateAndEquipment_Code(username,today,equipment.getCode()) != null){
            throw new CustomException("User already made a prediction for this equipment today. Try edit it.", HttpStatus.NOT_ACCEPTABLE); // 406
        }
        Prediction newPred = new Prediction(requester,equipment,dto.getPredictionType(),today);
        predictionRepository.save(newPred);
        return "Prediction has been saved.";
    }

    public PredictionStatsDTO getStats(String username){
        if (!userRepository.existsByUsername(username)) throw new CustomException("No such user", HttpStatus.NOT_ACCEPTABLE); //406
        List<Prediction> predictions = predictionRepository.findByUser_Username(username);
        int total = predictions.size();
        int success, fail, notEval;
        success = fail = notEval = 0;
        for (Prediction p : predictions) {
            if (p.getSuccess() == null) {
                notEval++;
            } else if (p.getSuccess()) {
                success++;
            } else {
                fail++;
            }
        }
        return new PredictionStatsDTO(username,total,notEval,success,fail);
    }

    public String editPrediction(String username, int predictionId, Prediction.PredictionType newType){
        Prediction prediction = validateAndGetPred(username,predictionId);
        prediction.setPredictionType(newType);
        predictionRepository.save(prediction);
        return "Prediction is updated.";
    }

    public String deletePrediction(String username, int predictionId){
        Prediction prediction = validateAndGetPred(username,predictionId);
        predictionRepository.delete(prediction);
        return "Prediction is deleted.";
    }

    public List<PredictionDTO> getPredictions(String username, String code, boolean queryingSelf){
        // TODO : May be check for profile privacy ?
        if(!userRepository.existsByUsername(username) || !equipmentRepository.existsByCode(code))
            throw new CustomException("No such equipment/user", HttpStatus.NOT_ACCEPTABLE); // 406
        List<Prediction> predictionsList = predictionRepository.findByUser_UsernameAndEquipment_Code(username,code);
        return predictionsList.stream().map(p ->
                new PredictionDTO(queryingSelf ? p.getId() : null , p.getEquipment().getCode(),p.getPredictionType(),p.getSuccess(),
                        p.getPredictionDate())).collect(Collectors.toList());
    }

    public List<PredictionDTO> getPredictions(String username, boolean queryingSelf){
        // TODO : May be check for profile privacy ?
        List<Prediction> predictionsList = predictionRepository.findByUser_Username(username);
        return predictionsList.stream().map(p ->
                new PredictionDTO(queryingSelf ? p.getId() : null,p.getEquipment().getCode(),p.getPredictionType(),p.getSuccess(),
                        p.getPredictionDate())).collect(Collectors.toList());
    }

    @Async("threadPoolTaskExecutor")
    public void updatePredictions(String code){
        List<Prediction> preds = predictionRepository.findByEquipment_CodeAndSuccessIsNull(code); // find not evaluated codes.
        for (Prediction p : preds){
            evaluateAndSavePrediction(p);
        }
    }

    private void evaluateAndSavePrediction(Prediction prediction){
        if (prediction == null || prediction.getSuccess() != null) return;
        String code = prediction.getEquipment().getCode();
        Equipment equipment = equipmentRepository.findByCode(code);
        HistoricalValue hw = historicalValueRepository.findHistoricalValueByDate(code,prediction.getPredictionDate());
        if (hw == null || equipment == null) return;
        double lastDayAvg = (hw.getOpen() + hw.getClose())/2;
        double current = equipment.getCurrentValue();
        switch (prediction.getPredictionType()){
            case INCREASE:
                prediction.setSuccess(current > lastDayAvg);
                break;
            case DECREASE:
                prediction.setSuccess(current < lastDayAvg);
                break;
            case STABLE:
                prediction.setSuccess(current == lastDayAvg);
                break;
        }
        predictionRepository.save(prediction);
    }

    private Prediction validateAndGetPred(String username, int predictionId){
        User requester = userRepository.findByUsername(username);
        if (requester == null) throw new CustomException("No such a user found.", HttpStatus.NOT_ACCEPTABLE); // 406
        Prediction prediction = predictionRepository.findByIdAndUser_Username(predictionId, username);
        if (prediction == null) throw new CustomException("No such prediction is found for that user.", HttpStatus.NOT_ACCEPTABLE); // 406
        boolean evaluated = prediction.getSuccess() == null;
        if (!evaluated) throw new CustomException("Cannot modify a prediction already evaluated.", HttpStatus.NOT_ACCEPTABLE); // 406
        return prediction;
    }

    // yyyy-mm-dd date of the given datetime
    private Date extractTime(Date datetime) {
        try {
            final DateFormat df = EquipmentConfig.df;
            return df.parse(df.format(datetime));
        } catch (ParseException e) {
            return datetime;
        }
    }

}
