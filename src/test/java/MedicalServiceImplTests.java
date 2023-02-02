import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTests {

    @Test
    void testCheckBloodPressure(){

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("1"))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));


        SendAlertService alertService = Mockito.mock();


        BloodPressure currentPressure_1 = new BloodPressure(60, 120);
        BloodPressure currentPressure_2 = new BloodPressure(120,80);

        MedicalService medicalService_1 = new MedicalServiceImpl(patientInfoRepository,alertService);
        medicalService_1.checkBloodPressure("1", currentPressure_1);

        MedicalService medicalService_2 = new MedicalServiceImpl(patientInfoRepository,alertService);
        medicalService_2.checkBloodPressure("1", currentPressure_2);

        PatientInfo patientInfo = patientInfoRepository.getById("1");

        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        Mockito.verify(alertService, Mockito.times(1)).send(message);








    }
}
